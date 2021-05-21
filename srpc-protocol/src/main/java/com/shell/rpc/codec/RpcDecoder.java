package com.shell.rpc.codec;

import com.shell.rpc.common.RpcConstants;
import com.shell.rpc.protocol.*;
import com.shell.rpc.serialization.Serialization;
import com.shell.rpc.serialization.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < 18) {
            return;
        }

        in.markReaderIndex();
        short magic = in.readShort();
        if (magic != RpcConstants.MAGIC) {
            in.resetReaderIndex();
            return;
        }

        byte version = in.readByte();
        byte serializationType = in.readByte();
        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();
        int msgLen = in.readInt();

        if (in.readableBytes() < msgLen) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[msgLen];
        in.readBytes(data);

        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serializationType);
        header.setMsgType(msgType);
        header.setStatus(status);
        header.setRequestId(requestId);
        header.setMsgLen(msgLen);

        Serialization serialization = SerializationFactory.getSerialization(serializationType);
        MsgTypeEnum msgTypeEnum = MsgTypeEnum.getMsgTypeEnum(msgType);
        switch (msgTypeEnum) {
            case REQUEST:
                MsgRequest request = serialization.deserialize(data, MsgRequest.class);
                if (request != null) {
                    MsgProtocol<MsgRequest> msgProtocol = new MsgProtocol<>();
                    msgProtocol.setHeader(header);
                    msgProtocol.setBody(request);
                    out.add(msgProtocol);
                }
                break;
            case RESPONSE:
                MsgResponse response = serialization.deserialize(data, MsgResponse.class);
                if (response != null) {
                    MsgProtocol<MsgResponse> msgProtocol = new MsgProtocol<>();
                    msgProtocol.setHeader(header);
                    msgProtocol.setBody(response);
                    out.add(msgProtocol);
                }
                break;
            case HEARTBEAT:
                // TODO
                break;
        }


    }
}
