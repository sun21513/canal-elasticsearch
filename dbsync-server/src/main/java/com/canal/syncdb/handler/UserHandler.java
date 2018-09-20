package com.canal.syncdb.handler;

import com.canal.syncdb.annotation.Autowrite;
import com.canal.syncdb.annotation.HandlerMapping;
import com.canal.syncdb.annotation.MethodMapping;
import com.canal.syncdb.constant.SearchConstants;
import com.canal.syncdb.exception.SyncdbException;
import com.canal.syncdb.repository.UserDAO;
import com.canal.syncdb.bean.common.EventType;
import com.canal.syncdb.bean.user.User;
import com.canal.syncdb.mapper.CrmHelpMapper;
import com.canal.syncdb.mapper.UserHelpMapper;
import com.canal.syncdb.thrift.Column;
import com.canal.syncdb.thrift.RowData;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by qhe on 2017/8/25.
 */
@HandlerMapping(index = "user_info")
public class UserHandler extends BaseHandler {
    Logger logger = LoggerFactory.getLogger(UserHandler.class);

    @Autowrite("userHelpMapper")
    public UserHelpMapper userHelpMapper;

    @Autowrite("crmHelpMapper")
    public CrmHelpMapper crmHelpMapper;

    @Autowrite("userDAO")
    public UserDAO userDAO;

    @Autowrite("elasticsearchTemplate")
    public ElasticsearchTemplate elasticsearchTemplate;

    @Autowrite("curatorClient")
    public CuratorFramework curatorClient;


