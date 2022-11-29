package com.acme;

import io.micrometer.influx.InfluxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private final Path workPath;
    private final String applicationName;

    private Path ackPath;
    private final InfluxConfig config;

    private Publisher publisher;

    private Bootstrap(String applicationName, String workDir, InfluxConfig config) {
        this(applicationName, Paths.get(workDir), config);
    }

    private Bootstrap(String applicationName, Path workPath, InfluxConfig config) {
        Objects.requireNonNull(applicationName);
        Objects.requireNonNull(workPath);
        Objects.requireNonNull(config);
        this.applicationName = applicationName;
        this.workPath = workPath;
        this.config = config;
        if (!Files.exists(workPath)) {
            throw new RuntimeException("work dir is empty : " + workPath.toAbsolutePath());
        }

        initialize();
    }

    private void initialize() {
        //找寻ack文件
        String workDir = workPath.toAbsolutePath().toString();
        this.ackPath = Paths.get(workDir, applicationName + ".ack");
        if (!Files.exists(ackPath)) {
            try {
                Files.createFile(ackPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-")));
            } catch (IOException e) {
                logger.error("error on initialize index file : " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        //初始化publisher
        this.publisher = new Publisher(config);

    }

    private void run() {
        //创建推送任务
        PublishTask task = new PublishTask(this.workPath, this.ackPath, this.applicationName, this.publisher);
        task.run();
    }


    public void sync() {
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            this.run();
        }
    }

    public void async() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        final Bootstrap bootstrap = this;
        executorService.schedule(bootstrap::run, 10, TimeUnit.SECONDS);
    }


    public static Bootstrap create(String applicationName, String workDir, Properties properties) {
        return new Bootstrap(applicationName, workDir, new PropertiesInfluxConfig(properties));
    }
}
