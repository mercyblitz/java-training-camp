package io.micrometer.influx;

import java.util.Objects;

/**
 * adapter with {@link FileBaseInfluxConfig} and {@link InfluxConfig}
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class FileBaseInfluxConfigAdapter implements FileBaseInfluxConfig {

    private final InfluxConfig delegate;

    private final String workDir;
    private final String applicationName;

    public FileBaseInfluxConfigAdapter(String workDir, String applicationName, InfluxConfig delegate) {
        Objects.requireNonNull(workDir);
        Objects.requireNonNull(applicationName);
        Objects.requireNonNull(delegate);
        this.delegate = delegate;
        this.workDir = workDir;
        this.applicationName = applicationName;
    }

    @Override
    public String storeDirectory() {
        if (workDir.isEmpty())
            return FileBaseInfluxConfig.super.storeDirectory();

        return workDir;
    }

    @Override
    public String get(String key) {
        return this.delegate.get(key);
    }

    @Override
    public String applicationName() {
        return this.applicationName;
    }

    @Override
    public InfluxConfig getNativeInfluxConfig() {
        return this.delegate;
    }
}
