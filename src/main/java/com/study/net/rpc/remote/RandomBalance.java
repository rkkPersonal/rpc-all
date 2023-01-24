package com.study.net.rpc.remote;

import com.study.net.rpc.proxy.Invoker;

import java.net.URI;
import java.util.Map;
import java.util.Random;

/**
 * @author Steven
 * @date 2023年01月24日 20:45
 */
public class RandomBalance implements LoadBalance{

    @Override
    public Invoker select(Map<URI, Invoker> invokerMap) {
        int i = new Random().nextInt(invokerMap.size());
        Invoker invoker = invokerMap.get(i );
        return invoker;
    }
}
