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
package com.acme.middleware.rpc.service.discovery;

import com.acme.middleware.rpc.service.ServiceInstance;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.acme.middleware.rpc.util.ServiceLoaders.loadDefault;

/**
 * 服务发现与注册
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public interface ServiceDiscovery {

    ServiceDiscovery DEFAULT = loadDefault(ServiceDiscovery.class);

    void initialize(Map<String, Object> config);

    void register(ServiceInstance serviceInstance);

    void deregister(ServiceInstance serviceInstance);

    List<ServiceInstance> getServiceInstances(String serviceName);

    void close();

}
