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
package com.acme.biz.api.interfaces;

import com.acme.biz.api.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 用户服务接口（Open Feign、Dubbo 等公用）
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see UserLoginService
 * @see UserRegistrationService
 * @since
 * @deprecated 该接口不再推荐使用，请使用 {@link UserLoginService} 或 {@link UserRegistrationService}
 */
@FeignClient("${user.service.name}") // user // user-login user-registration
@RequestMapping("/user")
@Deprecated
public interface UserService {

    @PostMapping("/register")
    Boolean registerUser(User user);

    @PostMapping("/login")
    @Deprecated
    User login(Map<String, Object> context);
}
