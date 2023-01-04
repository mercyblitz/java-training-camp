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
package com.acme.registry.eureka.env;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

/**
 * 替换 Java System Properties {@link EnvironmentPostProcessor} 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ReplacingJavaSystemPropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        String propertySourceName = SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

        PropertySource oldPropertySource = propertySources.get(propertySourceName);
        if (oldPropertySource instanceof PropertiesPropertySource) {
            // 如果 System#getProperties 未来变的话，替换后 PropertySource 无法感知
            Map properties = new HashMap<>(System.getProperties());
            propertySources.replace(propertySourceName, new MapPropertySource(propertySourceName, properties));
        }
    }
}
