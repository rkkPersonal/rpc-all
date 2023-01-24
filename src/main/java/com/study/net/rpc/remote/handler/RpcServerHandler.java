package com.study.net.rpc.remote.handler;

import com.alibaba.fastjson.JSONObject;
import com.study.net.rpc.config.RpcInvocation;
import com.study.net.rpc.proxy.Invoker;
import com.study.net.rpc.remote.Response;
import com.study.net.rpc.remote.handler.Handler;
import com.study.net.rpc.serialization.Serialization;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Steven
 * @date 2023年01月23日 22:28
 */
public class RpcServerHandler implements Handler {

    public Invoker invoker;
    public Serialization serialization;

    public RpcServerHandler(Invoker invoker, Serialization serialization) {
        this.invoker = invoker;
        this.serialization = serialization;
    }

    @Override
    public Invoker getInvoker() {
        return invoker;
    }

    @Override
    public Serialization getSerialization() {
        return serialization;
    }

    @Override
    public void onReceive(ChannelHandlerContext ctx, Object msg) {
        RpcInvocation rpcInvocation = (RpcInvocation) msg;
        Response response = null;
        try {
            Object result = invoker.invoke(rpcInvocation);
            if (result == null) {
                response = Response.error(200, "error");
            } else {
                response = Response.ok("success", result);
            }
        } catch (Exception e) {
            response = Response.error(500, "error");
        }
        String re = JSONObject.toJSONString(response);
        ctx.writeAndFlush(re);
    }

}

