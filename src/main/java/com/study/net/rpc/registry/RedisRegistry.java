package com.study.net.rpc.registry;

import com.study.net.rpc.util.UrlUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author Steven
 * @date 2023年01月24日 19:52
 */
public class RedisRegistry implements RegistryService {


    private static final int TIMEOUT = 15;
    private static final String SET = "set";
    private static final String EXPIRED = "expired";

    private URI address;
    Jedis jedis;
    private JedisPubSub jedisPubSub;

    /**
     * 服务提供者相关
     */
    ArrayList<URI> servicesHeartBeat = new ArrayList<>();
    Map<String, Set<URI>> localCache = new ConcurrentHashMap<>();
    Map<String, NotifyListener> listenerMap = new ConcurrentHashMap<>();

    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(5);

    @Override
    public void init(URI address) {
        this.address = address;
        jedis = new Jedis(address.getHost(), address.getPort());
        jedis.auth("123456");
        continuationExpired();
        monitorServiceInstance();
    }


    /**
     * rpc-PrcProtocol://127.0.0.1:10088/com.study.dubbo.sms.api.SmsService?transporter=Netty4Transporter&serialization=JsonSerialization
     */
    @Override
    public void register(URI uri) {
        jedis = new Jedis(uri.getHost(), uri.getPort());
        jedis.auth("123456");
        jedis.setex("rpc-" + uri.toString(), TIMEOUT, String.valueOf(System.currentTimeMillis()));
        jedis.close();
        servicesHeartBeat.add(uri);
    }

    @Override
    public void subscribe(String service, NotifyListener notifyListener) {

        try {
            if (localCache.get(service) == null) {
                localCache.putIfAbsent(service, new HashSet<>());
                listenerMap.putIfAbsent(service, notifyListener);

                jedis = new Jedis(address.getHost(), address.getPort());

                Set<String> serviceInstance = jedis.keys("rpc-*" + service + "?*");
                for (String instance : serviceInstance) {
                    URI instanceUri = new URI(instance.replace("rpc-", ""));
                    localCache.get(service).add(instanceUri);
                }
                notifyListener.notify(localCache.get(service));
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private void monitorServiceInstance() {
        executor.execute(() -> {
            jedisPubSub = new JedisPubSub() {
                @Override
                public void onPSubscribe(String pattern, int subscribedChannels) {
                    System.err.println("注册中心开始监听 :" + pattern);
                }

                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    try {
                        URI serviceURI = new URI(channel.replace("__keyspace@0__:rpc-", ""));
                        if (SET.equals(message)) {
                            Set<URI> uris = localCache.get(UrlUtil.getService(serviceURI));
                            if (uris != null) {
                                uris.add(serviceURI);
                            }
                        }

                        if (EXPIRED.equals(message)) {
                            Set<URI> uris = localCache.get(UrlUtil.getService(serviceURI));
                            if (uris != null) {
                                uris.remove(serviceURI);
                            }
                        }

                        if (SET.equals(message) || EXPIRED.equals(message)) {
                            System.err.println("服务实例有变化，开始刷心 。。。");
                            NotifyListener notifyListener = listenerMap.get(UrlUtil.getService(serviceURI));
                            if (Optional.ofNullable(notifyListener).isPresent()) {
                                notifyListener.notify(localCache.get(UrlUtil.getService(serviceURI)));
                            }
                        }
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            };
        });
        jedis.psubscribe(jedisPubSub, "__keyspace@0__:rpc-*");
    }

    private void continuationExpired() {
    /*
     定时延长服务key的过期时间
     */
        executor.scheduleWithFixedDelay(() -> {
            if (servicesHeartBeat.size() > 0) {
                for (URI uri : servicesHeartBeat) {
                    jedis.expire(uri.toString(), TIMEOUT);
                }
            }
        }, 3000, 5000, TimeUnit.MILLISECONDS);
    }
}
