package com.study.net.rpc.remote.netty;

import com.study.net.rpc.remote.handler.Handler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author Steven
 * @date 2023年01月23日 12:48
 */
public class NettyServer implements Server {


    private Handler handler;

    EventLoopGroup bootGroup = new NioEventLoopGroup(10);
    EventLoopGroup workGroup = new NioEventLoopGroup(20);


    public NettyServer(Handler handler) {
        this.handler = handler;
    }


    @Override
    public void start(URI uri) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bootGroup, workGroup)
                    .option(ChannelOption.SO_BACKLOG, 124)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new LoggingHandler())
                    .localAddress(new InetSocketAddress(uri.getHost(), uri.getPort()))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyHandler(handler));
                        }
                    });

            ChannelFuture sync = serverBootstrap.bind().sync();
            sync.channel().closeFuture().sync().addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println(uri.getHost() + ":" + uri.getPort() + " bind successfully.");
                } else {
                    System.err.println(uri.getHost() + ":" + uri.getPort() + " bind failed.");
                }
            });
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }


    }
}
