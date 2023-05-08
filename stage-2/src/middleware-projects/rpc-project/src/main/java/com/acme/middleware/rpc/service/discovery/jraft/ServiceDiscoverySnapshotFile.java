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
package com.acme.middleware.rpc.service.discovery.jraft;

import com.acme.middleware.rpc.service.ServiceInstance;
import com.alipay.remoting.serialization.Serializer;
import com.alipay.remoting.serialization.SerializerManager;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.alipay.remoting.serialization.SerializerManager.Hessian2;


public class ServiceDiscoverySnapshotFile {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDiscoverySnapshotFile.class);

    private static final Serializer serializer = SerializerManager.getSerializer(Hessian2);

    private final File file;

    public ServiceDiscoverySnapshotFile(String path) {
        super();
        this.file = new File(path);
    }

    public File getFile() {
        return file;
    }

    /**
     * Save value to snapshot file.
     */
    public boolean save(Map<String, Map<String, ServiceInstance>> data) {
        try {
            byte[] bytes = serializer.serialize(data);
            FileUtils.writeByteArrayToFile(file, bytes);
            return true;
        } catch (Throwable e) {
            LOG.error("Fail to save snapshot", e);
            return false;
        }
    }

    public Map<String, Map<String, ServiceInstance>> load() throws IOException {
        Map<String, Map<String, ServiceInstance>> map = null;
        try {
            byte[] bytes = FileUtils.readFileToByteArray(this.file);
            map = serializer.deserialize(bytes, Map.class.getName());
        } catch (Throwable e) {
            LOG.error("Fail to load snapshot", e);
        }
        return map;
    }
}
