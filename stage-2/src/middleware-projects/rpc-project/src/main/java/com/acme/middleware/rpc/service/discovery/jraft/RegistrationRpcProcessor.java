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

import com.acme.middleware.rpc.service.DefaultServiceInstance;
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

import static com.acme.middleware.rpc.service.discovery.jraft.ServiceDiscoveryOperation.Kind.DEREGISTRATION;
import static com.acme.middleware.rpc.service.discovery.jraft.ServiceDiscoveryOperation.Kind.REGISTRATION;

/**
 * {@link ServiceDiscoveryOuter.Registration 服务实例注册请求}处理器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class RegistrationRpcProcessor implements RpcProcessor<ServiceDiscoveryOuter.Registration> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationRpcProcessor.class);

    private final ServiceDiscoveryServer server;

    public RegistrationRpcProcessor(ServiceDiscoveryServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(RpcContext rpcCtx, ServiceDiscoveryOuter.Registration registration) {

        ServiceInstance serviceInstance = adaptServiceInstance(registration);

        ServiceDiscoveryOperation.Kind kind = registration.getReversed() ? DEREGISTRATION : REGISTRATION;

        ServiceDiscoveryOperation operation = new ServiceDiscoveryOperation(kind, serviceInstance);

        ServiceDiscoveryOperationClosure closure = new ServiceDiscoveryOperationClosure(operation, (status, result) -> {
            if (!status.isOk()) {
                logger.warn("Closure status is : {}", status);
                return;
            }
            // RPC 响应到客户端
            rpcCtx.sendResponse(response(status));
            logger.info("Registration request has been handled , status : {}", status);
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

        logger.info("The task of '{}' has been applied , data : {}", operation.getKind(), operation.getData());

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


    public static ServiceInstance adaptServiceInstance(ServiceDiscoveryOuter.Registration registration) {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setId(registration.getId());
        instance.setServiceName(registration.getServiceName());
        instance.setHost(registration.getHost());
        instance.setPort(registration.getPort());
        instance.setMetadata(registration.getMetadataMap());
        return instance;
    }

    private ServiceDiscoveryOuter.Response response(Status status) {
        ServiceDiscoveryOuter.Response response = ServiceDiscoveryOuter.Response.newBuilder()
                .setCode(status.getCode())
                .setMessage(status.getErrorMsg() == null ? "" : status.getErrorMsg())
                .build();
        return response;

    }

    @Override
    public String interest() {
        return ServiceDiscoveryOuter.Registration.class.getName();
    }
}
