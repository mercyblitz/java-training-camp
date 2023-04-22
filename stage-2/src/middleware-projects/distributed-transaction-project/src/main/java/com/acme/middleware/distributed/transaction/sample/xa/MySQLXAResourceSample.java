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
package com.acme.middleware.distributed.transaction.sample.xa;

import com.mysql.cj.jdbc.MysqlXADataSource;
import com.mysql.cj.jdbc.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * MySQL {@link XAResource} 样例
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class MySQLXAResourceSample {

    public static void main(String[] args) throws Throwable {
        String sql = "INSERT INTO user(id,name) VALUE (?,?);";
        int userId = 1;
        String userName = "root";

        // MySQL 本地数据库
        String jdbcURL1 = "jdbc:mysql://127.0.0.1:3306/test";
        XAConnection xaConnection1 = getXAConnection(jdbcURL1);
        // 获取本地 MySQL XAResource
        XAResource xaResource1 = xaConnection1.getXAResource();
        // 创建 Xid
        Xid xid1 = new MysqlXid(new byte[]{1}, new byte[]{2}, 1);
        // 事务管理器关联资源管理器
        xaResource1.start(xid1, XAResource.TMNOFLAGS);
        // 获取本地 MySQL Connection
        Connection connection = xaConnection1.getConnection();
        // 创建 PreparedStatement
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
        preparedStatement1.setInt(1, userId);
        preparedStatement1.setString(2, userName);
        preparedStatement1.executeUpdate();
        // 事务管理器取消关联资源管理器
        xaResource1.end(xid1, XAResource.TMSUCCESS);

        // MySQL 在 Docker 容器
        String jdbcURL2 = "jdbc:mysql://127.0.0.1:13306/test";
        XAConnection xaConnection2 = getXAConnection(jdbcURL2);
        // 获取 Docker MySQL XAResource
        XAResource xaResource2 = xaConnection2.getXAResource();
        Xid xid2 = new MysqlXid(new byte[]{11}, new byte[]{22}, 2);
        // 事务管理器关联资源管理器
        xaResource2.start(xid2, XAResource.TMNOFLAGS);
        // 获取 Docker MySQL Connection
        Connection connection2 = xaConnection2.getConnection();
        // 创建 PreparedStatement
        PreparedStatement preparedStatement2 = connection2.prepareStatement(sql);
        preparedStatement2.setInt(1, userId);
        preparedStatement2.setString(2, userName);
        preparedStatement2.executeUpdate();
        // 事务管理器取消关联资源管理器
        xaResource2.end(xid2, XAResource.TMSUCCESS);

        // 两阶段提交
        // 第一阶段提交
        int result1 = xaResource1.prepare(xid1);
        int result2 = xaResource2.prepare(xid2);

        // 第二阶段提交，如果第一阶段提交无误的话
        if (XAResource.XA_OK == result1 && XAResource.XA_OK == result2) {
            xaResource1.commit(xid1, false);
            xaResource2.commit(xid2, false);
        }else {
            xaResource1.rollback(xid1);
            xaResource2.rollback(xid1);
        }

        // 关闭数据库连接
        xaConnection1.close();
        xaConnection2.close();
    }

    private static XAConnection getXAConnection(String jdbcURL) throws SQLException {
        String userName = "root";
        String password = "123456";
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setURL(jdbcURL);
        return xaDataSource.getXAConnection(userName, password);
    }
}
