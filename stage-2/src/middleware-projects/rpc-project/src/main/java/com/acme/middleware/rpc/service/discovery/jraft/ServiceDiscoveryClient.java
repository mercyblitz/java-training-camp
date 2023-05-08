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

import com.alipay.sofa.jraft.RouteTable;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.rpc.CliClientService;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.rpc.impl.cli.CliClientServiceImpl;

import java.util.concurrent.TimeUnit;

/**
 * 服务发现客户端
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class ServiceDiscoveryClient {

    private String groupId = "service-discovery";

    private String registryAddress;


    private RpcClient rpcClient;

    private CliClientService cliClientService;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void init() {

        ServiceDiscoveryGrpcHelper.initGRpc();

        Configuration conf = new Configuration();
        if (!conf.parse(registryAddress)) {
            throw new IllegalArgumentException("Fail to parse conf:" + registryAddress);
        }

        RouteTable.getInstance().updateConfiguration(groupId, conf);

        CliClientServiceImpl cliClientService = new CliClientServiceImpl();
        cliClientService.init(new CliOptions());


        this.cliClientService = cliClientService;
        this.rpcClient = cliClientService.getRpcClient();
    }

    public <R> R invoke(Object request) throws Throwable {
        if (!RouteTable.getInstance().refreshLeader(cliClientService, groupId, 1000).isOk()) {
            throw new IllegalStateException("Refresh leader failed");
        }

        PeerId leader = RouteTable.getInstance().selectLeader(groupId);
        return (R) rpcClient.invokeSync(leader.getEndpoint(), request, TimeUnit.SECONDS.toMillis(5));
    }

}
