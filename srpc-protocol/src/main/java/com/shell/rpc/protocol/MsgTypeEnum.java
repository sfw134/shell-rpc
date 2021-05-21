package com.shell.rpc.protocol;

public enum MsgTypeEnum {
    REQUEST(1),
    RESPONSE(2),
    HEARTBEAT(3);

    private int type;

    public int getType() {
        return type;
    }

    MsgTypeEnum(int type) {
        this.type = type;
    }

    public static MsgTypeEnum getMsgTypeEnum(byte msgType) {
        for (MsgTypeEnum typeEnum : MsgTypeEnum.values()) {
            if (typeEnum.getType() == msgType) {
                return typeEnum;
            }
        }
        return null;
    }
}
