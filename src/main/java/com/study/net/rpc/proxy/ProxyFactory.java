package com.study.net.rpc.proxy;

import com.study.net.rpc.config.RpcInvocation;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Steven
 * @date 2023年01月22日 17:11
 */
public class ProxyFactory {


    public static Object getProxy(Invoker invoker, Class<?> [] interfaces) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvocationServerHandler(invoker));
    }


    public static Invoker getInvoker(Object proxy, Class<?> type) {
        return new Invoker() {
            @Override
            public Object getInterface() {
                return type;
            }

            @Override
            public Object invoke(RpcInvocation rpcInvocation) throws Exception{
                Method method = proxy.getClass().getMethod(rpcInvocation.getMethodName(), rpcInvocation.getParameterizedType());
                Object invoke = method.invoke(proxy, rpcInvocation.getArgs(), rpcInvocation);
                return invoke;
            }
        };
    }
}
