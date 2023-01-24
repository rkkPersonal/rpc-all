package com.study.net.rpc.remote.netty;

import com.study.net.rpc.remote.handler.Handler;

import java.net.URI;

/**
 * @author Steven
 */
public interface Transport {

    Server start(URI uri, Handler handler);

    Client connect(URI uri,Handler handler);
}
