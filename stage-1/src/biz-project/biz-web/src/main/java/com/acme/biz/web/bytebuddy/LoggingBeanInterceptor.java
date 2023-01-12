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
package com.acme.biz.web.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.Pipe;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Bean ( @Service or @Controller)
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class LoggingBeanInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingBeanInterceptor.class);

    private final Object bean;

    public LoggingBeanInterceptor(Object bean) {
        this.bean = bean;
    }

    @RuntimeType
    public Object log(@Pipe Function<Object, Object> pipe, @AllArguments Object[] args, @Origin Method method)
            throws Exception {
        Arrays.stream(args).forEach(arg -> logger.info("{} 调用参数: {}", method, arg));
        try {
            return pipe.apply(bean);
        } finally {
        }
    }
}
