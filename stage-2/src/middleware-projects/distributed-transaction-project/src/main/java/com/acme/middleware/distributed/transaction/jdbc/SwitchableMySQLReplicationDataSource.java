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
package com.acme.middleware.distributed.transaction.jdbc;

import com.acme.middleware.distributed.transaction.jdbc.datasource.util.DataSourceType;
import com.mysql.cj.jdbc.ha.ReplicationConnection;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.ShardingKeyBuilder;
import java.util.logging.Logger;

import static org.springframework.util.ClassUtils.resolveClassName;

/**
 * 可切换 MySQL 复制 {@link DataSource}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ReplicationConnection
 * @since 1.0.0
 */
public class SwitchableMySQLReplicationDataSource implements DataSource {

    private static final String MYSQL_REPLICATION_CONNECTION_CLASS_NAME = "com.mysql.cj.jdbc.ha.ReplicationConnection";

    private static final Class<ReplicationConnection> MYSQL_REPLICATION_CONNECTION_CLASS = (Class<ReplicationConnection>)
            resolveClassName(MYSQL_REPLICATION_CONNECTION_CLASS_NAME, SwitchableMySQLReplicationDataSource.class.getClassLoader());

    private static final boolean MYSQL_REPLICATION_CONNECTION_CLASS_PRESENT = MYSQL_REPLICATION_CONNECTION_CLASS != null;

    private final DataSource dataSource;

    public SwitchableMySQLReplicationDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return resolveConnection(dataSource.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return resolveConnection(dataSource.getConnection(username, password));
    }

    private Connection resolveConnection(Connection rawConnection) throws SQLException {
        Connection connection = rawConnection;
        if (MYSQL_REPLICATION_CONNECTION_CLASS_PRESENT && connection.isWrapperFor(MYSQL_REPLICATION_CONNECTION_CLASS)) {
            ReplicationConnection replicationConnection = rawConnection.unwrap(MYSQL_REPLICATION_CONNECTION_CLASS);
            DataSourceType dataSourceType = DataSourceType.current();
            switch (dataSourceType) {
                case READ:
                    connection = replicationConnection.getReplicaConnection();
                    break;
                default:
                    connection = replicationConnection.getSourceConnection();
            }
        }
        return connection;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return dataSource.createConnectionBuilder();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return dataSource.getParentLogger();
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return dataSource.createShardingKeyBuilder();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return dataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return dataSource.isWrapperFor(iface);
    }
}
