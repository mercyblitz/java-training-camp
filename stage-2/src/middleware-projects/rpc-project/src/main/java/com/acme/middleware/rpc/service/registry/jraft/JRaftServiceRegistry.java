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
package com.acme.middleware.rpc.service.registry.jraft;

import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.registry.ServiceRegistry;
import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.rhea.StoreEngineHelper;
import com.alipay.sofa.jraft.rhea.options.StoreEngineOptions;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * SOFAJRATF {@link ServiceRegistry} 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class JRaftServiceRegistry implements ServiceRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(JRaftServiceRegistry.class);

    private final ServiceRegistrationServer serviceRegistrationServer;

    private final Executor readIndexExecutor;

    public JRaftServiceRegistry(ServiceRegistrationServer serviceRegistrationServer) {
        this.serviceRegistrationServer = serviceRegistrationServer;
        this.readIndexExecutor = createReadIndexExecutor();
    }

    private Executor createReadIndexExecutor() {
        final StoreEngineOptions opts = new StoreEngineOptions();
        return StoreEngineHelper.createReadIndexExecutor(opts.getReadIndexCoreThreads());
    }

    @Override
    public void initialize(Map<String, Object> config) {

    }

    @Override
    public void register(ServiceInstance serviceInstance) {
        ServiceInstanceRegistrationClosure closure = ServiceInstanceRegistrationClosure.get(serviceInstance);
        if (!isLeader()) {
            handlerNotLeaderError(closure);
            return;
        }
        try {
            final Task task = new Task();
            // 将待注册的服务实例序列化成 byte 数组
            // 写入到本地日志，将作为 AppendEntries RPC 请求的来源 -> Followers
            task.setData(ByteBuffer.wrap(SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(serviceInstance)));
            // 设置 ServiceInstanceRegistrationClosure
            // 触发 Leader 节点上的状态机 -> ServiceRegistrationStateMachine.onApply
            task.setDone(closure);
            this.serviceRegistrationServer.getNode().apply(task);
        } catch (CodecException e) {
            String errorMsg = "Fail to encode ServiceInstance";
            LOG.error(errorMsg, e);
            closure.failure(errorMsg, StringUtils.EMPTY);
            closure.run(new Status(RaftError.EINTERNAL, errorMsg));
        }
    }

    private boolean isLeader() {
        return this.serviceRegistrationServer.getFsm().isLeader();
    }

    private void handlerNotLeaderError(final ServiceInstanceRegistrationClosure closure) {
        closure.failure("Not leader.", getRedirect());
        closure.run(new Status(RaftError.EPERM, "Not leader"));
    }

    private String getRedirect() {
        return this.serviceRegistrationServer.getRedirect();
    }

    @Override
    public void deregister(ServiceInstance serviceInstance) {

    }

    @Override
    public List<ServiceInstance> getServiceInstances(String serviceName) {
        return null;
    }

    @Override
    public void close() {

    }
}
