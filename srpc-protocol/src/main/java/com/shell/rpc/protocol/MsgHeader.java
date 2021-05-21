package com.shell.rpc.protocol;

import java.io.Serializable;

public class MsgHeader implements Serializable {

    private short magic; // 魔数
    private byte version; // 版本号
    private byte serialization; //序列化算法
    private byte msgType; // 报文类型
    private byte status; // 状态，0-成功，1-失败
    private long requestId; // 请求ID
    private int msgLen; // 报文长度

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getSerialization() {
        return serialization;
    }

    public void setSerialization(byte serialization) {
        this.serialization = serialization;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }
}
