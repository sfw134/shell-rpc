package com.shell.rpc.handler;

import com.shell.rpc.protocol.MsgResponse;
import io.netty.util.concurrent.Promise;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class RpcRequestHolder {

    public static final AtomicLong REQUEST_ID_GEN = new AtomicLong();

    public static final Map<Long, RpcPromise<MsgResponse>> REQUEST_HOLDER_MAP = new HashMap<>();
}
