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

import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.rpc.RpcProcessor;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * RPC Request Processor
 *
 * @param <T> The type of request data
 * @param <R> the type of response data
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public interface RequestProcessor<T extends Serializable, R extends Serializable> {

    R process(RequestContext<T> requestContext, Status status);

    default String getRequestType() {
        String requestType = null;
        // TODO Get all generic interfaces
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                if (RequestProcessor.class.equals(parameterizedType.getRawType())) {
                    requestType = parameterizedType.getActualTypeArguments()[0].getTypeName();
                }
            }
        }
        return requestType;
    }

    default RpcProcessor<T> adapt(JRaftServer server) {
        return new RequestProcessorRpcProcessorAdapter(server, this);
    }
}
