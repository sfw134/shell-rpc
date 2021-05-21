package com.shell.rpc.consumer.controller;

import com.shell.rpc.api.HelloService;
import com.shell.rpc.consumer.annotation.RpcReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RpcReference(serviceVersion = "1.0", timeout = 5000)
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello() {
        return helloService.hello("shell-rpc");
    }


}
