package com.study.net.agent.proxy;

import com.study.net.agent.Invoker;

import java.lang.reflect.Proxy;

/**
 * @author Steven
 * @date 2023年01月22日 14:25
 */
public class ProxyFactory {

    public static Object getProxy(Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new Invoker(obj));
    }
}
