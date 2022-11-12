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
package com.acme.biz.api.i18n;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Properties;

/**
 * 基于 PropertySources 实现 {@link MessageSource}
 * <p>
 * 通过 ResourcePatternResolver 获取 Locale 与 Resource 映射关系
 * 解析 YAML Resource 变成 Spring PropertySource
 * 通过 Locale 获取 PropertySource
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class PropertySourceMessageSource implements MessageSource, InitializingBean {

    /**
     *
     */
    private static final String MESSAGES_RESOURCE_PATTERN = "classpath*:/META-INF/Messages*.yaml";

    /**
     * {} : 如果 Locale 等默认 Locale 的话，{} 为 ""，
     * 否则 {} 为 Locale#toString()
     */
    private static final String RESOURCE_NAME_PATTERN = "classpath:/META-INF/Messages{}.yaml";

    private static final String PROPERTY_SOURCE_NAME_PREFIX = "Messages_";

    private static final String CODE_PREFIX = "messages.";

    private final MutablePropertySources propertySources;

    public PropertySourceMessageSource(ConfigurableEnvironment environment) {
        this.propertySources = environment.getPropertySources();
    }

    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        String messagePattern = getMessagePattern(code, locale);
        String message = format(messagePattern, args);
        return message == null ? defaultMessage : message;
    }

    private String getMessagePattern(String code, Locale locale) {
        String propertyName = CODE_PREFIX + code;
        String propertySourceName = buildPropertySourceName(locale);
        PropertySource propertySource = propertySources.get(propertySourceName);
        return propertySource == null ? null : (String) propertySource.getProperty(propertyName);
    }

    private String buildPropertySourceName(Locale locale) {
        return (PROPERTY_SOURCE_NAME_PREFIX + locale).toLowerCase();
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        return getMessage(code, args, null, locale);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        String message = null;
        Object[] args = resolvable.getArguments();
        String defaultMessage = resolvable.getDefaultMessage();
        for (String code : resolvable.getCodes()) {
            message = getMessage(code, args, defaultMessage, locale);
            if (message != null) {
                break;
            }
        }
        return message;
    }

    /**
     * @param messagePattern
     * @param args
     * @return
     * @see MessageFormatter#format(String, Object)
     */
    public static String format(String messagePattern, Object... args) {
        if (messagePattern == null) {
            return messagePattern;
        }
        return MessageFormatter.arrayFormat(messagePattern, args).getMessage();
    }

    protected static String getResourceName(Locale locale) {
        String suffix = Locale.getDefault().equals(locale) ? "" : "_" + locale.toString();
        return format(RESOURCE_NAME_PATTERN, suffix);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getResourceName(Locale.getDefault()));
        System.out.println(getResourceName(Locale.ENGLISH));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(MESSAGES_RESOURCE_PATTERN);
        String prefix = "/META-INF/Messages";
        String suffix = ".yaml";
        Locale locale = null;

        // 通过资源它们对应的 Locale
        for (Resource resource : resources) {
            URI uri = resource.getURI();
            String path = uri.getPath();
            String localeString = path.substring(path.indexOf(prefix) + prefix.length());
            if (localeString.startsWith(".")) { // Default Locale
                locale = Locale.getDefault();
            } else if (localeString.startsWith("_")) {
                localeString = localeString.substring(1, localeString.length() - suffix.length());
                locale = new Locale(localeString);
            }
            // 构建 Locale 与 Resource 对应 YAML PropertySource
            PropertySource propertySource = buildPropertySource(locale, resource);
            // 添加 Locale PropertySource 到 PropertySources 中
            propertySources.addLast(propertySource);
        }
    }

    private PropertySource buildPropertySource(Locale locale, Resource resource) {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        // 关联 YAML Resource
        yamlPropertiesFactoryBean.setResources(resource);
        // 初始化
        yamlPropertiesFactoryBean.afterPropertiesSet();
        // 将 YAML 资源转化成 Properties
        Properties properties = yamlPropertiesFactoryBean.getObject();
        String propertySourceName = buildPropertySourceName(locale);
        return new PropertiesPropertySource(propertySourceName, properties);
    }
}
