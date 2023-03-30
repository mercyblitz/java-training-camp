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
package com.acme.middleware.zookeeper.service.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;

import java.time.Clock;
import java.util.Map;
import java.util.UUID;

/**
 * 服务发现与注册示例
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ServiceDiscoveryDemo {

    public static void main(String[] args) throws Throwable {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(new RetryForever(300))
                .build();

        // 启动客户端
        client.start();

        // client.create().forPath("/temp", "Hello,World".getBytes(StandardCharsets.US_ASCII));

        // 注册服务
        registerService(client);

        Thread.sleep(5000);

        // 关闭客户端（Session）
        client.close();
    }

    private static void registerService(CuratorFramework client) throws Exception {
        ServiceInstance instance = createInstance("demo-service");
        // 构建 ServiceDiscovery
        ServiceDiscovery serviceDiscovery = ServiceDiscoveryBuilder.builder(Map.class)
                .basePath("/demo-services")
                .client(client)
                .watchInstances(true)
                .build();
        // 启动 ServiceDiscovery
        serviceDiscovery.start();
        // 注册服务实例
        serviceDiscovery.registerService(instance);
//        try (
//        } // 自动关闭 ServiceDiscovery

    }

    private static ServiceInstance<Map<String, String>> createInstance(String serviceName) throws Exception {
        String id = UUID.randomUUID().toString();
        Clock clock = Clock.systemUTC();
        ServiceInstance<Map<String, String>> instance = ServiceInstance.<Map<String, String>>builder()
                .id(id)
                .name(serviceName)
                .enabled(true)
                .address("127.0.0.1")
                .port(8080)
                .serviceType(ServiceType.PERMANENT)
                .payload(Map.of("A", "1"))
                .registrationTimeUTC(clock.millis())
                .build();
        return instance;
    }
}

