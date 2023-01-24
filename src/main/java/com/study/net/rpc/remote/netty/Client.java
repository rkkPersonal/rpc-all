package com.study.net.rpc.remote.netty;

import io.netty.channel.Channel;

import java.net.URI;

/**
 * @author Steven
 * @date 2023年01月24日 1:27
 */
public interface Client {

    void start(URI uri);

    Channel getChannel();
}
