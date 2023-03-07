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
package com.acme.biz.web.service;

import com.acme.biz.api.exception.UserException;
import com.acme.biz.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO Comment
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
public class SubInMemoryUserRegistrationService extends InMemoryUserRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(SubInMemoryUserRegistrationService.class);

    @Override
    public Boolean registerUser(User user) throws UserException {
        logger.info("...");
        return super.registerUser(user);
    }
}
