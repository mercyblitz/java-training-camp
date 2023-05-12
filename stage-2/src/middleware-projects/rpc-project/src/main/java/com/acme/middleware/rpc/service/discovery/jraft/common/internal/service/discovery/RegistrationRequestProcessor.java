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
package com.acme.middleware.rpc.service.discovery.jraft.common.internal.service.discovery;

import com.acme.middleware.rpc.service.DefaultServiceInstance;
import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.discovery.jraft.common.RequestContext;
import com.acme.middleware.rpc.service.discovery.jraft.common.RequestProcessor;
import com.acme.middleware.rpc.service.discovery.proto.ServiceDiscoveryOuter;
import com.alipay.sofa.jraft.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class RegistrationRequestProcessor implements RequestProcessor<ServiceDiscoveryOuter.Registration, ServiceDiscoveryOuter.Response> {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationRequestProcessor.class);

    private final ServiceDiscoveryRepository repository = ServiceDiscoveryRepository.getInstance();

    @Override
    public ServiceDiscoveryOuter.Response process(RequestContext<ServiceDiscoveryOuter.Registration> requestContext,
                                                  Status status) {
        ServiceDiscoveryOuter.Registration registration = requestContext.getData();
        logger.info("Registration : {}", registration);
        ServiceInstance serviceInstance = adaptServiceInstance(registration);
        repository.register(serviceInstance);
        return response(status);
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
}
