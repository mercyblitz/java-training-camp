package io.micrometer.influx;

import io.micrometer.core.instrument.Clock;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class BaseFileTest {

    public static void main(String[] args) {
        System.setProperty("user.dir", "/Users/kuroky/metrics");
        FileBaseInfluxConfig config = new FileBaseInfluxConfig() {
            @Override
            public String applicationName() {
                return "test-service";
            }

            @Override
            public String get(String key) {
                return null;
            }
        };

        FileBaseInfluxMeterRegistry meterRegistry = new FileBaseInfluxMeterRegistry(config, Clock.SYSTEM);
        meterRegistry.publishInternal();
    }

}
