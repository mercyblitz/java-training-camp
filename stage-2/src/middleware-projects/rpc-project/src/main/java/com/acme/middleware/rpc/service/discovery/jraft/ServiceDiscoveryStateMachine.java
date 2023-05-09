/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.middleware.rpc.service.discovery.jraft;

import com.acme.middleware.rpc.service.ServiceInstance;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.error.RaftError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务发现状态机 FSM
 */
public class ServiceDiscoveryStateMachine extends StateMachineAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryStateMachine.class);

    /**
     * 服务名称与服务实例列表（List）映射
     */
    private final Map<String, Map<String, ServiceInstance>> serviceNameToInstancesStorage = new ConcurrentHashMap<>();

    /**
     * Leader term
     */
    private final AtomicLong leaderTerm = new AtomicLong(-1);

    private Node node;

    public boolean isLeader() {
        return this.leaderTerm.get() > 0;
    }

    @Override
    public void onApply(final Iterator iter) {
        while (iter.hasNext()) {
            ServiceDiscoveryOperation operation = null;

            ServiceDiscoveryOperationClosure closure = null;
            if (iter.done() != null) {
                // 从当前 Leader 节点获取  Closure
                closure = (ServiceDiscoveryOperationClosure) iter.done();
                operation = closure.getServiceDiscoveryOperation();
                logger.info("The closure with operation[{}] at the Leader node[{}]", operation, node);
            } else {
                // 在 Follower 节点通过 日志反序列化得到 ServiceDiscoveryOperation
                final ByteBuffer data = iter.getData();
                operation = ServiceDiscoveryOperation.deserialize(data);
                logger.info("The closure with operation[{}] at the Follower node[{}]", operation, node);
            }

            if (operation != null) {
                ServiceDiscoveryOperation.Kind kind = operation.getKind();
                switch (kind) {
                    case REGISTRATION:
                        // 写入内存操作
                        register((ServiceInstance) operation.getData());
                        break;
                    case DEREGISTRATION:
                        deregister((ServiceInstance) operation.getData());
                        break;
                    case GET_SERVICE_INSTANCES:
                        getServiceInstances(closure, operation);
                        break;
                }

                if (closure != null) {
                    closure.run(Status.OK());
                }
            }

            iter.next();
        }
    }

    @Override
    public void onLeaderStart(final long term) {
        this.leaderTerm.set(term);
        super.onLeaderStart(term);
    }

    @Override
    public void onLeaderStop(final Status status) {
        this.leaderTerm.set(-1);
        super.onLeaderStop(status);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    private void register(ServiceInstance serviceInstance) {
        String serviceName = serviceInstance.getServiceName();
        String id = serviceInstance.getId();

        synchronized (serviceNameToInstancesStorage) {
            Map<String, ServiceInstance> serviceInstancesMap = serviceNameToInstancesStorage.computeIfAbsent(serviceName,
                    n -> new LinkedHashMap<>());
            serviceInstancesMap.put(id, serviceInstance);
        }

        logger.info("{} has been registered at the node[{}]", serviceInstance, node);
    }

    private void deregister(ServiceInstance serviceInstance) {
        String serviceName = serviceInstance.getServiceName();
        String id = serviceInstance.getId();

        synchronized (serviceNameToInstancesStorage) {
            Map<String, ServiceInstance> serviceInstancesMap = getServiceInstancesMap(serviceName);
            serviceInstancesMap.remove(id);
        }

        logger.info("{} has been deregistered at the node[{}]", serviceInstance, node);
    }

    private void getServiceInstances(ServiceDiscoveryOperationClosure closure, ServiceDiscoveryOperation<String> operation) {
        if (!isLeader()) {
            return;
        }
        String serviceName = operation.getData();

        Map<String, ServiceInstance> serviceInstancesMap = getServiceInstancesMap(serviceName);
        closure.setResult(serviceInstancesMap.values());
    }

    private Map<String, ServiceInstance> getServiceInstancesMap(String serviceName) {
        return serviceNameToInstancesStorage.computeIfAbsent(serviceName, n -> new LinkedHashMap<>());
    }

}
