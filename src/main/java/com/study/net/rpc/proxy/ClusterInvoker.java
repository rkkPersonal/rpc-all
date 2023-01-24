package com.study.net.rpc.proxy;

import com.study.net.rpc.registry.NotifyListener;
import com.study.net.rpc.registry.RegistryService;
import com.study.net.rpc.config.RegistryConfig;
import com.study.net.rpc.config.RemoteConfig;
import com.study.net.rpc.config.RpcInvocation;
import com.study.net.rpc.remote.LoadBalance;
import com.study.net.rpc.remote.protocol.RpcProtocol;
import com.study.net.rpc.util.SpiUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端
 *
 * @author Steven
 * @date 2023年01月24日 13:26
 */
public class ClusterInvoker implements Invoker {

    private static final Map<URI, Invoker> invokerMap = new ConcurrentHashMap<>();

    private RemoteConfig remoteConfig;

    private LoadBalance loadBalance;

    public ClusterInvoker(RemoteConfig remoteConfig) throws URISyntaxException {
        this.remoteConfig = remoteConfig;
        this.loadBalance = SpiUtil.getInstance(remoteConfig.getLoadBalance().getName(), LoadBalance.class);
        String serviceName = remoteConfig.getService().getName();
        List<RegistryConfig> registryConfigs = remoteConfig.getRegistryConfigList();
        for (RegistryConfig registryConfig : registryConfigs) {
            URI regitryUri = new URI(registryConfig.getScheme() + ":\\\\" + registryConfig.getHost() + ":" + registryConfig.getPort());
            RegistryService registryService = SpiUtil.getInstance(regitryUri.getScheme(), RegistryService.class);
            registryService.init(regitryUri);
            registryService.subscribe(serviceName, new NotifyListener() {
                // 当服务有更新的时候，触发【新增、剔除】
                @Override
                public void notify(Set<URI> uris) {
                    System.out.println("更新前的服务invoker信息" + invokerMap);
                    // 剔除 - 创建好的invoker，是不是存在于最小的 实例里面
                    for (URI uri : invokerMap.keySet()) {
                        if (!uris.contains(uri)) {
                            invokerMap.remove(uri);
                        }
                    }

                    // 新增 - 意味新建一个invoker
                    for (URI uri : uris) {
                        if (!invokerMap.containsKey(uri)) {
                            // 意味着有一个服务实例
                            RpcProtocol protocol = SpiUtil.getInstance(uri.getScheme(), RpcProtocol.class);
                            Invoker invoker = protocol.remote(uri); // invoker 代表一个长连接
                            // 保存起来
                            invokerMap.putIfAbsent(uri, invoker);
                        }
                    }
                    System.out.println("更新后的服务invoker信息" + invokerMap);
                }
            });
        }


    }

    @Override
    public Object getInterface() {
        return remoteConfig.getService();
    }


    @Override
    public Object invoke(RpcInvocation rpcInvocation) throws Exception {
        Invoker invoker = loadBalance.select(invokerMap);
        return invoker.invoke(rpcInvocation);
    }
}
