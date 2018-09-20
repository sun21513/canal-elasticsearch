package com.canal.syncdb.util;

import com.canal.syncdb.annotation.Autowrite;
import com.canal.syncdb.annotation.HandlerMapping;
import com.canal.syncdb.annotation.MethodMapping;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by qhe on 2017/8/28.
 */
public class AnnoManageUtil {
    final  Logger logger = LoggerFactory.getLogger(getClass());
    private static  Map<String,ExecutorBean>  handlers;
    private static  String packageName = "com.vsochina.syncdb.handler";

    private AnnoManageUtil(){}

    public static  Map<String,ExecutorBean>  getHandlers(ApplicationContext applicationContext){
        if(handlers == null){
            handlers = AnnoManageUtil.getRequestMappingMethod(packageName,applicationContext);
        }
        return  handlers;
    }

    /**
     * 获取指定文件下面的RequestMapping方法保存在mapp中
     *
     * @param packageName
     * @return
     */
    private static Map<String, ExecutorBean> getRequestMappingMethod(String packageName,ApplicationContext applicationContext) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(HandlerMapping.class);

        // 存放url和ExecutorBean的对应关系
        Map<String, ExecutorBean> mapp = new HashMap<String, ExecutorBean>();
        for (Class classes : classesList) {
            //得到该类下面的所有方法
            Object object = null;
            try{
                object = classes.newInstance();
            }catch (Exception e){

            }
            Method[] methods = classes.getDeclaredMethods();
            Field[] fields  =   classes.getDeclaredFields();
            for(Field field:fields){
                Autowrite autowrite =  field.getAnnotation(Autowrite.class);
                if(autowrite == null){
                    continue;
                }
                try {
                    field.set(object,applicationContext.getBean(autowrite.value()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            HandlerMapping  handlerMapping = (HandlerMapping) classes.getAnnotation(HandlerMapping.class);
            for (Method method : methods) {
                //得到该类下面的MethodMapping注解
                MethodMapping methodMapping = method.getAnnotation(MethodMapping.class);
                if (null != methodMapping) {
                    ExecutorBean executorBean = new ExecutorBean();
                    executorBean.setObject(object);
                    executorBean.setMethod(method);
                    if(methodMapping.isPrimary()){
                        executorBean.setPrimary(true);
                    }
                    mapp.put(handlerMapping.index()+"/"+methodMapping.db()+"/"+methodMapping.table(), executorBean);
                }
            }
        }
        return mapp;
    }

    public static void  main(String args[]){
       // System.out.print(getRequestMappingMethod("com.vsochina.syncdb.handler"));
    }
}
