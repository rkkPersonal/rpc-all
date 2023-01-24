package com.study.net.rpc.remote.handler;

import com.study.net.rpc.proxy.Invoker;
import com.study.net.rpc.remote.Response;
import com.study.net.rpc.serialization.Serialization;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Steven
 * @date 2023年01月24日 1:11
 */
public class RpcClientHandler implements Handler{

    private static final Map<Long, CompletableFuture> invokerMap=new ConcurrentHashMap<>();

    public Serialization serialization;

    public RpcClientHandler( Serialization serialization) {
        this.serialization = serialization;
    }


    public static CompletableFuture waitResult(long messageId) {
        CompletableFuture future = new CompletableFuture();
        invokerMap.put(messageId, future);
        return future;
    }


    @Override
    public Invoker getInvoker() {
        return null;
    }

    @Override
    public Serialization getSerialization() {
        return null;
    }

    @Override
    public void onReceive(ChannelHandlerContext ctx, Object msg) {
        Response response = (Response) msg;
        invokerMap.get(response.getRequestId()).complete(response);
        invokerMap.remove(response.getRequestId());
    }
}
