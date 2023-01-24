package com.study.net.rpc.remote;

import com.study.net.rpc.proxy.Invoker;

import java.net.URI;
import java.util.Map;

/**
 * @author Steven
 */
public interface LoadBalance {

    Invoker select(Map<URI, Invoker> invokerMap);
}
