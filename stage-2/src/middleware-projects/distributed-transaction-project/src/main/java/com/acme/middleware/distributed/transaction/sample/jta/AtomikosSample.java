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
package com.acme.middleware.distributed.transaction.sample.jta;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.transaction.UserTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JTA 开源框架 - Atomikos
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class AtomikosSample {

    public static void main(String[] args) throws Throwable {
        // MySQL 本地数据库
        String jdbcURL1 = "jdbc:mysql://127.0.0.1:3306/test";
        AtomikosDataSourceBean atomikosDataSourceBean1 = getAtomikosDataSourceBean(jdbcURL1);

        // MySQL 在 Docker 容器
        String jdbcURL2 = "jdbc:mysql://127.0.0.1:13306/test";
        AtomikosDataSourceBean atomikosDataSourceBean2 = getAtomikosDataSourceBean(jdbcURL2);

        UserTransaction userTransaction = new UserTransactionImp();

        // begin 方法未关联  XAResource.start 操作
        userTransaction.begin();
        // 插入 User 数据到 MySQL 本地数据库
        // Atomikos 在 JDBC Connection 接口上实现动态代理，拦截 enlist 方法，包括:
        // createStatement , prepareStatement 以及 prepareCall 方法
        insertUser(atomikosDataSourceBean1.getConnection());
        // 插入 User 数据到 MySQL 在 Docker 容器
         insertUser(atomikosDataSourceBean2.getConnection());
        // commit 方法分别执行 XAResource end , prepare 以及 commit 操作
        userTransaction.commit();

        // 关闭数据源
        atomikosDataSourceBean1.close();
        atomikosDataSourceBean2.close();
    }

    private static void insertUser(Connection connection) throws SQLException {
        String sql = "INSERT INTO user(name) VALUE (?);";
        String userName = "admin";
        // 创建 PreparedStatement
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
        preparedStatement1.setString(1, userName);
        preparedStatement1.executeUpdate();
    }


    private static AtomikosDataSourceBean getAtomikosDataSourceBean(String jdbcURL) {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(jdbcURL);
        ds.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");
        Properties p = new Properties();
        p.setProperty("user", "root");
        p.setProperty("password", "123456");
        p.setProperty("URL", jdbcURL);
        ds.setXaProperties(p);
        ds.setPoolSize(5);
        return ds;
    }

}