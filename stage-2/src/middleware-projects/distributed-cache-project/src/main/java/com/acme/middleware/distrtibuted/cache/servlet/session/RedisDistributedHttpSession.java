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
package com.acme.middleware.distrtibuted.cache.servlet.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
public class RedisDistributedHttpSession implements HttpSession {

    private final HttpSession httpSession;

    public RedisDistributedHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public long getCreationTime() {
        return httpSession.getCreationTime();
    }

    @Override
    public String getId() {
        return httpSession.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return httpSession.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return httpSession.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        // Redis set expire time
        httpSession.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return httpSession.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return httpSession.getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        // Redis Key : HttpSession Id -> Hash
        // Hash Keys
        return null;
    }

    @Override
    @Deprecated
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {
        String key = getId();
        // hset key name value
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public void invalidate() {
        // Redis hash remove
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
