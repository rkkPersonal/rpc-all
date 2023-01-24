package com.study.net.rpc.proxy;

import com.study.net.rpc.config.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.study.net.rpc.common.RpcConstant.*;

/**
 * @author Steven
 * @date 2023年01月22日 17:13
 */
public class InvocationServerHandler implements InvocationHandler {

    private Invoker invoker;

    public InvocationServerHandler(Invoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(invoker, args);
        }
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        // ToDo Do not need to remote call
        if (parameterTypes.length == 0) {
            if (TO_STRING.equals(methodName)) {
                return invoker.toString();
            } else if ($DESTROY.equals(methodName)) {
                return null;
            } else if (HASHCODE.equals(methodName)) {
                return invoker.hashCode();
            }
        } else if (parameterTypes.length == 1 && EQUALS.equals(methodName)) {
            return invoker.equals(args[0]);
        }
        //  method, invoker.getInterface().getName(), args;
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setMethodName(methodName);
        rpcInvocation.setArgs(args);
        rpcInvocation.setParameterizedType(parameterTypes);
        rpcInvocation.setServiceName(method.getDeclaringClass().getName());
        return invoker.invoke(rpcInvocation);

    }
}
