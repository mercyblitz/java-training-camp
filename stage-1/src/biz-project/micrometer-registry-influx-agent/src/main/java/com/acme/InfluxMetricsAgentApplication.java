package com.acme;

import java.io.IOException;
import java.util.Properties;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class InfluxMetricsAgentApplication {

    public static void main(String[] args) throws IOException {
        final String applicationName = args[0];
        final String workDir = args[1];
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("influxdb.properties"));
        Bootstrap.create(applicationName, workDir, properties)
                .sync();
    }

}
