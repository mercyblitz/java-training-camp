package io.micrometer.influx;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.util.MeterPartition;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * 基于文件实现高可用版本{@link InfluxMeterRegistry}
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @see io.micrometer.influx.InfluxMeterRegistry
 * @since 1.0.0
 */
public class FileBaseInfluxMeterRegistry extends InfluxMeterRegistry {

    private static final Logger logger = LoggerFactory.getLogger(FileBaseInfluxMeterRegistry.class);
    protected FileBaseInfluxConfig config;

    private volatile boolean initialized = false;

    private Charset charset = StandardCharsets.UTF_8;

    private Path indexPath;
    private Path dataPath;


    public FileBaseInfluxMeterRegistry(FileBaseInfluxConfig config, Clock clock) {
        super(config.getNativeInfluxConfig(), clock);
        this.config = config;
        //初始化文件系统
        initializeInfluxFiles();
    }

    private void initializeInfluxFiles() {
        //初始化目录
        Path baseDir = Paths.get(config.storeDirectory());
        if (!Files.exists(baseDir)) {
            try {
                Files.createDirectories(baseDir);
            } catch (IOException e) {
                logger.error("error on initialize dir : " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        String workDir = baseDir.toAbsolutePath().toString();
        String applicationName = config.applicationName();
        //寻找index文件
        this.indexPath = Paths.get(workDir, applicationName + ".index");
        if (!Files.exists(indexPath)) {
            try {
                Files.createFile(indexPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-")));
            } catch (IOException e) {
                logger.error("error on initialize index file : " + e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        //寻找存储文件
        this.dataPath = Paths.get(workDir, applicationName);
        if (!Files.exists(dataPath)) {
            initializeDataDir(dataPath);
        }

        initialized = true;
    }

    protected void initializeDataDir(Path dataPath) {
        try {
            Files.createFile(dataPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-")));
        } catch (IOException e) {
            logger.error("error on initialize data file : " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private OutputStream getFileOutputStream(Path path) throws IOException {
        return Files.newOutputStream(path,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND,
                StandardOpenOption.SYNC,
                StandardOpenOption.DSYNC);

    }

    @Override
    protected synchronized void publish() {
        if (!initialized) {
            //初始化失败，不存储到文件系统，直接请求Influxdb
            super.publish();
            return;
        }

        publishInternal();
    }

    public void publishInternal() {
        try (OutputStream dataOutputStream = getFileOutputStream(this.dataPath);
             OutputStream indexOutputStream = getFileOutputStream(this.indexPath)
        ){
            //read metrics and sync to file
            for (List<Meter> batch : MeterPartition.partition(this, config.batchSize())) {
                String text = batch.stream()
                        .flatMap(m -> m.match(
                                gauge -> writeGauge(gauge.getId(), gauge.value()),
                                counter -> writeCounter(counter.getId(), counter.count()),
                                this::writeTimer,
                                this::writeSummary,
                                this::writeLongTaskTimer,
                                gauge -> writeGauge(gauge.getId(), gauge.value(getBaseTimeUnit())),
                                counter -> writeCounter(counter.getId(), counter.count()),
                                this::writeFunctionTimer,
                                this::writeMeter))
                        .collect(joining("\n"));
                //write to file
                if (!text.isEmpty()) {
                    logger.info("try write metrics to files");
                    syncToFile(text.getBytes(charset), dataOutputStream, indexOutputStream);
                    logger.info("write metrics to files, success");
                }

            }

        } catch (Throwable e) {
            logger.error("failed to send metrics to influx", e);
        }
    }

    protected void syncToFile(byte[] contents, OutputStream fileOutputStream, OutputStream indexOutStream) throws IOException {
        int length = contents.length;
        //append data file
        fileOutputStream.write(contents);
        fileOutputStream.flush();

        //append index file
        byte[] indexContents = String.format("%d\n", contents.length).getBytes(StandardCharsets.UTF_8);
        indexOutStream.write(indexContents);
        indexOutStream.flush();
    }

    /**
     * copy from {@link InfluxMeterRegistry}
     */

    private Stream<String> writeLongTaskTimer(LongTaskTimer timer) {
        Stream<Field> fields = Stream.of(new Field("active_tasks", timer.activeTasks()),
                new Field("duration", timer.duration(getBaseTimeUnit())));
        return Stream.of(influxLineProtocol(timer.getId(), "long_task_timer", fields));
    }

    private Stream<String> writeTimer(Timer timer) {
        final Stream<Field> fields = Stream.of(new Field("sum", timer.totalTime(getBaseTimeUnit())),
                new Field("count", timer.count()), new Field("mean", timer.mean(getBaseTimeUnit())),
                new Field("upper", timer.max(getBaseTimeUnit())));

        return Stream.of(influxLineProtocol(timer.getId(), "histogram", fields));
    }

    private Stream<String> writeSummary(DistributionSummary summary) {
        final Stream<Field> fields = Stream.of(new Field("sum", summary.totalAmount()),
                new Field("count", summary.count()), new Field("mean", summary.mean()),
                new Field("upper", summary.max()));

        return Stream.of(influxLineProtocol(summary.getId(), "histogram", fields));
    }

    private String influxLineProtocol(Meter.Id id, String metricType, Stream<Field> fields) {
        String tags = getConventionTags(id).stream().filter(t -> StringUtils.isNotBlank(t.getValue()))
                .map(t -> "," + t.getKey() + "=" + t.getValue()).collect(joining(""));

        return getConventionName(id) + tags + ",metric_type=" + metricType + " "
                + fields.map(Field::toString).collect(joining(",")) + " " + clock.wallTime();
    }
}
