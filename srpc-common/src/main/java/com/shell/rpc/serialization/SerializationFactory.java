package com.shell.rpc.serialization;

public class SerializationFactory {

    public static Serialization getSerialization(byte serializationType) {

        SerializationTypeEnum typeEnum = SerializationTypeEnum.getSerializationType(serializationType);

        switch (typeEnum) {
            case HESSAIN:
                return new HessianSerialization();
            case JSON:
                return new JsonSerialization();
            default:
                throw new IllegalArgumentException("serialization type is illegal, " + serializationType);
        }
    }
}
