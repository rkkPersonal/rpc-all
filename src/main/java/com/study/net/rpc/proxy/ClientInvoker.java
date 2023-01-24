package com.study.net.rpc.proxy;

import com.study.net.rpc.config.RpcInvocation;
import com.study.net.rpc.remote.Response;
import com.study.net.rpc.remote.handler.RpcClientHandler;
import com.study.net.rpc.remote.netty.Client;
import com.study.net.rpc.serialization.Serialization;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author Steven
 * @date 2023年01月24日 0:48
 */
public class ClientInvoker implements Invoker {


    private Client client;
    private Serialization serialization;

    public ClientInvoker(Client client, Serialization serialization) {
        this.client = client;
        this.serialization = serialization;
    }

    @Override
    public Object getInterface() {
        return null;
    }

    @Override
    public Object invoke(RpcInvocation rpcInvocation) throws Exception {

        // 1. 序列化 rpcInvocation -- 根据服务提供者的配置决定
        byte[] requestBody = serialization.serialize(rpcInvocation);
        // 2. 发起请求 -- rpcInvocation -- 协议数据包 -- 编码
        this.client.getChannel().writeAndFlush(requestBody);
        // 3.另一个线程 接收响应? TODO ? 解码--> handler
        // 实现 等待结果的
        CompletableFuture completableFuture = RpcClientHandler.waitResult(rpcInvocation.getId());
        // future.get 获取结果
        Object result = completableFuture.get(60, TimeUnit.SECONDS);
        Response response = (Response) result;
        if (response.getCode() == 200) {
            return response.getData();
        } else {
            throw new Exception("server error:" + response.getData());
        }
    }
}
