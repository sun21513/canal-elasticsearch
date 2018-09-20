package com.canal.syncdb.constant;


public class SearchConstants {

    public  static  String username;

    public static  final  String VSO_CRM_COMPANY = "crm"; //服务商库索引名称(改进)
    public static  final String NEWRC_TYPE_NAME = "newrc_type"; //服务商库类型名称(改进)

    public   final static String USER = "user_info"; //用户索引

    public static  final  String USER_TYPE = "user_info"; //用户索引


    public static final String INDEX_STORE_TYPE = "niofs"; //索引存储类型

    public static final int DEFAULT_SHARDS = 9;  //默认分片数量

    public static final int DEFAULT_REPLICAS = 1;  //默认副本数量

    public static final String SYNC_ROLE_USER_KEY = "/canal_search/syncdb_update_user_role";  //默认副本数量


}
