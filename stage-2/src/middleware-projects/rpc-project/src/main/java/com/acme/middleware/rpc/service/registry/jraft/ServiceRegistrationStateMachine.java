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
package com.acme.middleware.rpc.service.registry.jraft;

import com.acme.middleware.rpc.service.ServiceInstance;
import com.alipay.remoting.exception.CodecException;
import com.alipay.remoting.serialization.SerializerManager;
import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.error.RaftException;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.alipay.sofa.jraft.util.NamedThreadFactory;
import com.alipay.sofa.jraft.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;


public class ServiceRegistrationStateMachine extends StateMachineAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRegistrationStateMachine.class);
    private static ThreadPoolExecutor executor = ThreadPoolUtil
            .newBuilder()
            .poolName("JRAFT_SERVICE_EXECUTOR")
            .enableMetric(true)
            .coreThreads(3)
            .maximumThreads(5)
            .keepAliveSeconds(60L)
            .workQueue(new SynchronousQueue<>())
            .threadFactory(
                    new NamedThreadFactory("JRaft-ServiceRegistration-Executor-", true)).build();


    /**
     * 服务名称与服务实例列表（List）映射
     */
    private final Map<String, Map<String, ServiceInstance>> serviceNameToInstancesStorage = new ConcurrentHashMap<>();

    private void register(ServiceInstance serviceInstance) {
        String serviceName = serviceInstance.getServiceName();
        String id = serviceInstance.getId();

        synchronized (serviceNameToInstancesStorage) {
            Map<String, ServiceInstance> serviceInstancesMap = serviceNameToInstancesStorage.computeIfAbsent(serviceName,
                    n -> new LinkedHashMap<>());
            serviceInstancesMap.put(id, serviceInstance);
        }
    }


    /**
     * Leader term
     */
    private final AtomicLong leaderTerm = new AtomicLong(-1);

    public boolean isLeader() {
        return this.leaderTerm.get() > 0;
    }

//    /**
//     * Returns current value.
//     */
//    public long getValue() {
//        return this.value.get();
//    }

    @Override
    public void onApply(final Iterator iter) {
        while (iter.hasNext()) {
            ServiceOperation serviceOperation = null;

            ServiceOperationClosure closure = null;
            if (iter.done() != null) {
                // 从当前 Leader 节点获取  Closure
                closure = (ServiceOperationClosure) iter.done();
                serviceOperation = closure.getServiceOperation();
            } else {
                // 在 Follower 节点通过 日志反序列化得到 ServiceOperation
                final ByteBuffer data = iter.getData();
                try {
                    serviceOperation = SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(
                            data.array(), ServiceOperation.class.getName());
                } catch (final CodecException e) {
                    LOG.error("Fail to decode IncrementAndGetRequest", e);
                }
                // follower ignore read operation
//                if (counterOperation != null && counterOperation.isReadOp()) {
//                    iter.next();
//                    continue;
//                }
            }

            if (serviceOperation != null) {
                ServiceOperation.Kind kind = serviceOperation.getKind();
                switch (kind) {
                    case REGISTRATION:
                        // 写入内存操作
                        register((ServiceInstance) serviceOperation.getValue());
                        break;
                }

                if (closure != null) {
                    // closure.success();
                    closure.run(Status.OK());
                }
            }


            iter.next();
        }
    }

    @Override
    public void onSnapshotSave(final SnapshotWriter writer, final Closure done) {
        executor.submit(() -> {
            final ServiceRegistrationSnapshotFile snapshot = new ServiceRegistrationSnapshotFile(writer.getPath() + File.separator + "data");
            // TODO
//            if (snapshot.save(currVal)) {
//                if (writer.addFile("data")) {
//                    done.run(Status.OK());
//                } else {
//                    done.run(new Status(RaftError.EIO, "Fail to add file to writer"));
//                }
//            } else {
//                done.run(new Status(RaftError.EIO, "Fail to save counter snapshot %s", snapshot.getPath()));
//            }
        });
    }

    @Override
    public void onError(final RaftException e) {
        LOG.error("Raft error: {}", e, e);
    }

    @Override
    public boolean onSnapshotLoad(final SnapshotReader reader) {
        if (isLeader()) {
            LOG.warn("Leader is not supposed to load snapshot");
            return false;
        }
        if (reader.getFileMeta("data") == null) {
            LOG.error("Fail to find data file in {}", reader.getPath());
            return false;
        }
        final ServiceRegistrationSnapshotFile snapshot = new ServiceRegistrationSnapshotFile(reader.getPath() + File.separator + "data");
        try {
            // TODO
            // this.value.set(snapshot.load());
            return true;
        } catch (Throwable e) {
            LOG.error("Fail to load snapshot from {}", snapshot.getPath());
            return false;
        }

    }

    @Override
    public void onLeaderStart(final long term) {
        this.leaderTerm.set(term);
        super.onLeaderStart(term);

    }

    @Override
    public void onLeaderStop(final Status status) {
        this.leaderTerm.set(-1);
        super.onLeaderStop(status);
    }

}
