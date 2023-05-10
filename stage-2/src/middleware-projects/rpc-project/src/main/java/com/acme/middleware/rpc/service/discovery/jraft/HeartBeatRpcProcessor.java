package com.acme.middleware.rpc.service.discovery.jraft;

import com.acme.middleware.rpc.service.DefaultServiceInstance;
import com.acme.middleware.rpc.service.ServiceInstance;
import com.acme.middleware.rpc.service.discovery.proto.ServiceDiscoveryOuter;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.acme.middleware.rpc.service.discovery.jraft.ServiceDiscoveryOperation.Kind.*;

/**
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public class HeartBeatRpcProcessor implements RpcProcessor<ServiceDiscoveryOuter.HeartBeat> {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatRpcProcessor.class);

    private final ServiceDiscoveryServer server;

    public HeartBeatRpcProcessor(ServiceDiscoveryServer server) {
        this.server = server;
    }

    @Override
    public void handleRequest(RpcContext rpcContext, ServiceDiscoveryOuter.HeartBeat heartBeat) {
        ServiceInstance serviceInstance = adaptServiceInstance(heartBeat);
        ServiceDiscoveryOperation.Kind kind = BEAT;
        ServiceDiscoveryOperation<ServiceInstance> operation = new ServiceDiscoveryOperation<>(kind, serviceInstance);

        ServiceDiscoveryOperationClosure closure = new ServiceDiscoveryOperationClosure(operation, (status, result) -> {
            if (!status.isOk()) {
                logger.warn("Closure status is : {}", status);
                return;
            }
            // RPC 响应到客户端
            rpcContext.sendResponse(response(status));
            logger.info("heartbeat request has been handled , status : {}", status);
        });

        if (!this.server.getFsm().isLeader()) {
            closure.run(new Status(RaftError.EPERM, "Not leader"));
        }

        //心跳请求无须序列化存储到本地,对于leader节点来说直接执行即可
        this.server.getFsm().onBeat(serviceInstance);
        closure.run(Status.OK());
    }

    private ServiceDiscoveryOuter.Response response(Status status) {
        ServiceDiscoveryOuter.Response response = ServiceDiscoveryOuter.Response.newBuilder()
                .setCode(status.getCode())
                .setMessage(status.getErrorMsg() == null ? "" : status.getErrorMsg())
                .build();
        return response;

    }

    public static ServiceInstance adaptServiceInstance(ServiceDiscoveryOuter.HeartBeat heartBeat) {
        DefaultServiceInstance instance = new DefaultServiceInstance();
        instance.setId(heartBeat.getId());
        instance.setServiceName(heartBeat.getServiceName());
        instance.setHost(heartBeat.getHost());
        instance.setPort(heartBeat.getPort());
        return instance;
    }

    @Override
    public String interest() {
        return ServiceDiscoveryOuter.HeartBeat.class.getName();
    }
}
