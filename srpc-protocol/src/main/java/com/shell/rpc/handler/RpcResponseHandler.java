package com.shell.rpc.handler;

import com.shell.rpc.protocol.MsgHeader;
import com.shell.rpc.protocol.MsgProtocol;
import com.shell.rpc.protocol.MsgResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcResponseHandler extends SimpleChannelInboundHandler<MsgProtocol<MsgResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtocol<MsgResponse> msg) throws Exception {
        MsgHeader header = msg.getHeader();
        RpcPromise<MsgResponse> rpcPromise = RpcRequestHolder.REQUEST_HOLDER_MAP.get(header.getRequestId());
        if (rpcPromise != null) {
            rpcPromise.getPromise().setSuccess(msg.getBody());
        }
    }
}
