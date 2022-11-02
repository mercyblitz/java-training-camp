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
package com.acme.biz.web.discovery.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@ConditionalOnClass(name = {
        "org.springframework.cloud.client.discovery.DiscoveryClient",
        "org.springframework.cloud.client.serviceregistry.ServiceRegistry"
})
@Configuration(proxyBeanMethods = false)
public class MetadataUploadConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MetadataUploadConfiguration.class);

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Autowired
    private Registration registration; // 当前服务实例注册对象

    @Scheduled(fixedRate = 5000L, initialDelay = 10L)
    public void upload() {
        Map<String, String> metadata = registration.getMetadata();
        metadata.put("timestamp", String.valueOf(System.currentTimeMillis()));
        serviceRegistry.deregister(registration);
        serviceRegistry.register(registration);
        logger.info("Upload Registration's metadata");
    }

}
