package com.acme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Scanner;

/**
 * 推送任务
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class PublishTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PublishTask.class);
    private final Path workDir;
    private final Path ackPath;
    private final String applicationName;
    private final Publisher publisher;

    private final Path indexPath;
    private final Path dataPath;

    public PublishTask(Path workDir, Path ackPath, String applicationName, Publisher publisher) {
        this.workDir = workDir;
        this.ackPath = ackPath;
        this.applicationName = applicationName;
        this.publisher = publisher;

        this.indexPath = workDir.resolve(applicationName + ".index");
        this.dataPath = workDir.resolve(applicationName);
    }

    @Override
    public void run() {
        Path tempDataPath = this.workDir.resolve(applicationName + ".temp");
        Path tempIndexPath = this.workDir.resolve(applicationName + ".index-temp");
        InputStream dataStream = null;
        InputStream indexStream = null;
        try {
            int currentAct = readCurrentAck();
            //copy files
            copyFile(this.dataPath, tempDataPath);
            copyFile(this.indexPath, tempIndexPath);

            dataStream = Files.newInputStream(tempDataPath, StandardOpenOption.READ);
            indexStream = Files.newInputStream(tempIndexPath, StandardOpenOption.READ);

            Scanner scanner = new Scanner(indexStream);
            int start = 0;
            int endCount = 0;
            for (int i = 0; scanner.hasNext(); i++) {
                String sizeValue = scanner.next();
                int size = Integer.parseInt(sizeValue);
                if (i < currentAct) {
                    start += size;
                    endCount++;
                    continue;
                }

                String content = readContent(tempDataPath, start, size);
                this.publisher.publish(content, size);
                start += size;
                endCount++;
            }
            System.out.printf("读取文件结束, end offset is [%d].\n", endCount);

            //所有读完写ack文件
            if (!Files.exists(this.ackPath)) {
                Files.createFile(this.ackPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-")));
            }
            OutputStream ack = Files.newOutputStream(this.ackPath, StandardOpenOption.WRITE, StandardOpenOption.DSYNC, StandardOpenOption.SYNC);
            ack.write(String.valueOf(endCount).getBytes(StandardCharsets.UTF_8));
            ack.flush();
            ack.close();

        } catch (Exception e) {
            logger.error("error on publish", e);
        } finally {
            try {
                if (dataStream != null)
                    dataStream.close();
                if (indexStream != null)
                    indexStream.close();
                if (Files.exists(tempDataPath))
                    Files.delete(tempDataPath);
                if (Files.exists(tempIndexPath))
                    Files.delete(tempIndexPath);

            } catch (IOException ignored) {

            }
        }
    }

    private String readContent(Path dataPath, int start, int size) throws IOException {
        try (ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream(size);
             FileChannel source = FileChannel.open(dataPath, StandardOpenOption.READ)
        ) {
            source.transferTo(start, size, Channels.newChannel(bytesOutputStream));
            return new String(bytesOutputStream.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private int readCurrentAck() throws IOException {
        Scanner scanner = new Scanner(Files.newInputStream(this.ackPath, StandardOpenOption.READ));
        if (scanner.hasNext()) {
            String value = scanner.next();
            value = value.trim();
            scanner.close();
            if (value.isEmpty())
                return 0;
            return Integer.parseInt(value);
        }
        scanner.close();
        return 0;
    }


    private synchronized void copyFile(Path sourcePath, Path tempPath) throws IOException {
        if (Files.exists(tempPath)) {
            Files.delete(tempPath);
        }
        Files.createFile(tempPath, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw-rw-")));
        try (FileChannel source = FileChannel.open(sourcePath, StandardOpenOption.READ);
            FileChannel target = FileChannel.open(tempPath, StandardOpenOption.WRITE)
        ) {
            //zero copy
            source.transferTo(0, source.size(), target);
        }
    }
}
