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
package com.acme.biz.api.micrometer.binder.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

import static com.acme.biz.api.micrometer.Micrometers.async;

/**
 * Feign 调用计数 Metrics
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class FeignCallCounterMetrics implements RequestInterceptor, MeterBinder {


    private static MeterRegistry meterRegistry;

    private static Counter totalCounter;

    @Override
    public void apply(RequestTemplate template) { // FeignClient 子上下文调用
        // 异步执行
        async(() -> {
            // 方法统计
            String feignMethod = template.methodMetadata().configKey();
            Counter counter = Counter.builder("feign.calls")
                    .tags("method", feignMethod)                   // Feign 调用方法（接口 + 方法） Tag
                    .register(meterRegistry);
            counter.increment();
            // 全局统计
            totalCounter.increment();
        });

    }

    @Override
    public void bindTo(MeterRegistry registry) { // Spring Boot 主上下文调用
        this.meterRegistry = registry;
        this.totalCounter = Counter.builder("feign.total-calls")
                .register(registry);
    }
}
