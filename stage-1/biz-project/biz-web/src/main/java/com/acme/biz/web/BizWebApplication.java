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
package com.acme.biz.web;

import com.acme.biz.api.i18n.PropertySourceMessageSource;
import com.acme.biz.api.micrometer.binder.servo.ServoMetrics;
import com.acme.biz.web.i18n.LocalValidatorFactoryBeanPostProcessor;
import com.acme.biz.web.servlet.mvc.interceptor.ResourceBulkheadHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.context.support.AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME;

@SpringBootApplication
@ServletComponentScan
@Import(value = {
        ResourceBulkheadHandlerInterceptor.class,
        LocalValidatorFactoryBeanPostProcessor.class,
        ServoMetrics.class
})
@EnableDiscoveryClient // 激活服务发现客户端
@EnableScheduling
public class BizWebApplication implements WebMvcConfigurer {

    @Autowired
    private List<HandlerInterceptor> handlerInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        handlerInterceptors.forEach(registry::addInterceptor);
    }

    public static void main(String[] args) {
        SpringApplication.run(BizWebApplication.class, args);
    }

    @Primary
    @Bean(MESSAGE_SOURCE_BEAN_NAME)
    public static MessageSource messageSource(ConfigurableEnvironment environment) {
        return new PropertySourceMessageSource(environment);
    }
}

//@RequestMapping("/base")
//class BaseController {
//
//
//    @GetMapping("/echo")
//    public String echo() { // /base/echo
//
//    }
//
//}
//
//@RequestMapping("/default")
//class DefaultController extends BaseController { // /default/echo
//
//}

