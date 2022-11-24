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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;

/**
 * {@link RestTemplate} 配置类
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@Import(ErrorClientHttpRequestInterceptor.class)
public class RestTemplateConfiguration {

    @Bean
    public ClientHttpRequestInterceptor validatingClientHttpRequestInterceptor(Validator validator) {
        return new ValidatingClientHttpRequestInterceptor(validator, mappingJackson2HttpMessageConverter());
    }

    @Bean
    public RestTemplate restTemplate(List<ClientHttpRequestInterceptor> interceptors) {
        List<HttpMessageConverter<?>> converters = Arrays.asList(mappingJackson2HttpMessageConverter());
        // ClientHttpRequestInterceptor 排序
        AnnotationAwareOrderComparator.sort(interceptors);
        RestTemplate restTemplate = new RestTemplate(converters);
        ClientHttpRequestFactory requestFactory = buildClientHttpRequestFactory(interceptors);
        restTemplate.setRequestFactory(requestFactory);
        // TODO 增加 ResponseErrorHandler
        return restTemplate;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();
        httpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpMessageConverter;
    }

    private ClientHttpRequestFactory buildClientHttpRequestFactory(List<ClientHttpRequestInterceptor> interceptors) {
        // TODO 替换 SimpleClientHttpRequestFactory
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        return new InterceptingClientHttpRequestFactory(requestFactory, interceptors);
    }
}
