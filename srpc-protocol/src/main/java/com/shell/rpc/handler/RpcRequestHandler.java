package com.shell.rpc.handler;

import com.shell.rpc.common.ServiceHelper;
import com.shell.rpc.protocol.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RpcRequestHandler extends SimpleChannelInboundHandler<MsgProtocol<MsgRequest>> {

    private Map<String, Object> rpcServiceMap = new HashMap<>();

    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtocol<MsgRequest> msg) throws Exception {

        MsgHeader header = msg.getHeader();
        header.setMsgType((byte) MsgTypeEnum.RESPONSE.getType());

        MsgRequest request = msg.getBody();
        String serviceKey = ServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = rpcServiceMap.get(serviceKey);
        if (serviceBean == null) {
            throw new IllegalArgumentException("service not exist");
        }

        Class<?> beanClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        Class<?>[] paramTypes = request.getParamTypes();

        MsgProtocol<MsgResponse> msgProtocol = new MsgProtocol<>();
        MsgResponse response = new MsgResponse();
        try {
            Method method = beanClass.getMethod(methodName, paramTypes);
            Object result = method.invoke(serviceBean, parameters);
            header.setStatus((byte) 0);
            msgProtocol.setHeader(header);
            response.setData(result);
            msgProtocol.setBody(response);
        } catch (Exception e) {
            header.setStatus((byte) 1);
            msgProtocol.setHeader(header);
            response.setMessage(e.getMessage());
            msgProtocol.setBody(response);
        }

        ctx.writeAndFlush(msgProtocol);

    }


}
