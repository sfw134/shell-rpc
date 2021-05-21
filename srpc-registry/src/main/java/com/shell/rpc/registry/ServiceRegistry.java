package com.shell.rpc.registry;

import java.io.IOException;

public interface ServiceRegistry {

    // 服务注册
    void registry(ServiceMetadata serviceMetadata) throws Exception;

    // 服务注销
    void unRegistry(ServiceMetadata serviceMetadata) throws Exception;

    // 服务发现
    ServiceMetadata discovery(String serviceName) throws Exception;

    // 关闭客户端
    void destroy() throws Exception;
}
