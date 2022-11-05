/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.biz.client.loadbalancer.ribbon.eureka;

import com.netflix.discovery.*;
import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.ServerListUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 基于 Eureka DiscoveryEvent 事件优化 Ribbon {@link ServerListUpdater}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see PollingServerListUpdater
 * @see CacheRefreshedEvent
 * @see StatusChangeEvent
 * @since 1.0.0
 */
public class EurekaDiscoveryEventServerListUpdater implements ServerListUpdater, EurekaEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EurekaDiscoveryEventServerListUpdater.class);

    private final EurekaClient eurekaClient;

    private UpdateAction updateAction;

    private volatile long timestamp;

    public EurekaDiscoveryEventServerListUpdater(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
        // 注册当前对象作为 EurekaEventListener
        eurekaClient.registerEventListener(this);
    }


    @Override
    public void start(UpdateAction updateAction) {
        logger.info("开始更新...");
        this.updateAction = updateAction;
    }

    @Override
    public void stop() {
        logger.info("停止更新...");
    }

    @Override
    public String getLastUpdate() {
        return new Date(timestamp).toString();
    }

    @Override
    public long getDurationSinceLastUpdateMs() {
        return 0;
    }

    @Override
    public int getNumberMissedCycles() {
        return 0;
    }

    @Override
    public int getCoreThreads() {
        return 0;
    }

    @Override
    public void onEvent(EurekaEvent event) {
        if (event instanceof DiscoveryEvent) { // 当 Eureka 客户端更新时会发送事件 - DiscoveryEvent
            this.timestamp = ((DiscoveryEvent) event).getTimestamp();
            // 利用 CacheRefreshedEvent 事件来触发
            updateAction.doUpdate();
        }
    }
}
