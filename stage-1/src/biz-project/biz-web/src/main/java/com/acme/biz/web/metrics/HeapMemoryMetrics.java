package com.acme.biz.web.metrics;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 堆内存使用指标
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@Component
public class HeapMemoryMetrics implements MeterBinder, EnvironmentAware, InitializingBean, BeanFactoryAware {

    private String applicationName;
    private String profile;
    private String instanceId;

    private MeterRegistry meterRegistry;

    private Gauge headMemoryGauge;

    private MemoryMXBean memoryMXBean;

    private Tags getTags() {
        return Tags.of(
                Tag.of("applicationName", applicationName),
                Tag.of("profile", profile),
                Tag.of("instanceId", instanceId)
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
    }

    @Override
    public void setEnvironment(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length == 0)
            this.profile = "default";
        else this.profile = activeProfiles[0];
        this.applicationName = environment.getProperty("spring.application.name");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        Registration registration = beanFactory.getBean(Registration.class);
        this.instanceId = registration.getInstanceId();
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        this.meterRegistry = registry;
        this.headMemoryGauge = Gauge.builder("application.heap.memory", this.memoryMXBean, mBean -> {
            MemoryUsage heapMemoryUsage = mBean.getHeapMemoryUsage();
            long max = heapMemoryUsage.getMax();
            long used = heapMemoryUsage.getUsed();
            BigDecimal usage = new BigDecimal(used * 100.0);
            return usage.divide(new BigDecimal(max), 3, RoundingMode.UP).doubleValue();
        }).tags(getTags())
                .strongReference(true)
                .register(this.meterRegistry);
    }
}
