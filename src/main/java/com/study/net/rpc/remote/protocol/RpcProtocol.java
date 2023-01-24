package com.study.net.rpc.remote.protocol;

import com.study.net.rpc.proxy.ClientInvoker;
import com.study.net.rpc.proxy.Invoker;
import com.study.net.rpc.remote.handler.RpcClientHandler;
import com.study.net.rpc.remote.handler.RpcServerHandler;
import com.study.net.rpc.remote.netty.Client;
import com.study.net.rpc.remote.netty.Transport;
import com.study.net.rpc.serialization.Serialization;
import com.study.net.rpc.util.SpiUtil;
import com.study.net.rpc.util.UrlUtil;

import java.net.URI;

/**
 * @author Steven
 * @date 2023年01月23日 22:24
 */
public class RpcProtocol {

    public static void start(URI uri, Invoker invoker) {
        // 找到序列化
        String serializationName = UrlUtil.getParam("serialization", uri);
        Serialization serialization = SpiUtil.getInstance(serializationName, Serialization.class);

        // 1. 编解码器
        /*TrpcCodec trpcCodec = new TrpcCodec();
        trpcCodec.setDecodeType(RpcInvocation.class);
        trpcCodec.setSerialization(serialization);*/
        // 2. 收到请求处理器
        RpcServerHandler rpcServerHandler = new RpcServerHandler(invoker, serialization);
        // 3. 底层网络框架
        String transporterName = UrlUtil.getParam("transporter", uri);

        Transport launcher = SpiUtil.getInstance(transporterName, Transport.class);
        launcher.start(uri, rpcServerHandler);
    }

    public  Invoker remote(URI uri) {
        // 找到序列化
        String serializationName = UrlUtil.getParam("serialization", uri);
        Serialization serialization = SpiUtil.getInstance(serializationName, Serialization.class);

        // 1. 编解码器
        /*TrpcCodec trpcCodec = new TrpcCodec();
        trpcCodec.setDecodeType(RpcInvocation.class);
        trpcCodec.setSerialization(serialization);*/
        // 2. 收到请求处理器
        RpcClientHandler rpcServerHandler = new RpcClientHandler(serialization);
        // 3. 底层网络框架
        String transporterName = UrlUtil.getParam("transporter", uri);

        Transport launcher = SpiUtil.getInstance(transporterName, Transport.class);
        Client connect = launcher.connect(uri, rpcServerHandler);

        ClientInvoker clientInvoker = new ClientInvoker(connect,serialization);
        
        return clientInvoker;

    }
}
