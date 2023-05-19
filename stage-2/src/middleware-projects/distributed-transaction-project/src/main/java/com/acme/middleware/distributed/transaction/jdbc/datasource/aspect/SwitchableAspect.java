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
package com.acme.middleware.distributed.transaction.jdbc.datasource.aspect;

import com.acme.middleware.distributed.transaction.jdbc.datasource.annotation.Switchable;
import com.acme.middleware.distributed.transaction.jdbc.datasource.util.DataSourceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * {@link Switchable} {@link Aspect}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Aspect
public class SwitchableAspect {

    @Pointcut("execution(* com.acme.middleware.distributed.transaction.service..*.*(..)) " +
            "& @target(com.acme.middleware.distributed.transaction.jdbc.datasource.annotation.Switchable)")
    private void switchable() {
    }

    @Around("switchable()")
    public Object switchDataSource(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Switchable switchable = method.getAnnotation(Switchable.class);
        Object result = null;
        try {
            if (switchable != null) {
                DataSourceType dataSourceType = switchable.dataSource();
                dataSourceType.switchDataSource();
            }
            result = pjp.proceed(pjp.getArgs());
        } finally {
            if (switchable != null) {
                DataSourceType.resetDataSource();
            }
        }
        return result;
    }
}
