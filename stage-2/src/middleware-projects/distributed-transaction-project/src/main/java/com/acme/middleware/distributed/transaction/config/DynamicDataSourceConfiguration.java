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
package com.acme.middleware.distributed.transaction.config;

import com.acme.middleware.distributed.transaction.jdbc.datasource.DynamicDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.BeanFactoryDataSourceLookup;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

import javax.sql.DataSource;
import java.util.Map;

/**
 * {@link DataSource} 配置类，与 {@link DataSourceAutoConfiguration} 互斥的
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@Profile("datasources")
public class DynamicDataSourceConfiguration {

    @Autowired
    private Environment environment;

    /**
     * 写 DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "datasources.write")
    public DataSource writeDataSource() {
       return new HikariDataSource();
    }

    /**
     * 读 DataSource
     */
    @Bean
    @ConfigurationProperties(prefix = "datasources.read")
    public DataSource readDataSource() {
        return new HikariDataSource();
    }

    @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(Map<String, DataSource> targetDataSources,
            @Qualifier("dataSourceLookup") DataSourceLookup dataSourceLookup) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources((Map) targetDataSources);
        dynamicDataSource.setDataSourceLookup(dataSourceLookup);
        // 设置 DataSource Bean 名称
        dynamicDataSource.setDefaultTargetDataSource("writeDataSource");
        return dynamicDataSource;
    }

    @Bean
    public BeanFactoryDataSourceLookup dataSourceLookup() {
        return new BeanFactoryDataSourceLookup();
    }
}
