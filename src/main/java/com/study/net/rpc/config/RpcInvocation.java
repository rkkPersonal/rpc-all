package com.study.net.rpc.config;

/**
 * @author Steven
 * @date 2023年01月23日 11:26
 */

public class RpcInvocation {

    private Long id;

    private String serviceName;

    private String methodName;

    private Object[] args;

    private Class<?>[] parameterizedType;

    public String getServiceName() {
        return serviceName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class<?>[] getParameterizedType() {
        return parameterizedType;
    }

    public void setParameterizedType(Class<?>[] parameterizedType) {
        this.parameterizedType = parameterizedType;
    }
}
