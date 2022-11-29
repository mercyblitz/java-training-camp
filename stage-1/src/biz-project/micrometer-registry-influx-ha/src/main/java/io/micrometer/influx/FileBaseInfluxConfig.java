package io.micrometer.influx;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 基于文件配置文件
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public interface FileBaseInfluxConfig extends InfluxConfig {

    String applicationName();

    default String storeDirectory() {
        Path path = Paths.get(System.getProperty("user.dir"), "metrics", "influx").normalize();
        return path.toString();
    }

    default InfluxConfig getNativeInfluxConfig() {
        return this;
    }

}
