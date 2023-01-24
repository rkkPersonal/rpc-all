package com.study.net.rpc.remote;

import com.study.net.rpc.config.ProtocolConfig;
import com.study.net.rpc.config.RemoteConfig;
import com.study.net.rpc.config.ServiceConfig;
import com.study.net.rpc.proxy.ClientInvoker;
import com.study.net.rpc.proxy.ClusterInvoker;
import com.study.net.rpc.proxy.Invoker;
import com.study.net.rpc.proxy.ProxyFactory;
import com.study.net.rpc.remote.protocol.RpcProtocol;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Steven
 * @date 2023年01月23日 12:09
 */
public class RpcExecutor {


    public static void expose(ServiceConfig serviceConfig) {
        Invoker invoker = ProxyFactory.getInvoker(serviceConfig.getProxy(), serviceConfig.getService());
        List<ProtocolConfig> protocolConfigList = serviceConfig.getProtocolConfigList();
        try {
            for (ProtocolConfig protocolConfig : protocolConfigList) {
                String serialization = protocolConfig.getSerialization();
                StringBuilder builder = new StringBuilder();
                builder.append(protocolConfig.getProtocol() + ":\\\\")
                        .append(protocolConfig.getHost() + ":" + protocolConfig.getPort() + "\\" + protocolConfig.getServiceName())
                        .append("?" + "serialization=" + serialization + "&")
                        .append("transport=" + protocolConfig.getTransport());
                RpcProtocol.start(new URI(builder.toString()), invoker);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public static Object generateRemoteBean(RemoteConfig remoteConfig) {
        Object proxyInstance = null;
        try {
            Class<?> loadBalance = remoteConfig.getLoadBalance();
            Class<?> service = remoteConfig.getService();
            ClusterInvoker clientInvoker = new ClusterInvoker(remoteConfig);
            proxyInstance = ProxyFactory.getProxy(clientInvoker, new Class[]{service});
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return proxyInstance;
    }
}
