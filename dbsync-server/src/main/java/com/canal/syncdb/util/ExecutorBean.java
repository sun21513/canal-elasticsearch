package com.canal.syncdb.util;

import java.lang.reflect.Method;

/**
 * Created by qhe on 2017/8/28.
 */
public class ExecutorBean {
    private Object object;
    private Method method;
    private Boolean  isPrimary;

    private  Object[] params;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }
}
