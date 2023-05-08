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
package com.acme.middleware.rpc.service.discovery.jraft;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;

/**
 * 服务操作回调
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ServiceDiscoveryOperationClosure implements Closure {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryOperationClosure.class);

    private final ServiceDiscoveryOperation serviceDiscoveryOperation;

    private final BiConsumer<Status, Object> callback;

    private Object result;

    public ServiceDiscoveryOperationClosure(ServiceDiscoveryOperation serviceDiscoveryOperation, BiConsumer<Status, Object> callback) {
        this.serviceDiscoveryOperation = serviceDiscoveryOperation;
        this.callback = callback;
    }

    public ServiceDiscoveryOperation getServiceDiscoveryOperation() {
        return serviceDiscoveryOperation;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public void run(Status status) {
        logger.info("Run closure[status : {}] with operation : {}", status, serviceDiscoveryOperation);
        callback.accept(status, getResult());
    }
}
