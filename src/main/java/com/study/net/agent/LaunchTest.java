package com.study.net.agent;

import com.study.net.agent.proxy.ProxyFactory;
import com.study.net.agent.service.IphoneService;
import com.study.net.agent.service.IphoneServiceImpl;

/**
 * @author Steven
 * @date 2023年01月22日 14:24
 */
public class LaunchTest {

    public static void main(String[] args) {
        IphoneService iphoneService = new IphoneServiceImpl();
        IphoneService proxy = (IphoneService) ProxyFactory.getProxy(iphoneService);
        System.err.println(proxy);
        proxy.product("手机壳");
    }
}
