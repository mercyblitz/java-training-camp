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

import java.sql.ResultSet;

/**
 * 交易服务
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Service
public class TransactionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Long addTransaction(Long sellerId, Long buyerId, Long amount) {
        Long txId = System.currentTimeMillis() / 1000;
        jdbcTemplate.execute("INSERT INTO transactions(xid,seller_id,buyer_id,amount) VALUES (?,?,?,?)",
                (PreparedStatementCallback<Void>) ps -> {
                    ps.setLong(1, txId);
                    ps.setLong(2, sellerId);
                    ps.setLong(3, buyerId);
                    ps.setLong(4, amount);
                    ps.executeUpdate();
                    return null;
                });

        System.out.println(txId);

//        Long txId = jdbcTemplate.execute((ConnectionCallback<Long>) connection -> {
//            PreparedStatement ps = connection.prepareStatement("INSERT INTO transactions(seller_id,buyer_id,amount) VALUES (?,?,?)");
//            ps.setLong(1, sellerId);
//            ps.setLong(2, buyerId);
//            ps.setLong(3, amount);
//            ps.executeUpdate();
//        });
        return txId;
    }
}
