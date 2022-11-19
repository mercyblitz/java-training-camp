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
package com.acme.biz.api.micrometer.binder.servo;

import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.StringUtils;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.ToDoubleFunction;

/**
 * Netflix Servo {@link MeterBinder}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ServoMetrics implements MeterBinder, ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ServoMetrics.class);

    private static final String OBJECT_NAME_PATTERN = "com.netflix.servo:*";

    private MeterRegistry registry;

    private MBeanServer mBeanServer;

    private ClassLoader classLoader;

    private ConversionService conversionService;

    @Override
    public void bindTo(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) { // Spring Boot 完全启动后
        initClassLoader(event);
        initConversionService(event);

        registerServoMetrics(classLoader);
    }

    private void initClassLoader(ApplicationReadyEvent event) {
        this.classLoader = event.getApplicationContext().getClassLoader();
    }

    private void initConversionService(ApplicationReadyEvent event) {
        this.conversionService = event.getApplicationContext().getEnvironment().getConversionService();
    }

    private void registerServoMetrics(ClassLoader classLoader) {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = findServoMBeanObjectNames();
        for (ObjectName objectName : objectNames) {
            // registerServoMeter(objectName);
            registerServoMetrics(objectName);
        }
    }

    @Deprecated
    private void registerServoMeter(ObjectName objectName) {
        String type = objectName.getKeyProperty("type");

        try {
            MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);

            switch (type) {
                case "COUNTER":
                    registerServoCounterMeter(objectName, mBeanInfo);
                    break;
                case "GAUGE":
                    // registerServoMetrics(objectName, mBeanInfo);
                    break;
                default:
                    // TODO
                    break;
            }
        } catch (Throwable e) {
            logger.error("");
        }

    }

    @Deprecated
    private void registerServoCounterMeter(ObjectName objectName, MBeanInfo mBeanInfo) throws Throwable {
        String name = objectName.getKeyProperty("name");
        String className = objectName.getKeyProperty("class");

        MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
        for (MBeanAttributeInfo attribute : attributes) {
            String attributeName = attribute.getName();
            String counterName = buildMeterName(objectName, attributeName);
            // 构建 Counter
            FunctionCounter.builder(counterName, mBeanServer, mbs -> {
                        Double counterValue = null;
                        try {
                            Object attributeValue = mbs.getAttribute(objectName, attributeName);
                            // Counter -> double
                            // 'value' attribute -> java.lang.Number
                            counterValue = conversionService.convert(attributeValue, Double.class);
                        } catch (Throwable e) {
                        }
                        return counterValue;
                    })
                    .tags("name", name, "className", className)
                    .register(registry);
        }
    }

    private String buildMeterName(ObjectName objectName, String attributeName) {
        String type = objectName.getKeyProperty("type"); // type=COUNTER
        String name = objectName.getKeyProperty("name"); // name=success
        String className = objectName.getKeyProperty("class"); // class=TimedSupervisorTask
        String id = objectName.getKeyProperty("id"); // id=cacheRefresh

        // ${type}.${class}.${id}.${name}.${attributeName}
        StringJoiner joiner = new StringJoiner(".");

        appendIfPresent(joiner, type)
                .appendIfPresent(joiner, className)
                .appendIfPresent(joiner, id)
                .appendIfPresent(joiner, name)
                .appendIfPresent(joiner, attributeName)
        ;
        return joiner.toString();
    }

    private ServoMetrics appendIfPresent(StringJoiner joiner, String value) {
        if (StringUtils.hasText(value)) {
            joiner.add(value);
        }
        return this;
    }

    private void registerServoMetrics(ObjectName objectName) {
        try {
            MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);
            String type = objectName.getKeyProperty("type");
            String name = objectName.getKeyProperty("name");
            String className = objectName.getKeyProperty("class");

            MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
            for (MBeanAttributeInfo attribute : attributes) {
                String attributeName = attribute.getName();
                String meterName = buildMeterName(objectName, attributeName);
                // Meter 回调函数
                ToDoubleFunction<MBeanServer> toDoubleFunction = mbs -> {
                    Double value = null;
                    try {
                        Object attributeValue = mbs.getAttribute(objectName, attributeName);
                        // target type -> double
                        value = conversionService.convert(attributeValue, Double.class);
                    } catch (Throwable e) {
                    }
                    return value;
                };

                switch (type) {
                    case "COUNTER":
                        // 构建 FunctionCounter
                        FunctionCounter.builder(meterName, mBeanServer, toDoubleFunction)
                                .tags("name", name, "className", className)
                                .register(registry);
                        break;
                    case "GAUGE":
                        // 构建 Gauge
                        Gauge.builder(meterName, mBeanServer, toDoubleFunction)
                                .tags("name", name, "className", className)
                                .register(registry);
                        break;
                    case "NORMALIZED":

                        break;
                    default:
                        // TODO
                        break;
                }
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
    }

    private Set<ObjectName> findServoMBeanObjectNames() {
        Set<ObjectName> objectNames = Collections.emptySet();
        try {
            ObjectName objectName = new ObjectName(OBJECT_NAME_PATTERN);
            objectNames = mBeanServer.queryNames(objectName, objectName);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        }
        return objectNames;
    }
}
