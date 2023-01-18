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
package com.acme.biz.web.data.bean;

import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;

/**
 * Redis 操作拦截器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public interface RedisOperationInterceptor extends Ordered {

    void before(Object wrapper, Object delegate, String methodName, Object[] args);

    void after(Object wrapper, Object delegate, String methodName, Object[] args,
               @Nullable Object result,
               @Nullable Throwable failure);

    default void afterReturning(Object wrapper, Object delegate, String methodName, Object[] args, Object result) {
        after(wrapper, delegate, methodName, args, result, null);
    }

    default void afterThrowing(Object wrapper, Object delegate, String methodName, Object[] args, Throwable failure) {
        after(wrapper, delegate, methodName, args, null, failure);
    }

    @Override
    default int getOrder() {
        return 0;
    }

    @Override
    boolean equals(Object object);

    @Override
    int hashCode();

}
