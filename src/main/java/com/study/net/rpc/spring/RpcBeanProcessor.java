package com.study.net.rpc.spring;

import com.study.net.rpc.annotation.RemoteService;
import com.study.net.rpc.annotation.RpcService;
import com.study.net.rpc.config.ProtocolConfig;
import com.study.net.rpc.config.RegistryConfig;
import com.study.net.rpc.config.RemoteConfig;
import com.study.net.rpc.config.ServiceConfig;
import com.study.net.rpc.remote.RpcExecutor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * @author Steven
 * @date 2023年01月23日 11:49
 */
public class RpcBeanProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService annotation = bean.getClass().getDeclaredAnnotation(RpcService.class);
        if (annotation != null) {
            // ToDO This  is a configuration  as server
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setRegistryConfig(applicationContext.getBean(RegistryConfig.class));
            serviceConfig.setProtocolConfig(applicationContext.getBean(ProtocolConfig.class));
            serviceConfig.setProxy(bean);
            serviceConfig.setService(bean.getClass().getInterfaces()[0]);
            // ToDo  start up to expose service waiting for invoke
            RpcExecutor.expose(serviceConfig);
        }
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RemoteService pwcRemoteService = declaredField.getAnnotation(RemoteService.class);
            if (pwcRemoteService == null) {
                continue;
            }
            // ToDo This is a remote service client
            RemoteConfig remoteConfig = new RemoteConfig();
            remoteConfig.setLoadBalance(pwcRemoteService.loadBalance());
            remoteConfig.setService(declaredField.getType());
            // Todo obtain remote proxy bean instance to inject to service
            Object remoteService = RpcExecutor.generateRemoteBean(remoteConfig);
            try {
                declaredField.setAccessible(true);
                declaredField.set(bean, remoteService);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
