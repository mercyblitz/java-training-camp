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
package com.acme.biz.web.data.bean.mircometer;

import com.acme.biz.web.data.bean.RedisOperationInterceptor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 操作 Metrics 拦截器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Component
public class RedisOperationMetricsInterceptor implements RedisOperationInterceptor, MeterBinder {

    private MeterRegistry registry;

    private ThreadLocal<Map<String, Long>> startTimeThreadLocal = ThreadLocal.withInitial(HashMap::new);

    @Override
    public void before(Object wrapper, Object delegate, String methodName, Object[] args) {
        String meterName = createMeterName(methodName, args);
        Map<String, Long> startTimeMap = startTimeThreadLocal.get();
        startTimeMap.put(meterName, System.nanoTime());
    }

    @Override
    public void after(Object wrapper, Object delegate, String methodName, Object[] args, Object result, Throwable failure) {
        String meterName = createMeterName(methodName, args);
        recordTimer(meterName);
        count(meterName);
    }

    private void recordTimer(String meterName) {
        Map<String, Long> startTimeMap = startTimeThreadLocal.get();
        Timer timer = Timer.builder("Timer-" + meterName)
                .register(registry);
        long amount = System.nanoTime() - startTimeMap.get(meterName);
        timer.record(amount, TimeUnit.NANOSECONDS);
    }

    private void count(String meterName) {
        Counter counter = Counter.builder("Counter-" +meterName)
                .register(registry);
        counter.increment();
    }

    private String createMeterName(String methodName, Object... args) {
        return "redis-ops-" + methodName;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

}
