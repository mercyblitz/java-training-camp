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
package com.acme.middleware.rpc.service.discovery.jraft.common;

import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.Serializer;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Node;

import java.io.Serializable;
import java.nio.ByteBuffer;

import static com.alipay.remoting.serialization.SerializerManager.Hessian2;

/**
 * Request Context
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see
 * @since 1.0.0
 */
public class RequestContext<T extends Serializable> {

    private static final Serializer serializer = SerializerManager.getSerializer(Hessian2);

    private final T data;

    private final String dataType;

    private final Node node;

    private final JRaftStateMachine fsm;

    public RequestContext(T data, Node node, JRaftStateMachine fsm) {
        this.data = data;
        this.dataType = data.getClass().getName();
        this.node = node;
        this.fsm = fsm;
    }

    public T getData() {
        return data;
    }

    public String getDataType() {
        return dataType;
    }

    public Node getNode() {
        return node;
    }

    public JRaftStateMachine getFsm() {
        return fsm;
    }

    @Override
    public String toString() {
        return "RequestContext{" +
                "data=" + data +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    public ByteBuffer serialize() {
        byte[] data = null;
        try {
            data = serializer.serialize(this.data);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        }
        return ByteBuffer.wrap(data);
    }

    public static <V extends Serializable> RequestContext deserialize(ByteBuffer byteBuffer, Node node, JRaftStateMachine fsm) {
        byte[] bytes = byteBuffer.array();
        RequestContext requestContext = null;
        try {
            V data = serializer.deserialize(bytes, RequestContext.class.getName());
            requestContext = new RequestContext(data, node, fsm);
        } catch (CodecException e) {
            throw new RuntimeException(e);
        }
        return requestContext;
    }
}
