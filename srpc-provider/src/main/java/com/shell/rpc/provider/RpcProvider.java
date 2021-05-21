package com.shell.rpc.provider;

import com.shell.rpc.codec.RpcDecoder;
import com.shell.rpc.codec.RpcEncoder;
import com.shell.rpc.common.ServiceHelper;
import com.shell.rpc.handler.RpcRequestHandler;
import com.shell.rpc.provider.annotation.RpcService;
import com.shell.rpc.registry.ServiceMetadata;
import com.shell.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class RpcProvider implements InitializingBean, BeanPostProcessor {

    private String serviceAddr;
    private int servicePort;
    private ServiceRegistry serviceRegistry;

    private Map<String, Object> rpcServiceMap = new HashMap<>();

    public RpcProvider(int servicePort, ServiceRegistry serviceRegistry) throws Exception {
        this.serviceAddr = InetAddress.getLocalHost().getHostAddress();
        this.servicePort = servicePort;
        this.serviceRegistry = serviceRegistry;
    }

    public void startServer() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new RpcDecoder())
                                    .addLast(new RpcEncoder())
                                    .addLast(new RpcRequestHandler(rpcServiceMap));
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(serviceAddr, servicePort).sync();
            System.out.println("服务启动成功");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        new Thread(() -> {
            try {
                startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
        if (annotation != null) {
            ServiceMetadata serviceMetadata = new ServiceMetadata();
            serviceMetadata.setServiceAddr(this.serviceAddr);
            serviceMetadata.setServicePort(this.servicePort);
            serviceMetadata.setServiceVersion(annotation.serviceVersion());
            serviceMetadata.setServiceName(annotation.serviceInterface().getName());
            try {
                this.serviceRegistry.registry(serviceMetadata);
                this.rpcServiceMap.put(ServiceHelper.buildServiceKey(serviceMetadata.getServiceName(), serviceMetadata.getServiceVersion()), bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
