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
import com.acme.middleware.rpc.service.discovery.jraft.common.JRaftStateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServiceDiscoveryRepository
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ServiceDiscoveryRepository {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryRepository.class);

    private static ServiceDiscoveryRepository instance = new ServiceDiscoveryRepository();

    private final Map<String, Map<String, ServiceInstance>> serviceNameToInstancesStorage = new ConcurrentHashMap<>();

    private JRaftStateMachine fsm;

    public void setFsm(JRaftStateMachine fsm) {
        this.fsm = fsm;
    }

    public void register(ServiceInstance serviceInstance) {
        String serviceName = serviceInstance.getServiceName();
        String id = serviceInstance.getId();

        synchronized (serviceNameToInstancesStorage) {
            Map<String, ServiceInstance> serviceInstancesMap = serviceNameToInstancesStorage.computeIfAbsent(serviceName,
                    n -> new LinkedHashMap<>());
            serviceInstancesMap.put(id, serviceInstance);
        }

        logger.info("{} has been registered", serviceInstance);
    }

    public void deregister(ServiceInstance serviceInstance) {
        String serviceName = serviceInstance.getServiceName();
        String id = serviceInstance.getId();

        synchronized (serviceNameToInstancesStorage) {
            Map<String, ServiceInstance> serviceInstancesMap = getServiceInstancesMap(serviceName);
            serviceInstancesMap.remove(id);
        }

        logger.info("{} has been deregistered", serviceInstance);
    }

    public Collection<ServiceInstance> getServiceInstances(String serviceName) {
        Map<String, ServiceInstance> serviceInstancesMap = getServiceInstancesMap(serviceName);
        return serviceInstancesMap.values();
    }

    private boolean isLeader() {
        return fsm.isLeader();
    }

    private Map<String, ServiceInstance> getServiceInstancesMap(String serviceName) {
        return serviceNameToInstancesStorage.computeIfAbsent(serviceName, n -> new LinkedHashMap<>());
    }

    public static ServiceDiscoveryRepository getInstance() {
        return instance;
    }
}
