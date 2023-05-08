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
import com.acme.middleware.rpc.service.discovery.proto.ServiceDiscoveryOuter;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.acme.middleware.rpc.service.discovery.jraft.ServiceDiscoveryOperation.Kind.GET_SERVICE_INSTANCES;

/**
 * {@link ServiceDiscoveryOuter.GetServiceInstancesRequest 服务实例注册请求}处理器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class GetServiceInstancesRequestRpcProcessor implements RpcProcessor<ServiceDiscoveryOuter.GetServiceInstancesRequest> {

    private static final Logger logger = LoggerFactory.getLogger(GetServiceInstancesRequestRpcProcessor.class);

    private final ServiceDiscoveryServer server;

    public GetServiceInstancesRequestRpcProcessor(ServiceDiscoveryServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(RpcContext rpcCtx, ServiceDiscoveryOuter.GetServiceInstancesRequest request) {

        final String serviceName = request.getServiceName();

        final ServiceDiscoveryOperation.Kind kind = GET_SERVICE_INSTANCES;

        ServiceDiscoveryOperation operation = new ServiceDiscoveryOperation(kind, serviceName);

        final ServiceDiscoveryOperationClosure closure = new ServiceDiscoveryOperationClosure(operation, (status, result) -> {
            if (!status.isOk()) {
                logger.warn("Closure status is : {} at the {}", status, server.getNode());
                return;
            }
            // RPC 响应到客户端
            rpcCtx.sendResponse(response(result));
            logger.info("'{}' has been handled ,serviceName : '{}' , result : {} , status : {}",
                    kind, serviceName, result, status);
        });

        if (!isLeader()) {
            handlerNotLeaderError(closure);
            return;
        }

        Task task = new Task();
        // 将待注册的服务实例序列化成 byte 数组
        // 写入到本地日志，将作为 AppendEntries RPC 请求的来源 -> Followers
        task.setData(operation.serialize());
        // 设置 ServiceInstanceRegistrationClosure
        // 触发 Leader 节点上的状态机 -> ServiceRegistrationStateMachine.onApply
        task.setDone(closure);
        // 提交任务
        getNode().apply(task);

        logger.info("The task of '{}' has been applied , serviceName : '{}'", operation.getKind(), operation.getData());
    }

    private boolean isLeader() {
        return getFsm().isLeader();
    }

    private void handlerNotLeaderError(final Closure closure) {
        logger.error("No Leader node : {}", getNode().getNodeId());
        closure.run(new Status(RaftError.EPERM, "Not leader"));
    }

    private Node getNode() {
        return this.server.getNode();
    }

    private ServiceDiscoveryStateMachine getFsm() {
        return this.server.getFsm();
    }


    private ServiceDiscoveryOuter.GetServiceInstancesResponse response(Object result) {
        Collection<ServiceInstance> serviceInstances = (Collection<ServiceInstance>) result;
        ServiceDiscoveryOuter.GetServiceInstancesResponse response = ServiceDiscoveryOuter.GetServiceInstancesResponse.newBuilder()
                .addAllValue(adaptRegistrations(serviceInstances))
                .build();
        return response;
    }

    private List<ServiceDiscoveryOuter.Registration> adaptRegistrations(Collection<ServiceInstance> serviceInstances) {
        List<ServiceDiscoveryOuter.Registration> registrations = new ArrayList<>(serviceInstances.size());
        for (ServiceInstance serviceInstance : serviceInstances) {
            registrations.add(adaptRegistration(serviceInstance));
        }
        return registrations;
    }

    public static ServiceDiscoveryOuter.Registration adaptRegistration(ServiceInstance serviceInstance) {
        return ServiceDiscoveryOuter.Registration.newBuilder()
                .setId(serviceInstance.getId())
                .setServiceName(serviceInstance.getServiceName())
                .setHost(serviceInstance.getHost())
                .setPort(serviceInstance.getPort())
                .putAllMetadata(serviceInstance.getMetadata())
                .build();
    }

    @Override
    public String interest() {
        return ServiceDiscoveryOuter.GetServiceInstancesRequest.class.getName();
    }
}
