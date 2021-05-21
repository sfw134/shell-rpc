package com.shell.rpc.serialization;

public interface Serialization {

    <T> byte[] serialize(T obj) throws Exception;

    <T> T deserialize(byte[] data, Class<?> T) throws Exception;

}
