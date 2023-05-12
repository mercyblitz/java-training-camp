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

import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.discovery.jraft.common.RequestContext;
import com.acme.middleware.rpc.service.discovery.jraft.common.RequestProcessor;
import com.acme.middleware.rpc.service.discovery.proto.ServiceDiscoveryOuter;
import com.alipay.sofa.jraft.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link ServiceDiscoveryOuter.GetServiceInstancesRequest} Processor
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class GetServiceInstancesRequestProcessor implements RequestProcessor<ServiceDiscoveryOuter.GetServiceInstancesRequest, ServiceDiscoveryOuter.GetServiceInstancesResponse> {

    private final ServiceDiscoveryRepository repository = ServiceDiscoveryRepository.getInstance();

    @Override
    public ServiceDiscoveryOuter.GetServiceInstancesResponse process(RequestContext<ServiceDiscoveryOuter.GetServiceInstancesRequest> requestContext, Status status) {
        ServiceDiscoveryOuter.GetServiceInstancesRequest request = requestContext.getData();
        String serviceName = request.getServiceName();
        Collection<ServiceInstance> serviceInstances = repository.getServiceInstances(serviceName);
        return response(serviceInstances);
    }

    private ServiceDiscoveryOuter.GetServiceInstancesResponse response(Collection<ServiceInstance> serviceInstances) {
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


}
