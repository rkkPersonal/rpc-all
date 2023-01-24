package com.study.net.rpc.remote.netty;

import com.study.net.rpc.remote.handler.Handler;

import java.net.URI;

/**
 * @author Steven
 * @date 2023年01月23日 12:46
 */
public class NettyTransport implements Transport {
    @Override
    public Server start(URI uri, Handler handler) {
        NettyServer nettyServer = new NettyServer(handler);
        nettyServer.start(uri);
        return nettyServer;
    }

    @Override
    public Client connect(URI uri, Handler handler) {
        NettyClient nettyServer = new NettyClient(handler);
        nettyServer.start(uri);
        return nettyServer;
    }
}
