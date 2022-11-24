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
package com.acme.biz.web.client.rest;

import com.acme.biz.api.model.User;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Bean Validation 校验 {@link ClientHttpRequestInterceptor} 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ValidatingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor , Ordered {

    private final Validator validator;

    private final HttpMessageConverter[] converters;

    public ValidatingClientHttpRequestInterceptor(Validator validator, HttpMessageConverter... converters) {
        this.validator = validator;
        this.converters = converters;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = null;
        // 前置处理
        boolean valid = beforeExecute(request, body);
        HttpHeaders headers = request.getHeaders();
        headers.add("validation-result", Boolean.toString(valid));
        // 请求处理 (next interceptor)
        response = execution.execute(request, body);
        // 后置处理
        return afterExecute(response);
    }

    private ClientHttpResponse handleError(HttpRequest request, byte[] body) {
        return null;
    }

    private boolean beforeExecute(HttpRequest request, byte[] body) {
        return validateBean(request, body);
    }

    private boolean validateBean(HttpRequest request, byte[] body) {
        // FastJSON auto-type
        Class<?> bodyClass = resolveBodyClass(request.getHeaders());
        if (bodyClass != null) {
            HttpInputMessage httpInputMessage = new MappingJacksonInputMessage(new ByteArrayInputStream(body), request.getHeaders());
            MediaType mediaType = resolveMediaType(httpInputMessage);
            for (HttpMessageConverter converter : converters) {
                if (converter.canRead(bodyClass, mediaType)) {
                    try {
                        Object bean = converter.read(bodyClass, httpInputMessage);
                        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
                        if (!violations.isEmpty()) {
                            return false;
                        }
                        // TODO
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return true;
    }

    private Class<?> resolveBodyClass(HttpHeaders httpHeaders) {
        // 临时传递 HTTP Header
        List<String> classes = httpHeaders.remove("body-class");
        if (!ObjectUtils.isEmpty(classes)) {
            String bodyClassName = classes.get(0);
            if (StringUtils.hasText(bodyClassName)) {
                return ClassUtils.resolveClassName(bodyClassName, null);
            }
        }
        return User.class;
    }


    private MediaType resolveMediaType(HttpInputMessage httpInputMessage) {
        HttpHeaders httpHeaders = httpInputMessage.getHeaders();
        return httpHeaders.getContentType();
    }

    private ClientHttpResponse afterExecute(ClientHttpResponse response) {
        // TODO
        return response;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
