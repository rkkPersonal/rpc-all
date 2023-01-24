package com.study.net.rpc.annotation;


import com.study.net.rpc.spring.RpcBeanDefinitionConfig;
import com.study.net.rpc.spring.RpcBeanProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;
/**
 * @author Steven
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {RpcBeanDefinitionConfig.class, RpcBeanProcessor.class})
public @interface RpcEnable {

    Class<?> loadBalance() default void.class;

}
