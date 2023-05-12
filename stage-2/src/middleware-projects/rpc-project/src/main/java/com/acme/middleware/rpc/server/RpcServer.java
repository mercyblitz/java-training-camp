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
package com.acme.middleware.rpc.server;

import ch.qos.logback.core.util.ContextUtil;
import com.acme.middleware.rpc.codec.MessageDecoder;
import com.acme.middleware.rpc.codec.MessageEncoder;
import com.acme.middleware.rpc.context.ServiceContext;
import com.acme.middleware.rpc.service.DefaultServiceInstance;
import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.discovery.ServiceDiscovery;
import com.acme.middleware.rpc.transport.InvocationRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用服务器
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
public class RpcServer implements AutoCloseable {

    private final String applicationName;

    private final int port;

    private final ServiceContext serviceContext;

    private final ServiceDiscovery serviceDiscovery;

    private ServiceInstance localServiceInstance;

    private ServerBootstrap bootstrap;

    private EventLoopGroup group;

    private NioEventLoopGroup workerGroup;

    private Channel channel;

    public RpcServer(String applicationName, int port) {
        this.applicationName = applicationName;
        this.port = port;
        this.serviceContext = ServiceContext.DEFAULT;
        this.serviceDiscovery = ServiceDiscovery.DEFAULT;
    }

    private ServiceInstance createLocalServiceInstance() throws Exception {
        DefaultServiceInstance serviceInstance = new DefaultServiceInstance();
        String host = ContextUtil.getLocalHostName();
        serviceInstance.setId(generateInstanceId(host, applicationName, port));
        serviceInstance.setHost(host);
        serviceInstance.setPort(port);
        serviceInstance.setServiceName(applicationName);
        // TODO
        serviceInstance.setMetadata(new HashMap<>());
        return serviceInstance;
    }

    private String generateInstanceId(String host, String serviceName, int port) {
        return String.format("%s-%s-%d", host, serviceName, port);
    }

    public RpcServer registerService(String serviceName, Object service) {
        serviceContext.registerService(serviceName, service);
        return this;
    }

    public RpcServer start() throws Exception {
        this.serviceDiscovery.initialize((Map) System.getProperties());
        this.localServiceInstance = createLocalServiceInstance();
        this.bootstrap = new ServerBootstrap();
        this.group = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        bootstrap.group(group, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("message-encoder", new MessageEncoder());
                        ch.pipeline().addLast("message-decoder", new MessageDecoder());
                        ch.pipeline().addLast("request-handler", new InvocationRequestHandler(serviceContext));
                    }
                });

        ChannelFuture channelFuture = bootstrap.bind(port);
        // 注册服务
        registerServer();
        //监听shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> serviceDiscovery.deregister(localServiceInstance)));
        try {
            channel = channelFuture.sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void registerServer() {
        serviceDiscovery.register(localServiceInstance);
    }

    private void deregisterServer() {
        serviceDiscovery.deregister(localServiceInstance);
    }

    @Override
    public void close() throws Exception {
        deregisterServer();
        if (channel != null) {
            channel.close().sync();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }
}
