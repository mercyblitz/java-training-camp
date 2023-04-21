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
package com.acme.middleware.distributed.transaction.jdbc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
@Configuration
public class JdbcConfiguration {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof PlatformTransactionManager) {
                    PlatformTransactionManager ptm = (PlatformTransactionManager) bean;
                    return new PlatformTransactionManagerWrapper(ptm);
                }
                return bean;
            }
        };
    }

    class PlatformTransactionManagerWrapper implements PlatformTransactionManager {

        private final PlatformTransactionManager delegate;

        PlatformTransactionManagerWrapper(PlatformTransactionManager delegate) {
            this.delegate = delegate;
        }

        @Override
        public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
            return delegate.getTransaction(definition);
        }

        @Override
        public void commit(TransactionStatus status) throws TransactionException {
            if (status.isNewTransaction()) {
                applicationEventPublisher.publishEvent("before-commit");
            }
            delegate.commit(status);
        }

        @Override
        public void rollback(TransactionStatus status) throws TransactionException {
            if (status.isNewTransaction()) {
                applicationEventPublisher.publishEvent("before-rollback");
            }
            delegate.rollback(status);
        }


    }


}
