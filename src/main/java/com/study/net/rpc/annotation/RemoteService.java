package com.study.net.rpc.annotation;


import java.lang.annotation.*;

/**
 * @author Steven
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RemoteService {

    Class<?> loadBalance() default void.class;

}
