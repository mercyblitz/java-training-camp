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
import com.acme.middleware.rpc.service.proto.ServiceDiscoveryOutter;
import com.alipay.sofa.jraft.Status;

import java.util.Map;

public class ServiceInstanceRegistrationClosure extends ServiceOperationClosure {

    private final static String METADATA_KEY = "_ServiceInstanceRegistrationClosure_";

    private final ServiceInstance serviceInstance;

    private final Runnable callback;

    private ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse registrationResponse;

    public ServiceInstanceRegistrationClosure(ServiceInstance serviceInstance, Runnable callback) {
        super(new ServiceOperation(ServiceOperation.Kind.REGISTRATION, serviceInstance));
        this.serviceInstance = serviceInstance;
        this.callback = callback;
        Map metadata = serviceInstance.getMetadata();
        metadata.put(METADATA_KEY, serviceInstance);
    }

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse getRegistrationResponse() {
        return registrationResponse;
    }

    public void setRegistrationResponse(ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse registrationResponse) {
        this.registrationResponse = registrationResponse;
    }

    protected void failure(final String errorMsg, final String redirect) {
        final ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse response = ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse
                .newBuilder().setCode(500).setMessage(errorMsg).build();
        setRegistrationResponse(response);
    }

    protected void success() {
        final ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse response = ServiceDiscoveryOutter.ServiceInstanceRegistrationResponse
                .newBuilder().setCode(200).setMessage("OK").build();
        setRegistrationResponse(response);
    }

    @Override
    public void run(Status status) {
        if (status.isOk()) {
            callback.run();
        }
    }

    public static ServiceInstanceRegistrationClosure get(ServiceInstance serviceInstance) {
        Map metadata = serviceInstance.getMetadata();
        return (ServiceInstanceRegistrationClosure) metadata.get(METADATA_KEY);
    }
}
