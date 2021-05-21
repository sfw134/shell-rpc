package com.shell.rpc.codec;

import com.shell.rpc.protocol.MsgHeader;
import com.shell.rpc.protocol.MsgProtocol;
import com.shell.rpc.serialization.HessianSerialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder<MsgProtocol<Object>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MsgProtocol<Object> msg, ByteBuf out) throws Exception {

        // 请求头处理
        MsgHeader header = msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getVersion());
        out.writeByte(header.getSerialization());
        out.writeByte(header.getMsgType());
        out.writeByte(header.getStatus());
        out.writeLong(header.getRequestId());

        // 请求体处理
        Object body = msg.getBody();
        byte[] bytes = new HessianSerialization().serialize(body);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);

    }
}
