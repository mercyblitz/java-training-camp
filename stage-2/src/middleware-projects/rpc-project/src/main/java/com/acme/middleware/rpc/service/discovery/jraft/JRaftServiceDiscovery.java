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
import com.acme.middleware.rpc.service.discovery.ServiceDiscovery;
import com.acme.middleware.rpc.service.proto.ServiceDiscoveryOuter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * SOFAJRATF {@link ServiceDiscovery} 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class JRaftServiceDiscovery implements ServiceDiscovery {

    public static final String GROUP_ID_PROPERTY_NAME = "service.discovery.jraft.registry.group-id";

    public static final String DEFAULT_GROUP_ID_PROPERTY_VALUE = "service-discovery";

    public static final String REGISTRY_ADDRESS_PROPERTY_NAME = "service.discovery.jraft.registry.address";

    private static final Logger LOG = LoggerFactory.getLogger(JRaftServiceDiscovery.class);


    private ServiceDiscoveryClient client;

    @Override
    public void initialize(Map<String, Object> config) {
        String groupId = (String) config.getOrDefault(GROUP_ID_PROPERTY_NAME, DEFAULT_GROUP_ID_PROPERTY_VALUE);
        String registryAddress = (String) config.get(REGISTRY_ADDRESS_PROPERTY_NAME);

        ServiceDiscoveryClient client = new ServiceDiscoveryClient();
        client.setGroupId(groupId);
        client.setRegistryAddress(registryAddress);
        client.init();
        this.client = client;
    }

    @Override
    public void register(ServiceInstance serviceInstance) {
        // 调用 RPC
        ServiceDiscoveryOuter.RegistrationRequest request = buildRegistrationRequest(serviceInstance);
        try {
            client.invoke(request);
        } catch (Throwable e) {
            LOG.error("Fail to register a service instance : " + serviceInstance, e);
        }
    }

    private ServiceDiscoveryOuter.RegistrationRequest buildRegistrationRequest(ServiceInstance serviceInstance) {
        return ServiceDiscoveryOuter.RegistrationRequest.newBuilder()
                .setId(serviceInstance.getId())
                .setServiceName(serviceInstance.getServiceName())
                .setHost(serviceInstance.getHost())
                .setPort(serviceInstance.getPort())
                .putAllMetadata(serviceInstance.getMetadata())
                .build();
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
