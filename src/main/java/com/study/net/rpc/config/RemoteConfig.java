package com.study.net.rpc.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steven
 * @date 2023年01月23日 12:13
 */
public class RemoteConfig {

    private Class<?> loadBalance;

    private Class<?> service;

    private List<RegistryConfig> registryConfigList;
    private List<ProtocolConfig> protocolConfigList;

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRegistryConfig(RegistryConfig redisConfig){
        if (registryConfigList==null){
            registryConfigList=new ArrayList<>();
        }
        registryConfigList.add(redisConfig);
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

    public Class<?> getService() {
        return service;
    }

    public void setService(Class<?> service) {
        this.service = service;
    }

    public  Class<?> getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance( Class<?> loadBalance) {
        this.loadBalance = loadBalance;
    }
}
