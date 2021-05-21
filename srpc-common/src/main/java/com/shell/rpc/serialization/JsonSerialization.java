package com.shell.rpc.serialization;

public class JsonSerialization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj) throws Exception {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<?> T) throws Exception {
        return null;
    }
}
