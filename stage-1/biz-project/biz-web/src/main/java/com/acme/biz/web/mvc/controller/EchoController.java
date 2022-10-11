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
package com.acme.biz.web.mvc.controller;

import com.acme.biz.api.ApiResponse;
import com.acme.biz.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
@RestController
@RequestMapping("/echo")
public class EchoController {

    @Value("${server.port:8080}")
    private Integer port;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/user")
    public ApiResponse<String> echo(@RequestBody User user) {
        return ApiResponse.ok(user.getName());
    }

    @GetMapping("/rest-template/{name}")
    public ApiResponse<String> restTemplateCall(@PathVariable String name) {
        String url = "http://127.0.0.1:{port}/echo/user";
        User user = new User();
        user.setName(name);
        ApiResponse response = restTemplate.postForObject(url, user, ApiResponse.class, port);
        return response;
    }
}
