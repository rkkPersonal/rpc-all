package com.study.net.rpc.remote.netty;

import com.alibaba.fastjson.JSONObject;

import com.study.net.rpc.remote.Response;
import com.study.net.rpc.remote.handler.Handler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Steven
 * @date 2023年01月23日 22:53
 */
public class NettyHandler extends ChannelDuplexHandler {

    private Handler handler;

    public NettyHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        handler.onReceive(ctx,msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Response response = Response.error(500, "error :" + cause.getMessage());
        String re = JSONObject.toJSONString(response);
        ctx.writeAndFlush(re);
    }
}
