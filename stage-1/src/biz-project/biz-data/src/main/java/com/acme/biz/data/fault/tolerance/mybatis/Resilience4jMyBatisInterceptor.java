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
package com.acme.biz.data.fault.tolerance.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.util.List;
import java.util.Properties;

/**
 * Mybatis {@link Interceptor} resilience4j 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class Resilience4jMyBatisInterceptor implements Interceptor {

    private List<ExecutorDecorator> decorators;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 如果当前 Interceptor 采用 Interceptor#plugin 默认实现，即调用 Plugin.wrap(target, this)，当前方法会被执行
        // 如果当前 Interceptor plugin 方法实现采用静态拦截（Wrapper）的方式，那么，本方法不会被执行
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return decorateExecutor((Executor) target);
        }
        return Interceptor.super.plugin(target);
    }

    private Executor decorateExecutor(Executor target) {
        return new ExecutorDecorators(target, decorators);
    }

    /**
     * 外部注入
     *
     * @param decorators
     */
    public void setDecorators(List<ExecutorDecorator> decorators) {
        this.decorators = decorators;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
