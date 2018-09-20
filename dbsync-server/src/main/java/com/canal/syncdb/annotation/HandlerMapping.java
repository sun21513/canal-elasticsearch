package com.canal.syncdb.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by qhe on 2017/8/25.
 */
@Documented
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Component
public  @interface HandlerMapping {
    String value() default "";
    String index();
}