    @MethodMapping(db = "vsoucenter", table = "vso_user", isPrimary = true)
    public void handPrimaryTable() throws SyncdbException {
        long begin = System.currentTimeMillis();
        List<User> users = new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        if (event == EventType.DELETE) {
            for (RowData rowData : rowDataList) { //一次可能多行插入
                List<Column> columns = rowData.getColumns();
                for (Column column : columns) {
                    if ("username".equals(column.getName())) { //主键必须携带
                        userDAO.delete(column.getValue());
                    }
                }
            }
            return;
        }
        for (RowData rowData : rowDataList) {
            if (event == EventType.INSERT || event == EventType.UPDATE) {
                try {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    User user = new User();
                    List<Column> columns = rowData.getColumns();
                    for (Column column : columns) {
                        if ("username".equals(column.getName())) {
                            user.setUsername(column.getValue());
                            usernames.add(column.getValue());
                        }
                        if ("nickname".equals(column.getName()) && column.updated) {
                            user.setNickname(column.getValue());
                            xb.field("nickname", column.getValue());
                        }
                        if ("realname".equals(column.getName()) && column.updated) {
                            user.setRealname(column.getValue());
                            xb.field("realname", column.getValue());
                        }
                        if ("truename".equals(column.getName()) && column.updated) {
                            user.setTruename(column.getValue());
                            xb.field("truename", column.getValue());
                        }
                        if ("avatar".equals(column.getName()) && column.updated) {
                            user.setAvatar(column.getValue());
                            xb.field("avatar", column.getValue());
                        }
                        if ("email".equals(column.getName()) && column.updated) {
                            user.setEmail(column.getValue());
                            xb.field("email", column.getValue());
                        }
                        if ("sex".equals(column.getName()) && column.updated) {
                            user.setSex(column.getValue());
                            xb.field("sex", column.getValue());
                        }
                        if ("birthday".equals(column.getName()) && column.updated) {
                            user.setBirthday(column.getValue());
                            xb.field("birthday", column.getValue());
                        }
                        if ("mobile".equals(column.getName()) && column.updated) {
                            user.setMobile(column.getValue());
                            xb.field("mobile", column.getValue());
                        }
                        if ("qq".equals(column.getName()) && column.updated) {
                            user.setQq(column.getValue());
                            xb.field("qq", column.getValue());
                        }
                        if ("country".equals(column.getName()) && column.updated) {
                            user.setCountry(column.getValue());
                            xb.field("country", column.getValue());
                        }
                        if ("province".equals(column.getName()) && column.updated) {
                            user.setProvince(column.getValue());
                            xb.field("province", column.getValue());
                        }
                        if ("city".equals(column.getName()) && column.updated) {
                            user.setCity(column.getValue());
                            xb.field("city", column.getValue());
                        }
                        if ("status".equals(column.getName()) && column.updated) {
                            user.setStatus(Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue()));
                            xb.field("status", column.getValue());
                        }
                        if ("user_type".equals(column.getName()) && column.updated) {
                            user.setUser_type(Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue()));
                            xb.field("user_type", column.getValue());
                        }
                        if ("last_login_time".equals(column.getName()) && column.updated) {
                            user.setLastLoginTime(Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue()));
                            xb.field("lastLoginTime", column.getValue());
                        }
                        if ("spread_num".equals(column.getName()) && column.updated) {
                            user.setSpreadNum(column.getValue());
                            xb.field("spreadNum", column.getValue());
                        }
                    }
                    xb.endObject();
                    //主表更新操作到此为止
                    if (event == EventType.UPDATE) {
                        doUpdate(xb, user.getUsername());
                        return;
                    }
                    //  user.setUserRole("0");
                    //   user.setVipPackage(nu);
                    //   user.setVipCategory("0");
                    user.setCrmAuth(0);
                    user.setPersonAuth(0);
                    user.setCrmStatus(0);
                    if (StringUtils.isNotEmpty(user.getSpreadNum())) {
                        user.setChannel(1);
                    } else {
                        user.setChannel(0);
                    }
                    users.add(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SyncdbException("更新主表失败！");
                }
            }
        }
        List<HashMap<String, Object>> roles = userHelpMapper.findRoleByUsername(usernames);
        List<HashMap<String, Object>> regSource = userHelpMapper.findRegSource(usernames);
        List<HashMap<String, Object>> crmStatuses = userHelpMapper.findCrmStatusByUsername(usernames);
        List<HashMap<String, Object>> packageInfos = userHelpMapper.findPackageInfo(usernames);
        List<HashMap<String, Object>> vipMemInfos = userHelpMapper.findVipMemInfo(usernames);
        List<String> crmAuths = userHelpMapper.findCrmAuths(usernames);
        List<String> personAuths = userHelpMapper.findPersonAuths(usernames);
        for (User user : users) {
            for (HashMap<String, Object> roleMap : roles) {
                String roleUsername = String.valueOf(roleMap.get("username"));
                if (user.getUsername().equals(roleUsername)) {
                    user.setUserRole(user.getUserRole() == null ? "" : user.getUserRole() + roleMap.get("role") + ",");
                }
            }
            for (HashMap<String, Object> regMap : regSource) {
                String regUsername = String.valueOf(regMap.get("username"));
                if (user.getUsername().equals(regUsername)) {
                    String source_url = String.valueOf(regMap.get("source_url"));
                    if (!StringUtils.isEmpty(source_url)) {
                        user.setChannel(1);
                    }
                }
            }
            for (HashMap<String, Object> packageMap : packageInfos) {
                String pkgUsername = String.valueOf(packageMap.get("username"));
                if (user.getUsername().equals(pkgUsername)) {
                    Integer startAt = (Integer) packageMap.get("startAt");
                    Integer expiredAt = (Integer) packageMap.get("expiredAt");
                    Long now = System.currentTimeMillis() / 1000;
                    if (now > startAt && now < expiredAt) {
                        if (user.getVipPackage() == null) {
                            user.setVipPackage("");
                        }
                        String temp = "";
                        temp = user.getVipPackage() + packageMap.get("pkgCategory") + ",";
                        user.setVipPackage(temp);
                    }
                }

            }
            for (HashMap<String, Object> vipMap : vipMemInfos) {
                String vipUsername = String.valueOf(vipMap.get("username"));
                if (user.getUsername().equals(vipUsername)) {
                    //username,status ,startAt,expiredAt,pkgCategory
                    Integer startAt = (Integer) vipMap.get("startAt");
                    Integer expiredAt = (Integer) vipMap.get("expiredAt");
                    Long now = System.currentTimeMillis() / 1000;
                    if (now > startAt && now < expiredAt) {
                        if (user.getVipCategory() == null) {
                            user.setVipCategory("");
                        }
                        String temp = "";
                        temp = user.getVipCategory() + vipMap.get("vipCategory") + ",";
                        user.setVipCategory(temp);
                    }
                }

            }
            for (HashMap<String, Object> crmMap : crmStatuses) {
                String crmUsername = String.valueOf(crmMap.get("username"));
                if (user.getUsername().equals(crmUsername)) {
                    Integer status = Integer.parseInt(String.valueOf(crmMap.get("status") == null ? "0" : crmMap.get("status")));
                    user.setCrmStatus(status);
                }
            }

            for (String uname : crmAuths) {
                if (user.getUsername().equals(uname)) {
                    user.setCrmAuth(1);
                }
            }
            for (String uname : personAuths) {
                if (user.getUsername().equals(uname)) {
                    user.setPersonAuth(1);
                }
            }
        }
        long end = System.currentTimeMillis();
        // begin = System.currentTimeMillis();
        logger.info("mysql:" + String.valueOf(end - begin));
        userDAO.save(users);
        logger.info("es:" + String.valueOf(System.currentTimeMillis() - end));
    }

    @MethodMapping(db = "com_vsochina_vip", table = "mem_vip_user")
    public void handVipMem() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            List<Column> columns = rowData.getColumns();
            try {
                XContentBuilder xb = XContentFactory.jsonBuilder();
                String username = null;
                User user = null;
                String vipCategory = null;
                xb.startObject();
                for (Column column : columns) {
                    if ("vipId".equals(column.getName()) && column.updated) {
                        Integer vipId = Integer.parseInt(column.getValue());
                        vipCategory = String.valueOf(userHelpMapper.findVipCategory(vipId));
                    }
                    if ("username".equals(column.getName())) {
                        user = userDAO.findOne(column.getValue());
                    }
                }
                if(user == null){
                    return;
                }
                if(user.getVipCategory().length()> 100){
                    user.setVipCategory("");
                }
                vipCategory = user.getVipCategory() == null ? "" : user.getVipCategory() + vipCategory;
                xb.field("vipCategory",vipCategory);
                xb.endObject();
                doUpdate(xb, user.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("mem_vip_user fail");
            }
        }
    }

    @MethodMapping(db = "com_vsochina_vip", table = "pkg_package_user")
    public void handPkgUser() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            List<Column> columns = rowData.getColumns();
            try {
                XContentBuilder xb = XContentFactory.jsonBuilder();
                String username = null;
                User user = null;
                String packageCategory = null;
                xb.startObject();
                for (Column column : columns) {
                    if ("packageId".equals(column.getName()) && column.updated) {
                        packageCategory = String.valueOf(userHelpMapper.findPackageCategory(Integer.parseInt(column.getValue())));
                    }
                    if ("username".equals(column.getName())) {
                        username = column.getValue();
                        user = userDAO.findOne(column.getValue());
                    }
                }
                xb.field("vipPackage", user.getVipPackage() == null ? "" : user.getVipPackage() + packageCategory);
                xb.endObject();
                doUpdate(xb, username);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("mem_vip_user fail");
            }


        }
    }

    @MethodMapping(db = "com_vsochina_sp", table = "sp_provider_status")
    public void handCrmStatus() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                Boolean flag = false;
                String username = "";
                Integer crmStatus = 0;
                for (Column column : columns) {
                    if ("status".equals(column.getName()) && column.updated) {
                        flag = true;
                        crmStatus = Integer.parseInt(column.getValue());
                    }
                    if ("provider_id".equals(column.getName())) {
                        username = crmHelpMapper.findUsernameByProviderid(Integer.parseInt(column.getValue()));
                    }
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    xb.field("crmStatus", crmStatus);
                    xb.endObject();
                    doUpdate(xb, username);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("同步服务商状态失败");
            }
        }
    }


    @MethodMapping(db = "vsoucenter", table = "vso_user_role")
    public void handUserRole() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            List<Column> columns = rowData.getColumns();
            User user = null;
            String role = "";
            try {
                for (Column column : columns) {
                    if ("role".equals(column.getName())) {
                        role = column.getValue();
                    }
                    if ("username".equals(column.getName())) {
                        user = userDAO.findOne(column.getValue());
                    }
                }
                if (user != null) {
                    InterProcessMutex lock = new InterProcessMutex(curatorClient, SearchConstants.SYNC_ROLE_USER_KEY + user.getUsername());
                    if (!lock.acquire(100, TimeUnit.SECONDS)) {
                        logger.error("zookeeper key error.  username:" + user.getUsername());
                    }
                    try {
                        String roleStr = user.getUserRole() == null ? "" : user.getUserRole();
                        if (EventType.DELETE == event) {
                            roleStr = roleStr.replace(role + ",", "");
                        } else {
                            roleStr = roleStr + role + ",";
                        }
                        XContentBuilder xb = XContentFactory.jsonBuilder();
                        xb.startObject();
                        xb.field("userRole", roleStr);
                        xb.endObject();
                        doUpdate(xb, user.getUsername());
                    } finally {
                        lock.release();
                        curatorClient.delete().forPath(SearchConstants.SYNC_ROLE_USER_KEY + user.getUsername());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("同步用户角色失败");
            }
        }
    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_reg_source")
    public void handRegSource() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            List<Column> columns = rowData.getColumns();
            Boolean flag = false;
            String username = "";
            Integer channel = 0;
            try {
                for (Column column : columns) {
                    if ("username".equals(column.getName())) {
                        username = column.getValue();
                    }
                    if ("domain".equals(column.getName()) && column.updated) {
                        flag = true;
                        String regSource = column.getValue();
                        if (StringUtils.isNotEmpty(regSource)) {
                            channel = 1;
                        } else {
                            List<String> usernames = new ArrayList<>();
                            usernames.add(username);
                            String spreadNum = userHelpMapper.findSpreadNum(usernames);
                            if (StringUtils.isEmpty(spreadNum)) {
                                channel = 0;
                            } else {
                                channel = 1;
                            }
                        }
                    }
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    xb.field("channel", channel);
                    xb.endObject();
                    doUpdate(xb, username);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("同步注册数据源失败");
            }
        }
    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_realname")
    public void handRealnameAuth() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                Boolean flag = false;
                String username = "";
                Integer auth = 0;
                for (Column column : columns) {
                    if ("auth_status".equals(column.getName()) && column.updated) {
                        flag = true;
                        auth = Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue());
                    }
                    if ("username".equals(column.getName())) {
                        username = column.getValue();
                    }
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    xb.field("personAuth", auth);
                    xb.endObject();
                    doUpdate(xb, username);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("同步服务商状态失败");
            }
        }
    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_enterprise")
    public void handCrmAuth() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                Boolean flag = false;
                String username = "";
                Integer auth = 0;
                for (Column column : columns) {
                    if ("auth_status".equals(column.getName()) && column.updated) {
                        flag = true;
                        auth = Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue());
                    }
                    if ("username".equals(column.getName())) {
                        username = column.getValue();
                    }
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    xb.field("crmAuth", auth);
                    xb.endObject();
                    doUpdate(xb, username);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("同步服务商状态失败");
            }
        }
    }


    private String getVipMem(List<HashMap<String, Object>> vipMemInfo) {
        String vipMem = "";
        for (HashMap<String, Object> map : vipMemInfo) {
            Integer startAt = (Integer) map.get("startAt");
            Integer expiredAt = (Integer) map.get("expiredAt");
            Integer category = (Integer) map.get("vipId");
            Long now = System.currentTimeMillis() / 1000;
            if (now > startAt && now < expiredAt) {
                vipMem = vipMem + category + ",";
            }
        }
        return vipMem;
    }

    private String getPkgType(List<HashMap<String, Object>> packageInfo) {
        String vipPackage = "";//会员包类型
        for (HashMap<String, Object> map : packageInfo) {
            Integer startAt = (Integer) map.get("startAt");
            Integer expiredAt = (Integer) map.get("expiredAt");
            Integer packageId = (Integer) map.get("packageId");
            Long now = System.currentTimeMillis() / 1000;
            if (now > startAt && now < expiredAt) {
                vipPackage = vipPackage + packageId + ",";
            }
        }
        return vipPackage;
    }

    public void doUpdate(XContentBuilder xb, String username) throws SyncdbException {
        //当主表不存在数据的时候，不进行任何的子表操作
        if (username == null || !userDAO.exists(username)) {
            return;
        }
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.doc(xb);
        updateRequest.index(SearchConstants.USER);
        updateRequest.type(SearchConstants.USER_TYPE);
        updateRequest.refresh(Boolean.TRUE);
        updateRequest.id(username);
        updateRequest.retryOnConflict(20);
        try {
            elasticsearchTemplate.getClient().update(updateRequest).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SyncdbException("更新" + xb.toString() + "失败");
        }
    }


}
