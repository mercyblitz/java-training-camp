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
package com.acme.biz.web.servlet.embedded.tomcat;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 动态 Tomcat 配置实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ConfigurationPropertiesRebinder
 * @see EnvironmentManager
 * @see ConfigurationPropertiesBinder
 * @since 1.0.0
 */
@Configuration
public class DynamicTomcatConfiguration implements TomcatProtocolHandlerCustomizer {

    @Deprecated
    private volatile ServerProperties originalServerProperties;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ConfigurableEnvironment environment;

    private Binder binder;

    @PostConstruct
    @Deprecated
    public void init() {
        originalServerProperties = new ServerProperties();
        BeanUtils.copyProperties(serverProperties, originalServerProperties);
    }

    @PostConstruct
    public void init2() {
        Iterable<ConfigurationPropertySource> configurationPropertySources = ConfigurationPropertySources.get(environment);
        binder = new Binder(configurationPropertySources);
        bindOriginalServerProperties();
    }

    @Deprecated
    private void buildOriginalServerProperties() {
        ServerProperties newServerProperties = new ServerProperties();
        BeanUtils.copyProperties(serverProperties, newServerProperties);
        // exchange
        this.originalServerProperties = newServerProperties;
    }

    private void bindOriginalServerProperties() {
        BindResult<ServerProperties> result = binder.bind("server", ServerProperties.class);
        ServerProperties newServerProperties = result.get();
        // exchange
        this.originalServerProperties = newServerProperties;
    }


    private AbstractProtocol protocol;

    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent(EnvironmentChangeEvent event) {

        // 需要排除非关注 keys
        // server.tomcat.*
        Set<String> keys = event.getKeys();
        // server.tomcat.threads.minSpare
        // server.tomcat.threads.max
        if (keys.contains("server.tomcat.threads.max")) {
            setMaxThreads();
        } else if (keys.contains("server.tomcat.threads.minSpare")) { // 不足：无法匹配 server.tomcat.threads.min-spare
            setMinSpareThreads();
        }
    }

    @EventListener(EnvironmentChangeEvent.class)
    public void onEnvironmentChangeEvent2(EnvironmentChangeEvent event) {
        // 不足：需要实时更新 originalServerProperties
        ServerProperties.Tomcat.Threads originalThreads = originalServerProperties.getTomcat().getThreads();
        ServerProperties.Tomcat.Threads threads = serverProperties.getTomcat().getThreads();
        if (originalThreads.getMinSpare() != threads.getMinSpare()) {
            setMinSpareThreads();
        }
    }

    // @EventListener(EnvironmentChangeEvent.class)
    public void updateOriginalServerProperties(EnvironmentChangeEvent event) {
        // 不足：无法确保是最后更新操作
        buildOriginalServerProperties();
    }

    private void setMaxThreads() {
        int maxThreads = serverProperties.getTomcat().getThreads().getMax();
        protocol.setMaxThreads(maxThreads);
    }


    private void setMinSpareThreads() {
        int minSpareThreads = serverProperties.getTomcat().getThreads().getMinSpare();
        protocol.setMinSpareThreads(minSpareThreads);
        bindOriginalServerProperties();
    }

    @Override
    public void customize(ProtocolHandler protocolHandler) {
        if (protocolHandler instanceof AbstractProtocol) {
            this.protocol = (AbstractProtocol) protocolHandler;
        }
    }
}
