package com.study.net.rpc.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2023年01月23日 11:21
 */
public class ServiceConfig {

    private List<RegistryConfig> registryConfigList;
    private List<ProtocolConfig> protocolConfigList;
    private Object proxy;
    private Class<?> service;


    public void setRegistryConfig(RegistryConfig registryConfig){
        if (registryConfigList==null){
            registryConfigList=new ArrayList<>();
        }
        registryConfigList.add(registryConfig);
    }


    public void setProtocolConfig(ProtocolConfig protocolConfig){
        if (protocolConfigList==null){
            protocolConfigList=new ArrayList<>();
        }
        protocolConfigList.add(protocolConfig);
    }

    public List<RegistryConfig> getRegistryConfigList() {
        return registryConfigList;
    }

    public List<ProtocolConfig> getProtocolConfigList() {
        return protocolConfigList;
    }

    public Object getProxy() {
        return proxy;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public Class<?> getService() {
        return service;
    }

    public void setService(Class<?> service) {
        this.service = service;
    }
}
