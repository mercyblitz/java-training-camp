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
package com.acme.middleware.distributed.transaction.sample.base;

import com.acme.middleware.distributed.transaction.config.DynamicDataSourceConfiguration;
import com.acme.middleware.distributed.transaction.config.SwitchableMySQLReplicationDataSourceConfiguration;
import com.acme.middleware.distributed.transaction.jdbc.datasource.aspect.SwitchableAspect;
import com.acme.middleware.distributed.transaction.service.TransactionMessageService;
import com.acme.middleware.distributed.transaction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * 用户（账户）服务示例
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@RestController
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({UserService.class,
        DynamicDataSourceConfiguration.class,
        SwitchableMySQLReplicationDataSourceConfiguration.class,
        SwitchableAspect.class,
        TransactionMessageService.class})
public class UserServiceSample {

    @Autowired
    private UserService userService;

    @Autowired
    private KafkaTemplate<String, TxEvent> kafkaTemplate;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${kafka.transactions.topic:transactions}")
    private String transactionsTopic;

    @Value("${kafka.transactions.timeout:5000}")
    private long transactionsTimeout;

    @KafkaListener(id = "user-group", topics = {"${kafka.transactions.topic:transactions}"})
    @Transactional
    public void onMessage(TxEvent txEvent, Acknowledgment ack) {
        Long txId = txEvent.getTxId();
        Long sellerId = txEvent.getSellerId();
        Long userId = txEvent.getBuyerId();
        Long amount = txEvent.getAmount();
        userService.updateAmount(txId, sellerId, userId, amount);
        applicationEventPublisher.publishEvent(ack);
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        return userService.getAllUsers();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterComplete(Acknowledgment ack) {
        ack.acknowledge();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollback(Acknowledgment ack) {
        ack.nack(Duration.ZERO);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(UserServiceSample.class)
                .profiles("user", "mysql-replication")
                .run(args);
    }
}