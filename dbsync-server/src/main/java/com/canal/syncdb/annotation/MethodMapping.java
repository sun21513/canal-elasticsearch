package com.canal.syncdb.annotation;

import java.lang.annotation.*;

/**
 * Created by qhe on 2017/8/25.
 */
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public  @interface MethodMapping {
    String table();
    String db();
    boolean isPrimary() default  false;
}
