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
package com.acme.biz.client.loadbalancer;

import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import com.acme.biz.client.loadbalancer.ribbon.UserServiceRibbonClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
@SpringBootApplication
@EnableFeignClients(clients = UserRegistrationService.class)
@RibbonClient(name = "user-service",configuration = UserServiceRibbonClientConfiguration.class)
@RestController
public class BizRibbonClientWebApplication {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @GetMapping("/user/register")
    public Object registerUser() {
        User user = new User();
        return userRegistrationService.registerUser(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(BizRibbonClientWebApplication.class, args);
    }
}
