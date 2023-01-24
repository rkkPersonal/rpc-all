package com.study.net.rpc.remote.handler;

import com.study.net.rpc.proxy.Invoker;
import com.study.net.rpc.serialization.Serialization;
import io.netty.channel.ChannelHandlerContext;

public interface Handler {


     Invoker getInvoker();

     Serialization getSerialization();

    void onReceive(ChannelHandlerContext ctx, Object msg);
}
