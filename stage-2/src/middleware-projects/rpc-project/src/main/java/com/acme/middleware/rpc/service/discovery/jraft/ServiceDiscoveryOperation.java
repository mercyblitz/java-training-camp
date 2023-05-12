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

import com.acme.middleware.rpc.service.discovery.ServiceDiscovery;
import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.Serializer;
import com.alipay.remoting.serialization.SerializerManager;

import java.io.Serializable;
import java.nio.ByteBuffer;

import static com.alipay.remoting.serialization.SerializerManager.Hessian2;

/**
 * 服务发现操作
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ServiceDiscovery
 * @since 1.0.0
 */
public class ServiceDiscoveryOperation<V> implements Serializable {

    private static final Serializer serializer = SerializerManager.getSerializer(Hessian2);

    private final Kind kind;

    private final V data;

    public ServiceDiscoveryOperation(Kind kind, V value) {
        this.kind = kind;
        this.data = value;
    }

    public Kind getKind() {
        return kind;
    }


    public V getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ServiceDiscoveryOperation{" +
                "kind=" + kind +
                ", data=" + data +
                '}';
    }

    public ByteBuffer serialize() {
        byte[] data = null;
        try {
            data = serializer.serialize(this);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        }
        return ByteBuffer.wrap(data);
    }

    public static ServiceDiscoveryOperation deserialize(ByteBuffer data) {
        byte[] bytes = data.array();
        ServiceDiscoveryOperation operation = null;
        try {
            operation = serializer.deserialize(bytes, ServiceDiscoveryOperation.class.getName());
        } catch (CodecException e) {
            throw new RuntimeException(e);
        }
        return operation;
    }


    public enum Kind {

        REGISTRATION,

        DEREGISTRATION,

        GET_SERVICE_INSTANCES,

        BEAT;
    }
}
