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
package com.acme.biz.client.cloud.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
public class CpuUsageLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public CpuUsageLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier serviceInstanceListSupplier = serviceInstanceListSupplierProvider.getIfAvailable();
        Flux<List<ServiceInstance>> flux = serviceInstanceListSupplier.get();
        List<ServiceInstance> serviceInstances = flux.blockFirst();
        for (ServiceInstance serviceInstance : serviceInstances) {
            Map<String, String> metadata = serviceInstance.getMetadata();
            String cpuUsage = metadata.get("cpu-usage");
            Integer usage = Integer.valueOf(cpuUsage);
            // TODO 完成 CPU 利用率的算法实现
        }
        return Mono.justOrEmpty(new DefaultResponse(serviceInstances.get(0)));
    }

//    /**
//     * 兼容老版本
//     *
//     * @param request
//     * @return
//     */
//    public Mono<org.springframework.cloud.client.loadbalancer.reactive.Response<ServiceInstance>> choose(
//            org.springframework.cloud.client.loadbalancer.reactive.Request request) {
//        ServiceInstanceListSupplier serviceInstanceListSupplier = serviceInstanceListSupplierProvider.getIfAvailable();
//        Flux<List<ServiceInstance>> flux = serviceInstanceListSupplier.get();
//        List<ServiceInstance> serviceInstances = flux.blockFirst();
//        return Mono.justOrEmpty(new org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse(serviceInstances.get(0)));
//    }
}
