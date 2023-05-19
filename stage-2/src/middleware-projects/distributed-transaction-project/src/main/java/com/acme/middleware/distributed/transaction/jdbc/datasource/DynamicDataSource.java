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
package com.acme.middleware.distributed.transaction.jdbc.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * 动态 {@link DataSource} 实现
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static ThreadLocal<String> dataSourceBeanNameHolder = ThreadLocal.withInitial(() -> null);

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceBeanNameHolder.get();
    }


    public static void setDataSourceBeanName(String dataSourceBeanName) {
        dataSourceBeanNameHolder.set(dataSourceBeanName);
    }

    public static void clearDataSourceBeanName() {
        dataSourceBeanNameHolder.remove();
    }
}
