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

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * {@link ValueOperations} Wrapper
 * <p>
 * 实现拦截责任链
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ValueOperationsWrapper implements ValueOperations {

    private final ValueOperations delegate;

    private final ObjectProvider<RedisOperationInterceptor> interceptors; // 延迟依赖

    public ValueOperationsWrapper(ValueOperations delegate, ObjectProvider<RedisOperationInterceptor> interceptors) {
        this.delegate = delegate;
        this.interceptors = interceptors;
    }

    @Override
    public void set(Object key, Object value) {
        // 拦截该方法
        // 前置拦截
        iterable(interceptor -> interceptor.before(this, delegate, "set", new Object[]{key, value}));
        try {
            delegate.set(key, value);
            // 正常执行
            // 后置拦截（正常）
            iterable(interceptor -> interceptor.afterReturning(this, delegate, "set", new Object[]{key, value}, null));
        } catch (Throwable e) {
            // 异常执行
            // 通常情况：
            // 1. Redis 服务器无法访问
            // 2. Redis 连接池不够用（超时）
            // 后置拦截（异常）
            iterable(interceptor -> interceptor.afterThrowing(this, delegate, "set", new Object[]{key, value}, e));
        } finally {

        }

    }

    private void iterable(Consumer<RedisOperationInterceptor> interceptorConsumer) {
        // 有序迭代 + 延迟依赖查找
        interceptors.orderedStream().forEach(interceptorConsumer);
    }

    @Override
    public void set(Object key, Object value, long timeout, TimeUnit unit) {
        delegate.set(key, value, timeout, unit);
    }

    @Override
    public void set(Object key, Object value, Duration timeout) {
        delegate.set(key, value, timeout);
    }

    @Override
    @Nullable
    public Boolean setIfAbsent(Object key, Object value) {
        return delegate.setIfAbsent(key, value);
    }

    @Override
    @Nullable
    public Boolean setIfAbsent(Object key, Object value, long timeout, TimeUnit unit) {
        return delegate.setIfAbsent(key, value, timeout, unit);
    }

    @Override
    @Nullable
    public Boolean setIfAbsent(Object key, Object value, Duration timeout) {
        return delegate.setIfAbsent(key, value, timeout);
    }

    @Override
    @Nullable
    public Boolean setIfPresent(Object key, Object value) {
        return delegate.setIfPresent(key, value);
    }

    @Override
    @Nullable
    public Boolean setIfPresent(Object key, Object value, long timeout, TimeUnit unit) {
        return delegate.setIfPresent(key, value, timeout, unit);
    }

    @Override
    @Nullable
    public Boolean setIfPresent(Object key, Object value, Duration timeout) {
        return delegate.setIfPresent(key, value, timeout);
    }

    @Override
    public void multiSet(Map map) {
        delegate.multiSet(map);
    }

    @Override
    @Nullable
    public Boolean multiSetIfAbsent(Map map) {
        return delegate.multiSetIfAbsent(map);
    }

    @Override
    @Nullable
    public Object get(Object key) {
        return delegate.get(key);
    }

    @Override
    @Nullable
    public Object getAndDelete(Object key) {
        return delegate.getAndDelete(key);
    }

    @Override
    @Nullable
    public Object getAndExpire(Object key, long timeout, TimeUnit unit) {
        return delegate.getAndExpire(key, timeout, unit);
    }

    @Override
    @Nullable
    public Object getAndExpire(Object key, Duration timeout) {
        return delegate.getAndExpire(key, timeout);
    }

    @Override
    @Nullable
    public Object getAndPersist(Object key) {
        return delegate.getAndPersist(key);
    }

    @Override
    @Nullable
    public Object getAndSet(Object key, Object value) {
        return delegate.getAndSet(key, value);
    }

    @Override
    @Nullable
    public List multiGet(Collection keys) {
        return delegate.multiGet(keys);
    }

    @Override
    @Nullable
    public Long increment(Object key) {
        return delegate.increment(key);
    }

    @Override
    @Nullable
    public Long increment(Object key, long delta) {
        return delegate.increment(key, delta);
    }

    @Override
    @Nullable
    public Double increment(Object key, double delta) {
        return delegate.increment(key, delta);
    }

    @Override
    @Nullable
    public Long decrement(Object key) {
        return delegate.decrement(key);
    }

    @Override
    @Nullable
    public Long decrement(Object key, long delta) {
        return delegate.decrement(key, delta);
    }

    @Override
    @Nullable
    public Integer append(Object key, String value) {
        return delegate.append(key, value);
    }

    @Override
    @Nullable
    public String get(Object key, long start, long end) {
        return delegate.get(key, start, end);
    }

    @Override
    public void set(Object key, Object value, long offset) {
        delegate.set(key, value, offset);
    }

    @Override
    @Nullable
    public Long size(Object key) {
        return delegate.size(key);
    }

    @Override
    @Nullable
    public Boolean setBit(Object key, long offset, boolean value) {
        return delegate.setBit(key, offset, value);
    }

    @Override
    @Nullable
    public Boolean getBit(Object key, long offset) {
        return delegate.getBit(key, offset);
    }

    @Override
    @Nullable
    public List<Long> bitField(Object key, BitFieldSubCommands subCommands) {
        return delegate.bitField(key, subCommands);
    }

    @Override
    public RedisOperations getOperations() {
        return delegate.getOperations();
    }
}
