package com.shell.rpc.consumer;

import com.shell.rpc.codec.RpcDecoder;
import com.shell.rpc.codec.RpcEncoder;
import com.shell.rpc.common.ServiceHelper;
import com.shell.rpc.handler.RpcResponseHandler;
import com.shell.rpc.protocol.MsgProtocol;
import com.shell.rpc.protocol.MsgRequest;
import com.shell.rpc.registry.ServiceMetadata;
import com.shell.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RpcConsumer {

    private EventLoopGroup group;
    private Bootstrap bootstrap;

    public RpcConsumer() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(MsgProtocol<MsgRequest> msgProtocol, ServiceRegistry serviceRegistry) throws Exception {

        MsgRequest request = msgProtocol.getBody();
        String serviceKey = ServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        ServiceMetadata serviceMetadata = serviceRegistry.discovery(serviceKey);

        if (serviceMetadata != null) {
            ChannelFuture future = bootstrap.connect(serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort()).sync();
            future.addListener((ChannelFutureListener) args0 -> {
                if (future.isSuccess()) {
                    System.out.println("connect success");
                } else {
                    future.cause().printStackTrace();
                    group.shutdownGracefully();
                }
            });
            future.channel().writeAndFlush(msgProtocol);
        }

    }


}
