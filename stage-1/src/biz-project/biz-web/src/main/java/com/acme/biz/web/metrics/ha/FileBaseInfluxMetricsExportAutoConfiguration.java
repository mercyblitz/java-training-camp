package com.acme.biz.web.metrics.ha;

import io.micrometer.core.instrument.Clock;
import io.micrometer.influx.FileBaseInfluxMeterRegistry;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@EnableConfigurationProperties(FileBaseInfluxProperties.class)
public class FileBaseInfluxMetricsExportAutoConfiguration {

    private final FileBaseInfluxProperties properties;

    public FileBaseInfluxMetricsExportAutoConfiguration(FileBaseInfluxProperties properties) {
        this.properties = properties;
    }

    @Bean
    public InfluxMeterRegistry influxMeterRegistry(InfluxConfig influxConfig, Clock clock) {
        return new FileBaseInfluxMeterRegistry(properties.adapter(influxConfig), clock);
    }

}
