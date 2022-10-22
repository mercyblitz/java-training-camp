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
package com.acme.biz.web.servlet.embedded.tomcat;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

/**
 * 自定义 Tomcat {@link ProtocolHandler} 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class MyTomcatProtocolHandlerCustomizer implements TomcatProtocolHandlerCustomizer {

    private final TomcatServletWebServerFactory factory;

    public MyTomcatProtocolHandlerCustomizer(TomcatServletWebServerFactory factory) {
        this.factory = factory;
    }

    @Override
    public void customize(ProtocolHandler protocolHandler) {
        if (protocolHandler instanceof AbstractProtocol) {
            AbstractProtocol protocol = (AbstractProtocol) protocolHandler;
            protocol.setMaxThreads(100);
        }
    }
}
