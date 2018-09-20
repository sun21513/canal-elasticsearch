package com.canal.syncdb.annotation;

import java.lang.annotation.*;

/**
 * Created by qhe on 2017/8/25.
 */
@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public  @interface Autowrite {
    String value();

}
