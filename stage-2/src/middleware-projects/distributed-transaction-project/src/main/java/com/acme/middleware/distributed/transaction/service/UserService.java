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
package com.acme.middleware.distributed.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 账户服务
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since TODO
 */
@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean updateAmount(Long sellerId, Long buyerId, Long amount) {
        jdbcTemplate.execute("UPDATE users SET amt_sold=amt_sold + ? WHERE id=?",
                (PreparedStatementCallback<Void>) ps -> {
                    ps.setLong(1, amount);
                    ps.setLong(2, sellerId);
                    ps.executeUpdate();
                    return null;
                });

        jdbcTemplate.execute("UPDATE users SET amt_bought=amt_bought + ? WHERE id=?",
                (PreparedStatementCallback<Void>) ps -> {
                    ps.setLong(1, amount);
                    ps.setLong(2, buyerId);
                    ps.executeUpdate();
                    return null;
                });


        return true;
    }
}
