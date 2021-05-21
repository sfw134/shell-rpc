package com.shell.rpc.provider;

import com.shell.rpc.api.HelloService;
import com.shell.rpc.provider.annotation.RpcService;

@RpcService(serviceInterface = HelloService.class, serviceVersion = "1.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
    }
}
