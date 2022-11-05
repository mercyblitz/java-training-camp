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
package com.acme.biz.client.loadbalancer.ribbon;

import com.acme.biz.client.loadbalancer.ribbon.eureka.EurekaDiscoveryEventServerListUpdater;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.ServerListUpdater;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link com.acme.biz.api.interfaces.UserService}
 * {@link RibbonClientConfiguration}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see RibbonClientConfiguration
 * @since 1.0.0
 */
@Configuration
public class UserServiceRibbonClientConfiguration {

    @Bean
    @ConditionalOnClass(EurekaClient.class)
    @ConditionalOnMissingBean
    public ServerListUpdater eurekaDiscoveryEventServerListUpdater(EurekaClient eurekaClient) {
        return new EurekaDiscoveryEventServerListUpdater(eurekaClient);
    }
}
