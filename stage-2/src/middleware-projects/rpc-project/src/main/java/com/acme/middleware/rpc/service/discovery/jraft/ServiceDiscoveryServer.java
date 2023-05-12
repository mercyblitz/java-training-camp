
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

import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.RaftGroupService;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.rpc.RaftRpcServerFactory;
import com.alipay.sofa.jraft.rpc.RpcServer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static com.acme.middleware.rpc.service.discovery.jraft.JRaftServiceDiscovery.DEFAULT_GROUP_ID_PROPERTY_VALUE;
import static com.acme.middleware.rpc.service.discovery.jraft.JRaftServiceDiscovery.GROUP_ID_PROPERTY_NAME;


public class ServiceDiscoveryServer {

    private RaftGroupService raftGroupService;

    private Node node;

    private ServiceDiscoveryStateMachine fsm;

    public ServiceDiscoveryServer(final String dataPath, final String groupId, final PeerId serverId,
                                  final NodeOptions nodeOptions) throws IOException {
        // init raft data path, it contains log,meta,snapshot
        FileUtils.forceMkdir(new File(dataPath));

        // here use same RPC server for raft and business. It also can be seperated generally
        final RpcServer rpcServer = RaftRpcServerFactory.createRaftRpcServer(serverId.getEndpoint());

        // register business processor
        rpcServer.registerProcessor(new RegistrationRpcProcessor(this));
        rpcServer.registerProcessor(new GetServiceInstancesRequestRpcProcessor(this));
        rpcServer.registerProcessor(new HeartBeatRpcProcessor(this));
        // TODO

        // init state machine
        this.fsm = new ServiceDiscoveryStateMachine();
        // set fsm to nodeOptions
        nodeOptions.setFsm(this.fsm);
        // set the InMemoryJRaftServiceFactory
        nodeOptions.setServiceFactory(new InMemoryJRaftServiceFactory());
        // set storage path (log,meta,snapshot)
        // log, must
        nodeOptions.setLogUri(dataPath + File.separator + "log");
        // meta, must
        nodeOptions.setRaftMetaUri(dataPath + File.separator + "raft_meta");
        // init raft group service framework
        this.raftGroupService = new RaftGroupService(groupId, serverId, nodeOptions, rpcServer);
        // start raft node
        this.node = this.raftGroupService.start();
        this.fsm.setNode(this.node);

        //心跳检查线程
        Thread beatCheckThread = new ServiceInstanceBeatThread(this.fsm);
        beatCheckThread.setDaemon(true);
        beatCheckThread.start();
    }

    public ServiceDiscoveryStateMachine getFsm() {
        return this.fsm;
    }

    public Node getNode() {
        return this.node;
    }

    public RaftGroupService RaftGroupService() {
        return this.raftGroupService;
    }

    /**
     * Redirect request to new leader
     */
    public String getRedirect() {
        String redirect = null;
        if (this.node != null) {
            final PeerId leader = this.node.getLeaderId();
            if (leader != null) {
                redirect = leader.toString();
            }
        }
        return redirect;
    }

    public static void main(final String[] args) throws IOException {
        if (args.length != 2) {
            String className = ServiceDiscoveryServer.class.getName();
            System.err.printf("Usage : %s {serverId} {initConf}\n", className);
            System.err.printf("Example: %s 127.0.0.1:8081 127.0.0.1:8081,127.0.0.1:8082,127.0.0.1:8083\n", className);
            System.exit(1);
        }
        final String serverIdStr = args[0];
        final String initConfStr = args[1];
        final String dataPath = System.getProperty("user.dir") + File.separator + ".service-discovery" + File.separator + serverIdStr.replace(':', '_');
        final String groupId = System.getProperty(GROUP_ID_PROPERTY_NAME, DEFAULT_GROUP_ID_PROPERTY_VALUE);


        final NodeOptions nodeOptions = new NodeOptions();
        // for test, modify some params
        // set election timeout to 1s
        nodeOptions.setElectionTimeoutMs(1000);
        // disable CLI service。
        nodeOptions.setDisableCli(false);
        // do snapshot every 30s
        nodeOptions.setSnapshotIntervalSecs(30);
        // parse server address
        final PeerId serverId = new PeerId();
        if (!serverId.parse(serverIdStr)) {
            throw new IllegalArgumentException("Fail to parse serverId:" + serverIdStr);
        }
        final Configuration initConf = new Configuration();
        if (!initConf.parse(initConfStr)) {
            throw new IllegalArgumentException("Fail to parse initConf:" + initConfStr);
        }
        // set cluster configuration
        nodeOptions.setInitialConf(initConf);

        // start raft server
        final ServiceDiscoveryServer serviceDiscoveryServer = new ServiceDiscoveryServer(dataPath, groupId, serverId, nodeOptions);
        System.out.println("Started counter server at port:"
                + serviceDiscoveryServer.getNode().getNodeId().getPeerId().getPort());
        // GrpcServer need block to prevent process exit
        ServiceDiscoveryGrpcHelper.blockUntilShutdown();
    }
}
