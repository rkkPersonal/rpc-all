package com.study.net.rpc.annotation;


import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author Steven
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RpcService {

    String value() default "";
}
