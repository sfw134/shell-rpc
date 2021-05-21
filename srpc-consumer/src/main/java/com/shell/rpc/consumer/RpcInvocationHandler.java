package com.shell.rpc.consumer;

import com.shell.rpc.common.RpcConstants;
import com.shell.rpc.handler.RpcPromise;
import com.shell.rpc.handler.RpcRequestHolder;
import com.shell.rpc.protocol.*;
import com.shell.rpc.registry.ZookeeperServiceRegistry;
import com.shell.rpc.serialization.SerializationTypeEnum;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class RpcInvocationHandler implements InvocationHandler {

    private String registryAddr;
    private String serviceVersion; // 服务版本号
    private long timeout;


    public RpcInvocationHandler(String serviceVersion, long timeout, String registryAddr) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryAddr = registryAddr;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        MsgProtocol<MsgRequest> msgProtocol = new MsgProtocol<>();
        MsgHeader header = new MsgHeader();
        header.setMagic(RpcConstants.MAGIC);
        header.setVersion(RpcConstants.VERSION);
        header.setSerialization((byte) SerializationTypeEnum.HESSAIN.getType());
        header.setMsgType((byte) MsgTypeEnum.REQUEST.getType());
        header.setStatus((byte) 1);
        long requestId = RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setRequestId(requestId);
        MsgRequest request = new MsgRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);
        request.setParamTypes(method.getParameterTypes());
        request.setServiceVersion(this.serviceVersion);
        msgProtocol.setHeader(header);
        msgProtocol.setBody(request);

        RpcPromise<MsgResponse> rpcPromise = new RpcPromise<>(new DefaultPromise(new DefaultEventLoop()), this.timeout);
        RpcRequestHolder.REQUEST_HOLDER_MAP.put(requestId, rpcPromise);

        RpcConsumer consumer = new RpcConsumer();
        ZookeeperServiceRegistry zookeeperServiceRegistry = new ZookeeperServiceRegistry(this.registryAddr);
        consumer.sendRequest(msgProtocol, zookeeperServiceRegistry);

        return rpcPromise.getPromise().get(this.timeout, TimeUnit.MILLISECONDS).getData();
    }
}
