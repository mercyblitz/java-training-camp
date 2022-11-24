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
package com.acme.biz.client.cloud;

import com.acme.biz.api.feign.config.DefaultFeignClientsConfiguration;
import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.micrometer.MicrometerConfiguration;
import com.acme.biz.api.micrometer.binder.feign.FeignCallCounterMetrics;
import com.acme.biz.api.model.User;
import com.acme.biz.client.cloud.loadbalancer.CpuUsageBalancerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * biz-client 应用启动类
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableFeignClients(clients = UserRegistrationService.class, defaultConfiguration = DefaultFeignClientsConfiguration.class)
@LoadBalancerClient(name = "user-service", configuration = CpuUsageBalancerConfiguration.class)
@EnableScheduling
@Import({MicrometerConfiguration.class, FeignCallCounterMetrics.class})
public class BizClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(BizClientApplication.class, args);
    }

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Scheduled(fixedRate = 10 * 1000L)
    public void registerUser() {
        User user = new User();
        user.setId(1L);
        user.setName("ABC");
        System.out.println(userRegistrationService.registerUser(user));
    }
}
