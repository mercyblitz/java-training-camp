package com.acme.middleware.rpc.service.discovery.jraft;

import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.discovery.proto.ServiceDiscoveryOuter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务实例心跳线程
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class ServiceDiscoveryHeartBeat implements Runnable {


    private final ServiceInstance serviceInstance;
    private final ServiceDiscoveryClient client;

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryHeartBeat.class);

    public ServiceDiscoveryHeartBeat(ServiceInstance serviceInstance, ServiceDiscoveryClient client) {
        this.serviceInstance = serviceInstance;
        this.client = client;
    }

    @Override
    public void run() {
        ServiceDiscoveryOuter.HeartBeat heartBeat = buildHeartBeat(serviceInstance);
        try {
            client.invoke(heartBeat);
        } catch (Throwable e) {
            logger.error("Fail to send heartbeat for a service instance : " + serviceInstance, e);
        }
    }

    private ServiceDiscoveryOuter.HeartBeat buildHeartBeat(ServiceInstance serviceInstance) {
        return ServiceDiscoveryOuter.HeartBeat.newBuilder()
                .setId(serviceInstance.getId())
                .setHost(serviceInstance.getHost())
                .setServiceName(serviceInstance.getServiceName())
                .setPort(serviceInstance.getPort())
                .build();
    }
}
