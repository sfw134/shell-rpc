package com.shell.rpc.protocol;

import java.io.Serializable;

public class MsgProtocol<T> implements Serializable {

    private MsgHeader header;
    private T body;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
