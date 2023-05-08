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
import com.acme.middleware.rpc.service.proto.ServiceDiscoveryOuter;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;

/**
 * {@link ServiceDiscoveryOuter.RegistrationRequest 服务实例注册请求}处理器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class RegistrationRequestRpcProcessor implements
        RpcProcessor<ServiceDiscoveryOuter.RegistrationRequest> {

    private final ServiceDiscoveryServer server;

    public RegistrationRequestRpcProcessor(ServiceDiscoveryServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(RpcContext rpcCtx, ServiceDiscoveryOuter.RegistrationRequest request) {

        ServiceInstance serviceInstance = adaptServiceInstance(request);

        ServiceDiscoveryOperation.Kind kind = ServiceDiscoveryOperation.Kind.REGISTRATION;

        ServiceDiscoveryOperation serviceDiscoveryOperation = new ServiceDiscoveryOperation(kind, serviceInstance);

        ServiceDiscoveryOperationClosure closure = new ServiceDiscoveryOperationClosure(serviceDiscoveryOperation, status -> {
            // RPC 响应到客户端
            rpcCtx.sendResponse(response(status));
        });

        if (!isLeader()) {
            handlerNotLeaderError(closure);
            return;
        }


        Task task = new Task();
        // 将待注册的服务实例序列化成 byte 数组
        // 写入到本地日志，将作为 AppendEntries RPC 请求的来源 -> Followers
        task.setData(serviceDiscoveryOperation.serialize());
        // 设置 ServiceInstanceRegistrationClosure
        // 触发 Leader 节点上的状态机 -> ServiceRegistrationStateMachine.onApply
        task.setDone(closure);
        // 提交任务
        this.server.getNode().apply(task);
    }

    private boolean isLeader() {
        return server.getFsm().isLeader();
    }

    private void handlerNotLeaderError(final Closure closure) {
        closure.run(new Status(RaftError.EPERM, "Not leader"));
    }


    private ServiceInstance adaptServiceInstance(ServiceDiscoveryOuter.RegistrationRequest request) {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setId(request.getId());
        instance.setServiceName(request.getServiceName());
        instance.setHost(request.getHost());
        instance.setPort(request.getPort());
        instance.setMetadata(request.getMetadataMap());
        return instance;
    }

    private ServiceDiscoveryOuter.Response response(Status status) {
        ServiceDiscoveryOuter.Response response = ServiceDiscoveryOuter.Response.newBuilder()
                .setCode(status.getCode())
                .setMessage(status.getErrorMsg())
                .build();
        return response;

    }

    @Override
    public String interest() {
        return ServiceDiscoveryOuter.RegistrationRequest.class.getName();
    }
}
