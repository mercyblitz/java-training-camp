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
package com.acme.biz.web.service;

import com.acme.biz.api.exception.UserException;
import com.acme.biz.api.interfaces.UserRegistrationService;
import com.acme.biz.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.CurrentTraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实现 UserRegistrationService
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Service("userRegistrationService")
public class InMemoryUserRegistrationService implements UserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryUserRegistrationService.class);

    private Map<Long, User> usersCache = new ConcurrentHashMap<>();

    @Autowired
    private Tracer tracer;

    @Autowired
    private CurrentTraceContext currentTraceContext;

    @Override
    public Boolean registerUser(User user) throws UserException {
        Long id = user.getId();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Span initialSpan = (Span) request.getAttribute(Span.class.getName());
        Span newSpan = null;
        Boolean success = false;
        try (Tracer.SpanInScope ws = this.tracer.withSpan(initialSpan)) {
            newSpan = this.tracer.nextSpan().name("userRegistrationService");
            success = usersCache.putIfAbsent(id, user) == null;
            logger.info("registerUser() == {}", success);
        } finally {
            if (newSpan != null) {
                newSpan.end();
            }
        }
        return success;
    }
}
