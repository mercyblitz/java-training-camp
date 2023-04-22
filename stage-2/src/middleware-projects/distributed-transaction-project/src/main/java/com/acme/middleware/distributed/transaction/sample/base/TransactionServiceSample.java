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

import com.acme.middleware.distributed.transaction.service.TransactionMessageService;
import com.acme.middleware.distributed.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 交易服务示例
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@RestController
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
@Import({TransactionService.class, TransactionMessageService.class})
public class TransactionServiceSample {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceSample.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private KafkaTemplate<String, TxEvent> kafkaTemplate;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value("${kafka.transactions.topic:transactions}")
    private String transactionsTopic;

    @Value("${kafka.transactions.timeout:5000}")
    private long transactionsTimeout;

    @GetMapping("/tx/{sellerId}/{buyerId}/{amount}")
    @Transactional
    public boolean tx(@PathVariable Long sellerId, @PathVariable Long buyerId, @PathVariable Long amount) {
        Long txId = transactionService.addTransaction(sellerId, buyerId, amount);
        applicationEventPublisher.publishEvent(new TxEvent(this, txId, sellerId, buyerId, amount));
        return true;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void beforeCommit(TxEvent txEvent) throws Throwable {
        ListenableFuture<SendResult<String, TxEvent>> future = kafkaTemplate.send(transactionsTopic, txEvent);
        SendResult<String, TxEvent> result = future.get(transactionsTimeout, TimeUnit.MILLISECONDS);
        logger.info("{}", result);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(TransactionServiceSample.class)
                .profiles("tx", "test")
                .run(args);
    }
}
