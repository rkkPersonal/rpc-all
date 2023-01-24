package com.study.net.rpc.proxy;

import com.study.net.rpc.config.RpcInvocation;

public interface Invoker {

     Object getInterface();

    Object invoke(RpcInvocation rpcInvocation) throws Exception;
}
