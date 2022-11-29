package com.acme;

import io.micrometer.influx.InfluxConfig;

import java.util.Properties;

/**
 * {@link java.util.Properties} implements for {@link io.micrometer.influx.InfluxConfig}
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class PropertiesInfluxConfig implements InfluxConfig {

    private final Properties properties;

    public PropertiesInfluxConfig(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String get(String key) {
        return this.properties.getProperty(key);
    }
}
