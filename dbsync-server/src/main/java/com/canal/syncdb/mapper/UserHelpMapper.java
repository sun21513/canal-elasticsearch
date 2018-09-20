package com.canal.syncdb.mapper;



import java.util.HashMap;
import java.util.List;

public interface UserHelpMapper {
     List<HashMap<String,Object>>  findRoleByUsername(List<String> usernames);
     List<HashMap<String,Object>> findCrmStatusByUsername(List<String> usernames);
     HashMap<String,Integer>  findAuths(List<String> usernames);
     List<HashMap<String,Object>> findPackageInfo(List<String> usernames);
     List<HashMap<String,Object>> findVipMemInfo(List<String> usernames);
     List<HashMap<String,Object>> findRegSource(List<String> usernames);
     List<String> findCrmAuths(List<String> usernames);
     List<String> findPersonAuths(List<String> usernames);
     List<String> findMobileAuths(List<String> usernames);
     List<String> findBankAuths(List<String> usernames);
     List<String> findMailAuths(List<String> usernames);
     List<String> findTeamAuths(List<String> usernames);
     String findSpreadNum(List<String> usernames);
     Integer findVipCategory(Integer vipId);

     Integer findPackageCategory(Integer packageId);

}