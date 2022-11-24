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
package com.acme.biz.web.servlet.idempotent;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since
 */
public class IdempotentFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 使用 HttpSession Id -> cookie 来自于 headers
        String token = request.getParameter("token");

        // HttpSession 与 Redis 打通，利用 Spring Session
        // HttpSession#setAttribute 它底层利用 Redis Hash
        HttpSession httpSession = request.getSession(false);

        Object value = httpSession.getAttribute(token);

        if (value != null) {
            // 抛出异常
            throw new ServletException("幂等性校验错误");
        }

        // 设置状态
        httpSession.setAttribute(token, token);
        try {
            // 处理
            filterChain.doFilter(request, response);
        } finally {
            // 移除状态
            httpSession.removeAttribute(token);
        }

    }
}
