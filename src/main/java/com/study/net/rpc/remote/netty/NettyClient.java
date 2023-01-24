package com.study.net.rpc.remote.netty;

import com.study.net.rpc.remote.handler.Handler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author Steven
 * @date 2023年01月24日 1:27
 */
public class NettyClient implements Client{

    private Handler handler;

    private Channel channel;

    EventLoopGroup bootGroup = new NioEventLoopGroup(10);

    public NettyClient(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void start(URI uri) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(bootGroup)
                    .option(ChannelOption.SO_BACKLOG, 124)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .localAddress(new InetSocketAddress(uri.getHost(), uri.getPort()))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new NettyHandler(handler));
                        }
                    });
            // 同步-- 创建连接
            ChannelFuture channelFuture = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
            Channel channel = channelFuture.channel();
            // 优雅停机 -- kill pid -- 响应退出信号
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("我要停机了");
                    synchronized (NettyServer.class) {
                        bootGroup.shutdownGracefully().sync();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public Channel getChannel() {
        return channel;
    }
}
