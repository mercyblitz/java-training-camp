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
package com.acme.biz.data.fault.tolerance.mybatis;

import io.vavr.CheckedFunction0;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * Executor 静态拦截实现（包装器）
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public abstract class ExecutorDecorator implements Executor {

    private Executor delegate;

    /**
     * 当前 delegate 是否为最原始的被包装对象
     */
    private boolean originalDelegate;

    public ExecutorDecorator() {
    }

    /**
     * 包装 Executor
     * @param delegate 被包装对象
     * @param originalDelegate  当前 delegate 是否为最原始的被包装对象
     */
    public Executor setDelegate(Executor delegate, boolean originalDelegate) {
        this.delegate = delegate;
        this.originalDelegate = originalDelegate;
        return this;
    }

    protected <R> R doInDelegate(MappedStatement ms, CheckedFunction0<R> action) throws SQLException {
        R result = null;
        before(ms);
        try {
            if (originalDelegate) {
                result = action.apply();
            }
        } catch (Throwable e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
        } finally {
            after(ms);
        }
        return result;
    }

    protected abstract void before(MappedStatement ms);

    protected abstract void after(MappedStatement ms);

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        return doInDelegate(ms, () -> delegate.update(ms, parameter));
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        return delegate.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        return delegate.query(ms, parameter, rowBounds, resultHandler);
    }

    @Override
    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        return delegate.queryCursor(ms, parameter, rowBounds);
    }

    @Override
    public List<BatchResult> flushStatements() throws SQLException {
        return delegate.flushStatements();
    }

    @Override
    public void commit(boolean required) throws SQLException {
        delegate.commit(required);
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        delegate.rollback(required);
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        return delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }

    @Override
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return delegate.isCached(ms, key);
    }

    @Override
    public void clearLocalCache() {
        delegate.clearLocalCache();
    }

    @Override
    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
        delegate.deferLoad(ms, resultObject, property, key, targetType);
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public void close(boolean forceRollback) {
        delegate.close(forceRollback);
    }

    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    @Override
    public void setExecutorWrapper(Executor executor) {
        delegate.setExecutorWrapper(executor);
    }


}
