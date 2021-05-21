package com.shell.rpc.protocol;

import java.io.Serializable;

public class MsgResponse implements Serializable {

    private Object data; // 成功返回数据

    private String message; // 失败返回错误信息

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
