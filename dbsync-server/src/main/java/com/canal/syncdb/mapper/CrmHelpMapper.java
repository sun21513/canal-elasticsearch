package com.canal.syncdb.mapper;


import com.canal.syncdb.bean.crm.CrmCompany;
import com.canal.syncdb.bean.crm.CrmWork;

import java.util.HashMap;
import java.util.List;

public interface CrmHelpMapper {
     String findAreaNameById(Integer id);

     CrmCompany findAuths(String username);

     String findUsernameByProviderid(Integer providerId);

      List<HashMap<String,Object>>  findTags(List<String> ids);

     List<HashMap<String,Object>>   findStatus(List<String> ids);

     HashMap<String,Object> findCategory(Integer providerId);

     HashMap<String,Object>  findVipMem(String username);

     List<HashMap<String,Object>> findDecorateLevel(List<String> usernames);

     List<HashMap<String,Object>> findKeywords(List<String> usernames);

     List<HashMap<String,Object>>  findCats(List<String> usernames);

     List<CrmWork>  findWork(List<String> usernames);

     List<HashMap<String,Object>>  findTagIds(List<String> ids);

     List<HashMap<String,Object>>   findVipName(List<String> usernames);

     String findUsernameByVipUserId(Integer vipUserId);

     Integer findPushStatus(Integer vipUserId);

     String findMemVipName(Integer vipId);

     List<HashMap<String,Object>>   findDetail(List<String> usernames);

     String  findIdByUsername(String username);

     List<HashMap<String,Object>>  findCategoryTypes(List<String> ids);

     List<HashMap<String,Object>> findCategoryOneName(List<String> ids);

     List<String>  getTagNamesByIds(String[] ids);

     List<HashMap<String,Object>>  findCitys();
     List<HashMap<String,Object>>  findVipMemInfo(List<String> usernames);

     String findTagNameById(Integer tagid);

}