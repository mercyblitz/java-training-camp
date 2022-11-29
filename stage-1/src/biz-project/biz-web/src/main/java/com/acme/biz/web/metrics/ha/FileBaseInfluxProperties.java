package com.acme.biz.web.metrics.ha;

import io.micrometer.influx.FileBaseInfluxConfig;
import io.micrometer.influx.FileBaseInfluxConfigAdapter;
import io.micrometer.influx.InfluxConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "management.metrics.export.influx.ha")
public class FileBaseInfluxProperties {

    private String workDir;

    @Value("${management.metrics.export.influx.ha.applicationName:${spring.application.name}}")
    private String applicationName;

    FileBaseInfluxConfig adapter(InfluxConfig influxConfig) {
        return new FileBaseInfluxConfigAdapter(getWorkDir(), getApplicationName(), influxConfig);
    }

    public String getWorkDir() {
        return workDir;
    }

    public void setWorkDir(String workDir) {
        this.workDir = workDir;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
