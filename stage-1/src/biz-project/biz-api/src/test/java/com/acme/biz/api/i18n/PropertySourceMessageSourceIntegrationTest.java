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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.context.support.AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME;

/**
 * {@link PropertySourceMessageSource} Integration Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PropertySourceMessageSourceIntegrationTest.class)
public class PropertySourceMessageSourceIntegrationTest {

    @Primary
    @Bean(MESSAGE_SOURCE_BEAN_NAME)
    public static MessageSource messageSource(ConfigurableEnvironment environment) {
        return new PropertySourceMessageSource(environment);
    }

    @Autowired
    private MessageSource messageSource;

    @Test
    public void test() {
        String code = "my.name";
        Object[] args = new Object[0];
        assertEquals("小马哥", messageSource.getMessage(code, args, Locale.getDefault()));
        assertEquals("mercyblitz", messageSource.getMessage(code, args, Locale.ENGLISH));
        assertEquals("mercy blitz", messageSource.getMessage(code, args, Locale.US));
        assertEquals("default message", messageSource.getMessage("not.exist.code", args, "default message", Locale.US));
    }

}
