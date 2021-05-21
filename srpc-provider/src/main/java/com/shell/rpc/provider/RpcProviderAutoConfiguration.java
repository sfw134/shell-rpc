package com.shell.rpc.provider;

import com.shell.rpc.registry.ZookeeperServiceRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties
public class RpcProviderAutoConfiguration {

    @Resource
    private RpcProviderProperties rpcProviderProperties;

    @Bean
    public RpcProvider getRpcProvider() throws Exception {
        return new RpcProvider(rpcProviderProperties.getServerPort(), new ZookeeperServiceRegistry(rpcProviderProperties.getRegistryAddr()));
    }

}
