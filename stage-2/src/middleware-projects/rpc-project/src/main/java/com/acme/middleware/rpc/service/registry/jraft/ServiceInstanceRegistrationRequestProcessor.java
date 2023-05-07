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

import com.acme.middleware.rpc.service.DefaultServiceInstance;
import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.proto.ServiceDiscoveryOutter;
import com.acme.middleware.rpc.service.registry.ServiceRegistry;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;

/**
 * {@link ServiceDiscoveryOutter.ServiceInstanceRegistrationRequest 服务实例注册请求}处理器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ServiceInstanceRegistrationRequestProcessor implements
        RpcProcessor<ServiceDiscoveryOutter.ServiceInstanceRegistrationRequest> {

    private final ServiceRegistry serviceRegistry;

    public ServiceInstanceRegistrationRequestProcessor(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void handleRequest(RpcContext rpcCtx, ServiceDiscoveryOutter.ServiceInstanceRegistrationRequest request) {
        ServiceInstance serviceInstance = adaptServiceInstance(request);
        new ServiceInstanceRegistrationClosure(serviceInstance, () -> {
            // RPC 响应到客户端
            rpcCtx.sendResponse(createServiceInstanceRegistrationResponse());
        });

        serviceRegistry.register(serviceInstance);
    }

    private ServiceInstance adaptServiceInstance(ServiceDiscoveryOutter.ServiceInstanceRegistrationRequest request) {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setId(request.getId());
        instance.setServiceName(request.getServiceName());
        instance.setHost(request.getHost());
        instance.setPort(request.getPort());
        instance.setMetadata(request.getMetadataMap());
        return instance;
    }

    private ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse createServiceInstanceRegistrationResponse() {
        ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse response =
                ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse.newBuilder()
                        .setCode(200)
                        .setMessage("OK")
                        .build();
        return response;

    }

    @Override
    public String interest() {
        return null;
    }
}
