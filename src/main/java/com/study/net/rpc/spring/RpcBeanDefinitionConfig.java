package com.study.net.rpc.spring;

import com.study.net.rpc.config.ProtocolConfig;
import com.study.net.rpc.config.RegistryConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;

/**
 * @author Steven
 * @date 2023年01月23日 11:50
 */
public class RpcBeanDefinitionConfig implements ImportBeanDefinitionRegistrar {

    private StandardEnvironment environment;

    public RpcBeanDefinitionConfig(Environment environment){
        this.environment= (StandardEnvironment) environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        BeanDefinitionBuilder redisConfigBuilder = BeanDefinitionBuilder.genericBeanDefinition(RegistryConfig.class);
        Field[] redisConfigFields = RegistryConfig.class.getDeclaredFields();
        setValue(redisConfigBuilder, redisConfigFields);
        registry.registerBeanDefinition("redisConfig",redisConfigBuilder.getBeanDefinition());

        BeanDefinitionBuilder protocolConfigBuilder = BeanDefinitionBuilder.genericBeanDefinition(ProtocolConfig.class);
        Field[] protocolConfigFields = ProtocolConfig.class.getDeclaredFields();
        setValue(protocolConfigBuilder, protocolConfigFields);
        registry.registerBeanDefinition("protocolConfig",protocolConfigBuilder.getBeanDefinition());

    }

    private void setValue(BeanDefinitionBuilder builder, Field[] declaredFields) {
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            builder.addPropertyValue(name,environment.getProperty("com.pwc.rpc."+name));
        }
    }
}
