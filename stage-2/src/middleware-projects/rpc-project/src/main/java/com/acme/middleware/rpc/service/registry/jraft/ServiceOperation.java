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
package com.acme.middleware.rpc.service.registry.jraft;

import com.acme.middleware.rpc.service.ServiceInstance;
import com.sun.tools.javac.util.List;

import java.io.Serializable;

/**
 * 服务操作
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see com.acme.middleware.rpc.service.registry.ServiceRegistry
 * @since 1.0.0
 */
public class ServiceOperation<V> implements Serializable {

    private final Kind kind;

    private final V value;

    public ServiceOperation(Kind kind, V value) {
        this.kind = kind;
        this.value = value;
    }

    public Kind getKind() {
        return kind;
    }


    public V getValue() {
        return value;
    }


    public enum Kind {

        REGISTRATION(ServiceInstance.class),

        DEREGISTRATION(ServiceInstance.class),

        GET_SERVICE_INSTANCES(List.class);

        private final Class<?> valueType;

        Kind(Class<?> valueType) {
            this.valueType = valueType;
        }
    }
}
