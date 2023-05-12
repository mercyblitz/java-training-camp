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

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link RequestContext} {@link Closure}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see RequestContext
 * @see Closure
 * @since 1.0.0
 */
public class RequestContextClosure implements Closure {

    private static final Logger logger = LoggerFactory.getLogger(RequestContextClosure.class);

    private final RequestContext requestContext;

    private final Closure delegate;

    private Object result;

    public RequestContextClosure(RequestContext requestContext, Closure delegate) {
        this.requestContext = requestContext;
        this.delegate = delegate;
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public void run(Status status) {
        logger.info("Run closure[status : {}] with operation : {}", status, requestContext);
        delegate.run(status);
    }
}
