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
package com.acme.middleware.distributed.transaction.jdbc.datasource.util;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * {@link DataSource} 类型
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public enum DataSourceType {

    /**
     * 写 {@link DataSource}
     */
    WRITE("writeDataSource"),

    /**
     * 读 {@link DataSource}
     */
    READ("readDataSource");

    private final String beanName;

    private final static ThreadLocal<String> dataSourceBeanNameHolder = ThreadLocal.withInitial(() -> null);

    DataSourceType(String beanName) {
        this.beanName = beanName;
    }

    public void switchDataSource() {
        dataSourceBeanNameHolder.set(beanName);
    }

    public static DataSourceType current() {
        String beanName = getDataSourceBeanName();
        DataSourceType current = DataSourceType.WRITE;
        if (beanName != null) {
            for (DataSourceType type : DataSourceType.values()) {
                if (Objects.equals(beanName, type.beanName)) {
                    current = type;
                    break;
                }
            }
        }
        return current;
    }

    public static String getDataSourceBeanName() {
        return dataSourceBeanNameHolder.get();
    }

    public static void resetDataSource() {
        dataSourceBeanNameHolder.remove();
    }
}
