package com.study.net.rpc.registry;

import redis.clients.jedis.Jedis;

import java.net.URI;
import java.util.ArrayList;


/**
 * @author Steven
 * @date 2023年01月24日 19:52
 */
public class RedisService implements RegistryService {

    private static final int TIMEOUT = 15;


    /**
     * 服务提供者相关
     */
    ArrayList<URI> servicesHeartBeat = new ArrayList<>();

    /**
     * rpc-PrcProtocol://127.0.0.1:10088/com.study.dubbo.sms.api.SmsService?transporter=Netty4Transporter&serialization=JsonSerialization
     */
    @Override
    public void register(URI uri) {
        Jedis jedis = new Jedis(uri.getHost(), uri.getPort());
        jedis.auth("123456");
        jedis.setex("rpc-" + uri.toString(), TIMEOUT, String.valueOf(System.currentTimeMillis()));
        jedis.close();
        servicesHeartBeat.add(uri);
    }

    @Override
    public void subscribe(String service, NotifyListener notifyListener) {

    }

    @Override
    public void init(URI address) {

    }
}
