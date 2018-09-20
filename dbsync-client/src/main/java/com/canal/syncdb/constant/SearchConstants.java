package com.canal.syncdb.constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

public class SearchConstants {
    public static Logger logger = LoggerFactory.getLogger(SearchConstants.class);
    public  static  String zkServers;
    public  static  String destination;
    public  static  String username;
    public  static  String password;
    public  static String canalRegx;
    public static  String index;

    static {
        Properties pro = new Properties();
        try {
            InputStream in = SearchConstants.class.getClassLoader().getResourceAsStream("instance.properties");
           // InputStream in = Object.class.getResourceAsStream("/instance.properties");
            pro.load(in);
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        zkServers = pro.getProperty("zkServers");
        destination = pro.getProperty("destination");
        username = pro.getProperty("username");
        password = pro.getProperty("password");
        canalRegx = pro.getProperty("canalRegx");
    }



    public static void main(String args[]) {
        System.out.println(System.getProperty("user.dir"));
        File directory = new File("");//设定为当前文件夹
        try{
            System.out.println(directory.getCanonicalPath());//获取标准的路径
            System.out.println(directory.getAbsolutePath());//获取绝对路径
        }catch(Exception e){}
    }
}
