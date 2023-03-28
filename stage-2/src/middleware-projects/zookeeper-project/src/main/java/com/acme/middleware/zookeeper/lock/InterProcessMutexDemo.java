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
package com.acme.middleware.zookeeper.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryForever;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@link InterProcessMutex} Demo
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class InterProcessMutexDemo {

    public static void main(String[] args) throws Throwable {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .retryPolicy(new RetryForever(300))
                .build();

        // 启动客户端
        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, "/demo-locks");

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                try {
                    System.out.printf("线程[name : %s] 尝试获取锁\n", Thread.currentThread().getName());
                    lock.acquire();
                    System.out.printf("线程[name : %s] 已获取锁\n", Thread.currentThread().getName());
                    Thread.sleep(TimeUnit.SECONDS.toMillis(3));



                } catch (Exception e) {
                    //
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                    }
                }
            });
        }

        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
            executorService.shutdown();
        }

        // 关闭客户端（Session）
        client.close();
    }
}
