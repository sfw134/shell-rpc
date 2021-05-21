package com.shell.rpc.serialization;

public class SerializationException extends RuntimeException {

    public SerializationException() {
        super();
    }

    public SerializationException(Throwable e) {
        super(e);
    }

    public SerializationException(String msg, Throwable e) {
        super(msg, e);
    }

}
