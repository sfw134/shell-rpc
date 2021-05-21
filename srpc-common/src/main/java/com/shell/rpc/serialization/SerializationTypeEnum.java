package com.shell.rpc.serialization;

public enum SerializationTypeEnum {

    HESSAIN(1), JSON(2);

    private int type;

    public int getType() {
        return type;
    }

    SerializationTypeEnum(int type) {
        this.type = type;
    }

    public static SerializationTypeEnum getSerializationType(byte serializationType) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.getType() == serializationType) {
                return typeEnum;
            }
        }
        return HESSAIN;
    }
}
