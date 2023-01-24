package com.study.net.agent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Steven
 * @date 2023年01月22日 14:20
 */
public class Invoker implements InvocationHandler {

    private Object  target;

    public Invoker(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(target, args);
        }
        System.out.println("方法执行前。。。");
        Object result = method.invoke(target, args);
        System.out.println("方法执行后。。。");
        return result;
    }
}
