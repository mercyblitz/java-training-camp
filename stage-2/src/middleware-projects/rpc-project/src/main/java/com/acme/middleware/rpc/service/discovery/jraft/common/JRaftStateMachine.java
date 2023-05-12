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

import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * JRaft FSM
 */
public class JRaftStateMachine extends StateMachineAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JRaftStateMachine.class);

    /**
     * Leader term
     */
    private final AtomicLong leaderTerm = new AtomicLong(-1);

    private final Map<String, RequestProcessor> requestProcessorsRepository = new HashMap<>();

    private Node node;

    public boolean isLeader() {
        return this.leaderTerm.get() > 0;
    }

    @Override
    public void onApply(final Iterator iter) {
        while (iter.hasNext()) {
            RequestContext requestContext = null;

            RequestContextClosure closure = null;
            if (iter.done() != null) {
                closure = (RequestContextClosure) iter.done();
                requestContext = closure.getRequestContext();
                logger.info("The closure with operation[{}] at the Leader node[{}]", requestContext, node);
            } else {
                final ByteBuffer data = iter.getData();
                requestContext = RequestContext.deserialize(data, node, this);
                logger.info("The closure with operation[{}] at the Follower node[{}]", requestContext, node);
            }

            if (requestContext != null) {
                if (closure != null) {
                    closure.run(Status.OK());
                } else { // Follower
                    String requestType = requestContext.getDataType();
                    // locate RequestProcessor
                    RequestProcessor requestProcessor = requestProcessorsRepository.get(requestType);
                    requestProcessor.process(requestContext, Status.OK());
                    logger.info("Locate the RequestProcessor[class : '{}'] by the request type : '{}'",
                            requestProcessor.getClass().getName(), requestType);
                    // TODO skip read operation in the follower node
                }
            }

            iter.next();
        }
    }

    @Override
    public void onLeaderStart(final long term) {
        this.leaderTerm.set(term);
        super.onLeaderStart(term);
    }

    @Override
    public void onLeaderStop(final Status status) {
        this.leaderTerm.set(-1);
        super.onLeaderStop(status);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void registerRequestProcessor(RequestProcessor requestProcessor) {
        requestProcessorsRepository.put(requestProcessor.getRequestType(), requestProcessor);
    }
}
