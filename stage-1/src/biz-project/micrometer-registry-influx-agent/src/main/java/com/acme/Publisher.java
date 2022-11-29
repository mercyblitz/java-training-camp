package com.acme;

import io.micrometer.core.instrument.util.StringUtils;
import io.micrometer.core.ipc.http.HttpSender;
import io.micrometer.core.ipc.http.HttpUrlConnectionSender;
import io.micrometer.influx.InfluxApiVersion;
import io.micrometer.influx.InfluxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URLEncoder;

/**
 * 数据推送
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class Publisher {

    private final InfluxConfig config;
    private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private final HttpSender httpClient;
    private volatile boolean databaseExists = false;

    public Publisher(InfluxConfig config) {
        this.config = config;
        this.httpClient = new HttpUrlConnectionSender(config.connectTimeout(), config.readTimeout());
    }

    public void publish(String content, int size) {
        createDatabaseIfNecessary();

        try {

            String influxEndpoint = null;
            String tokenValue = null;
            if (InfluxApiVersion.V2.equals(config.apiVersion())) {
                tokenValue = "Token " + config.token();
                influxEndpoint = config.uri() + "/write?consistency=" + config.consistency().name().toLowerCase()
                        + "&precision=ms&db=" + config.db();
                if (StringUtils.isNotBlank(config.retentionPolicy())) {
                    influxEndpoint += "&rp=" + config.retentionPolicy();
                }
            } else if (InfluxApiVersion.V1.equals(config.apiVersion())) {
                tokenValue = "Bearer " + config.token();
                String bucket = URLEncoder.encode(config.bucket(), "UTF-8");
                String org = URLEncoder.encode(config.org(), "UTF-8");
                influxEndpoint = config.uri() + "/api/v2/write?precision=ms&bucket=" + bucket + "&org=" + org;
            }

            // begin send
            httpClient.post(influxEndpoint)
                    .withBasicAuthentication(config.userName(), config.password())
                    .withHeader("Authorization", tokenValue)
                    .withPlainText(content)
                    .compressWhen(config::compressed)
                    .send()
                    .onSuccess(response -> {
                        System.out.printf("successfully sent %d size metrics to InfluxDB.\n", size);
                        databaseExists = true;
                    })
                    .onError(response -> System.out.printf("failed to send metrics to influx: %s, response.body \n", response.body()));
            // @end send
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(
                    "Malformed InfluxDB publishing endpoint, see '" + config.prefix() + ".uri'", e);
        } catch (Throwable e) {
            System.err.println("failed to send metrics to influx");
            e.printStackTrace();
        }
    }

    private void createDatabaseIfNecessary() {
        if (!config.autoCreateDb() || databaseExists || config.apiVersion() == InfluxApiVersion.V2)
            return;

        try {
            String createDatabaseQuery = new CreateDatabaseQueryBuilder(config.db())
                    .setRetentionDuration(config.retentionDuration()).setRetentionPolicyName(config.retentionPolicy())
                    .setRetentionReplicationFactor(config.retentionReplicationFactor())
                    .setRetentionShardDuration(config.retentionShardDuration()).build();

            HttpSender.Request.Builder requestBuilder = httpClient
                    .post(config.uri() + "/query?q=" + URLEncoder.encode(createDatabaseQuery, "UTF-8"))
                    .withBasicAuthentication(config.userName(), config.password())
                    .withHeader("Authorization", "Token " + config.token());

            requestBuilder.send().onSuccess(response -> {
                logger.debug("influx database {} is ready to receive metrics", config.db());
                databaseExists = true;
            }).onError(response -> logger.error("unable to create database '{}': {}", config.db(), response.body()));
        }
        catch (Throwable e) {
            logger.error("unable to create database '{}'", config.db(), e);
        }
    }

}
