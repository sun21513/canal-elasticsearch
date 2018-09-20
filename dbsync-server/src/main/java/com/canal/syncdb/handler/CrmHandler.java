package com.canal.syncdb.handler;

import com.canal.syncdb.annotation.Autowrite;
import com.canal.syncdb.annotation.HandlerMapping;
import com.canal.syncdb.annotation.MethodMapping;
import com.canal.syncdb.exception.SyncdbException;
import com.canal.syncdb.repository.CrmCompanyDAO;
import com.canal.syncdb.bean.common.CategoryInfo;
import com.canal.syncdb.bean.common.EventType;
import com.canal.syncdb.bean.common.KeywordInfo;
import com.canal.syncdb.bean.crm.CrmCompany;
import com.canal.syncdb.bean.crm.CrmWork;
import com.canal.syncdb.constant.SearchConstants;
import com.canal.syncdb.mapper.CrmHelpMapper;
import com.canal.syncdb.mapper.UserHelpMapper;
import com.canal.syncdb.thrift.Column;
import com.canal.syncdb.thrift.RowData;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qhe on 2017/8/25.
 */
@HandlerMapping(index = "crm")
public class CrmHandler extends BaseHandler {
    Logger logger = LoggerFactory.getLogger(CrmHandler.class);

    @Autowrite("crmHelpMapper")
    public CrmHelpMapper crmHelpMapper;

    @Autowrite("userHelpMapper")
    public UserHelpMapper userHelpMapper;

    @Autowrite("crmCompanyDAO")
    public CrmCompanyDAO crmCompanyDAO;

    @Autowrite("elasticsearchTemplate")
    public ElasticsearchTemplate elasticsearchTemplate;

    @MethodMapping(db = "com_vsochina_sp", table = "sp_provider_summary", isPrimary = true)
    public void handPrimaryTable() throws SyncdbException {
        List<CrmCompany> list = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        for (RowData rowData : rowDataList) {
            if (event == EventType.INSERT || event == EventType.UPDATE) {
                try {
                    CrmCompany crmCompany = new CrmCompany();
                    Integer providerId = 0;
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    List<Column> columns = rowData.getColumns();
                    for (Column column : columns) {
                        if ("provider_id".equals(column.getName())) {
                            providerId = Integer.parseInt(column.getValue());
                            crmCompany.setProviderId(providerId);
                            crmCompany.setId(String.valueOf(providerId));
                            ids.add(column.getValue());
                            xb.field("provider_id",providerId);
                        }
                        if ("company_name".equals(column.getName())) {
                            crmCompany.setNickname(column.getValue());
                            xb.field("company_name",column.getValue());
                        }
                        if ("username".equals(column.getName())) { //主键必须携带
                            crmCompany.setUsername(column.getValue());
                            usernames.add(column.getValue());
                            xb.field("username",column.getValue());
                        }
                        if ("company_province".equals(column.getName())) {
                            Integer provinceId = Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue());
                          /*  String provinceName = "";
                            if (provinceId != null) {
                                provinceName = crmHelpMapper.findAreaNameById(provinceId);
                            }*/
                            crmCompany.setProvinceId(provinceId);
                            xb.field("company_province",provinceId);
                            //     crmCompany.setProvinceName(provinceName);
                        }
                        if ("company_city".equals(column.getName())) {
                            Integer cityId = Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue());
                            //    String cityName = crmHelpMapper.findAreaNameById(cityId);
                            crmCompany.setCityId(cityId);
                            xb.field("company_city",cityId);
                            //   crmCompany.setCityName(cityName);
                        }
                        if ("nature".equals(column.getName())) {
                            crmCompany.setUserType(Integer.parseInt(column.getValue().equals("") ? "0" : column.getValue()));
                            xb.field("nature",crmCompany.getUserType());
                        }
                    }
                    xb.endObject();
                    //主表更新操作到此为止
                    if(event ==  EventType.UPDATE){
                        doUpdate(xb,providerId.toString());
                        return;
                    }
                    crmCompany.setRealNameAuth(0);
                    crmCompany.setBankAuth(0);
                    crmCompany.setTeamAuth(0);
                    crmCompany.setMailAuth(0);
                    crmCompany.setMobileAuth(0);
                    crmCompany.setEtpAuth(0);
                    crmCompany.setCategoryOneName("");
                    crmCompany.setCategoryOne("");
                    crmCompany.setCategoryTwo("");
                    crmCompany.setCategoryThree("");
                    crmCompany.setTagId("");
                    crmCompany.setWorkList(new ArrayList<CrmWork>());
                    crmCompany.setUserDecorateLevel(0);
                    crmCompany.setVipName("0");
                    crmCompany.setGroupName("");
                    crmCompany.setVipOldStartTime(0);
                    crmCompany.setVipOldEndTime(0);
                    crmCompany.setVipPushStatus(0);
                    crmCompany.setVipUserId(0);
                    crmCompany.setOrderNum(0);
                    crmCompany.setOrderCash(0.0);
                    crmCompany.setKeywords(new ArrayList<KeywordInfo>());
                    crmCompany.setCategories(new ArrayList<CategoryInfo>());
                    crmCompany.setVipStartAt(0);
                    crmCompany.setVipExpireAt(0);
                    crmCompany.setVipId(0);
                    crmCompany.setVipMemName("");
                    list.add(crmCompany);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SyncdbException("更新主表失败！");
                }
            }
        }
        long begin = System.currentTimeMillis();
        List<String> personAuths = userHelpMapper.findPersonAuths(usernames);
        //   List<String> crmAuths = userHelpMapper.findCrmAuths(usernames);
        List<String> bankAuths = userHelpMapper.findBankAuths(usernames);
        List<String> mobileAuths = userHelpMapper.findMobileAuths(usernames);
        List<String> teamAuths = userHelpMapper.findTeamAuths(usernames);
        List<String> mailAuths = userHelpMapper.findMailAuths(usernames);
        List<HashMap<String, Object>> tagMaps = crmHelpMapper.findTags(ids);
        List<HashMap<String, Object>> statusMaps = crmHelpMapper.findStatus(ids);
        List<HashMap<String, Object>> detailMaps = crmHelpMapper.findDetail(usernames);
        List<HashMap<String, Object>> categoryNameMaps = crmHelpMapper.findCategoryOneName(ids);
        List<HashMap<String, Object>> categoryTypeMaps = crmHelpMapper.findCategoryTypes(ids);
        List<HashMap<String, Object>> vipMems = crmHelpMapper.findVipMemInfo(usernames);
        List<HashMap<String, Object>> tagids = crmHelpMapper.findTagIds(ids);
        List<HashMap<String,Object>>  citys =  crmHelpMapper.findCitys();
        List<CrmWork> crmWorks = crmHelpMapper.findWork(usernames);
        List<HashMap<String, Object>> vipNames = crmHelpMapper.findVipName(usernames);
        List<HashMap<String, Object>> decorateLevelMaps = crmHelpMapper.findDecorateLevel(usernames);
        List<HashMap<String, Object>> keyWordMap = crmHelpMapper.findKeywords(usernames);
        List<HashMap<String, Object>> categoryTopMap = crmHelpMapper.findCats(usernames);
        logger.info("mysql:"+ (System.currentTimeMillis() -begin));
        for (CrmCompany crmCompany : list) {
            String username = crmCompany.getUsername();
            String id = crmCompany.getId();
            for (String authName : personAuths) {
                if (username.equals(authName)) {
                    crmCompany.setRealNameAuth(1);
                }
            }
            for (String authName : bankAuths) {
                if (username.equals(authName)) {
                    crmCompany.setBankAuth(1);
                }
            }
            for (String authName : mobileAuths) {
                if (username.equals(authName)) {
                    crmCompany.setMobileAuth(1);
                }
            }
            for (String authName : mailAuths) {
                if (username.equals(authName)) {
                    crmCompany.setMailAuth(1);
                }
            }
            for (String authName : teamAuths) {
                if (username.equals(authName)) {
                    crmCompany.setTeamAuth(1);
                }
            }
            for (HashMap<String, Object> tagMap : tagMaps) {
                if (id.equals(String.valueOf(tagMap.get("provider_id")))) {
                    crmCompany.setTagName(String.valueOf(tagMap.get("tagName")));
                }
            }
            for (HashMap<String, Object> statusMap : statusMaps) {
                if (id.equals(String.valueOf(statusMap.get("provider_id")))) {
                    crmCompany.setStatus(Integer.parseInt(String.valueOf(statusMap.get("status") == null ? "0" : statusMap.get("status"))));
                    crmCompany.setIsDeleted(Integer.parseInt(String.valueOf(statusMap.get("is_delete"))));
                    crmCompany.setCheckTime(Integer.parseInt(String.valueOf(statusMap.get("check_time"))));
                }
            }
            for (HashMap<String, Object> detailMap : detailMaps) {
                if (username.equals(String.valueOf(detailMap.get("username")))) {
                    crmCompany.setOrderNum(Integer.parseInt(String.valueOf(detailMap.get("hold_total") == null ? "0" : detailMap.get("hold_total"))));
                    crmCompany.setOrderCash(Double.parseDouble(String.valueOf(detailMap.get("hold_amount") == null ? "0" : detailMap.get("hold_amount"))));
                }
            }
            for (HashMap<String, Object> categoryNameMap : categoryNameMaps) {
                if (id.equals(String.valueOf(categoryNameMap.get("provider_id")))) {
                    crmCompany.setCategoryOneName(crmCompany.getCategoryOneName() + categoryNameMap.get("name")+",");
                }
            }
            for (HashMap<String, Object> categoryTypeMap : categoryTypeMaps) {
                if (id.equals(String.valueOf(categoryTypeMap.get("provider_id")))) {
                    crmCompany.setCategoryOne(crmCompany.getCategoryOne() + categoryTypeMap.get("ptype")+",");
                    crmCompany.setCategoryTwo(crmCompany.getCategoryTwo() + categoryTypeMap.get("stype")+",");
                    crmCompany.setCategoryThree(crmCompany.getCategoryThree() + categoryTypeMap.get("ctype")+",");
                }
            }
            for (HashMap<String, Object> tagMap : tagids) {
                if (id.equals(String.valueOf(tagMap.get("provider_id")))) {
                    crmCompany.setTagId(crmCompany.getTagId() + tagMap.get("tag_id")+",");
                }
            }
            for (CrmWork work : crmWorks) {
                if (username.equals(work.getUsername())) {
                    if(crmCompany.getWorkList().size() < 3){
                        crmCompany.getWorkList().add(work);
                    }
                }
            }
            for (HashMap<String, Object> map : decorateLevelMaps) {
                if (username.equals(String.valueOf(map.get("username")))) {
                    crmCompany.setUserDecorateLevel(Integer.parseInt(String.valueOf(map.get("lev"))));
                }
            }
            for (HashMap<String, Object> map : vipNames) {
                if (username.equals(String.valueOf(map.get("username")))) {
                    crmCompany.setVipName(String.valueOf(map.get("group_id")));
                    crmCompany.setGroupName(String.valueOf(map.get("group_name")));
                    crmCompany.setVipOldEndTime((Integer) map.get("start_time"));
                    crmCompany.setVipOldStartTime((Integer) map.get("end_time"));
                }
            }
            for (HashMap<String, Object> map : vipMems) {
                if (username.equals(String.valueOf(map.get("username")))) {
                    crmCompany.setVipExpireAt(Integer.parseInt(String.valueOf(map.get("expiredAt") == null ? "0" : map.get("expiredAt"))));
                    crmCompany.setVipStartAt(Integer.parseInt(String.valueOf(map.get("startAt") == null ? "0" : map.get("startAt"))));
                    crmCompany.setVipUserId(Integer.parseInt(String.valueOf(map.get("id"))));
                    crmCompany.setVipId(Integer.parseInt(String.valueOf(map.get("vipId") == null ? "0" : String.valueOf(map.get("vipId")))));
                     crmCompany.setVipPushStatus(Integer.parseInt(String.valueOf(map.get("push_status") == null ? "0" : map.get("push_status"))));
                    crmCompany.setVipMemName(String.valueOf(map.get("memName")));
                }
            }
            for(HashMap<String, Object> map:keyWordMap){
                if (username.equals(String.valueOf(map.get("username")))) {
                    KeywordInfo keywordInfo = new KeywordInfo();
                    keywordInfo.setKeyword(String.valueOf(map.get("keyword")));
                    keywordInfo.setUpdatetime(Integer.parseInt(String.valueOf(map.get("updatetime"))));
                    crmCompany.getKeywords().add(keywordInfo);
                }
            }
            // //indus_one,indus_two,send_time,username
            for(HashMap<String, Object> map: categoryTopMap){
                if (username.equals(String.valueOf(map.get("username")))) {
                    CategoryInfo categoryInfo = new CategoryInfo();
                    Long indus = null;
                    if (map.get("indus_two") != null) {
                        indus = Long.parseLong(String.valueOf(map.get("indus_two")));
                    } else {
                        indus = Long.parseLong(String.valueOf(map.get("indus_one")));
                    }
                    categoryInfo.setCategory(indus);
                    categoryInfo.setUpdatetime(Long.parseLong(String.valueOf(map.get("send_time"))));
                }
            }
            for(HashMap<String,Object>  map:citys){
                if(crmCompany.getCityId().equals(map.get("id"))){
                    crmCompany.setCityName(map.get("name").toString());
                }
                if(crmCompany.getProvinceId().equals(map.get("id"))){
                    crmCompany.setProvinceName(map.get("name").toString());
                }
            }
        }
        begin =  System.currentTimeMillis();
        crmCompanyDAO.save(list);
        logger.info("es:"+ (System.currentTimeMillis()-begin));
        if (event == EventType.DELETE) {
            for (RowData rowData : rowDataList) { //一次可能多行插入
                List<Column> columns = rowData.getColumns();
                for (Column column : columns) {
                    if ("".equals(column.getName())) { //主键必须携带
                        crmCompanyDAO.delete(column.getValue());
                    }
                }
            }
        }
    }

    @MethodMapping(db = "com_vsochina_sp", table = "sp_provider_tag")
    public void handTags() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            List<Column> columns = rowData.getColumns();
            CrmCompany crmCompany = null;
            String tagid = "";
            String tagName = "";
            for (Column column : columns) {
                if ("provider_id".equals(column.getName())) {
                    crmCompany = crmCompanyDAO.findOne(column.getValue());
                }
                if ("tagid".equals(column.getName())) {
                    tagid = column.getValue();
                    tagName = crmHelpMapper.findTagNameById(Integer.parseInt(tagid));
                }
            }
            String tagIds = "";
            String tagNames = "";
            tagIds = crmCompany.getTagId() == null ? "" : crmCompany.getTagId();
            tagNames = crmCompany.getTagName() ==null?"":crmCompany.getTagName();
            if (event == EventType.DELETE) {
                tagIds = tagid.replaceAll(tagIds, tagid + ",");
                tagNames = tagNames.replaceAll(tagNames, tagName + ",");
            } else {
                tagIds = tagIds + tagid + ",";
                tagNames = tagNames+ tagName +",";
            }
            try {
                XContentBuilder xb = XContentFactory.jsonBuilder();
                xb.startObject(); //
                xb.field("tagName", tagNames);
                xb.field("tagId", tagIds);
                xb.endObject();
                doUpdate(xb, crmCompany.getId());
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新标签失败");
            }

        }
    }

    @MethodMapping(db = "com_vsochina_sp", table = "sp_provider_status")
    public void handStatus() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                XContentBuilder xb = XContentFactory.jsonBuilder();
                boolean flag = false;
                String id = null;
                xb.startObject();
                for (Column column : columns) {
                    if ("status".equals(column.getName()) && column.updated) {
                        xb.field("status", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                    if ("is_delete".equals(column.getName()) && column.updated) {
                        xb.field("isDeleted", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                    if ("check_time".equals(column.getName()) && column.updated) {
                        xb.field("check_time", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                    if ("provider_id".equals(column.getName())) {
                        id = column.getValue();
                    }
                }
                xb.endObject();
                if (flag) {
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新状态表失败！");
            }

        }
    }

    @MethodMapping(db = "com_vsochina_sp", table = "sp_provider_detail")
    public void handDetail() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                XContentBuilder xb = XContentFactory.jsonBuilder();
                String id = null;
                boolean flag = false;
                xb.startObject();
                for (Column column : columns) {
                    if ("hold_amount".equals(column.getName()) && column.updated) {
                        xb.field("orderCash", Double.parseDouble(column.getValue()));
                        flag = true;
                    }
                    if ("hold_total".equals(column.getName()) && column.updated) {
                        xb.field("orderNum", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                    if ("provider_id".equals(column.getName())) {
                        id = column.getValue();
                        flag = true;
                    }
                }
                if (event.equals(EventType.DELETE)) {
                    xb.field("orderCash", 0);
                    xb.field("orderNum", 0);
                    flag = true;
                }
                xb.endObject();
                if (flag) {
                    doUpdate(xb, id);
                }


            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新detail表失败！");
            }
        }
    }


    @MethodMapping(db = "com_vsochina_sp", table = "sp_provider_industry")
    public void handCategory() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                XContentBuilder xb = XContentFactory.jsonBuilder();
                String id = null;
                xb.startObject();
                for (Column column : columns) {
                    if ("provider_id".equals(column.getName())) {
                        HashMap<String, Object> map = crmHelpMapper.findCategory(Integer.parseInt(column.getValue()));
                        xb.field("categoryOne", String.valueOf(map.get("ptype")));
                        xb.field("categoryTwo", String.valueOf(map.get("stype")));
                        xb.field("categoryThree", String.valueOf(map.get("ctype")));
                        xb.field("categoryOneName", String.valueOf(map.get("catNames")));
                        id = column.getValue();
                    }
                }
                xb.endObject();
                doUpdate(xb, id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新detail表失败！");
            }
        }
    }

    @MethodMapping(db = "com_vsochina_maker", table = "tb_space_home_change")
    public void handDecorate() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();

                String id = null;
                Integer decorateLevel = 0;
                boolean flag = false;
                for (Column column : columns) {
                    if ("user_decorate_level".equals(column.getName()) && column.updated) {
                        decorateLevel = Integer.parseInt(column.getValue());
                        flag = true;
                    }
                    if ("username".equals(column.getName())) {
                        String username = column.getValue();
                        id = crmHelpMapper.findIdByUsername(username);
                    }
                }
                if (event.equals(EventType.DELETE)) {
                    flag = true;
                    decorateLevel = 0;
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    xb.field("userDecorateLevel", decorateLevel);
                    xb.endObject();
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新Decorate表失败！");
            }
        }
    }


    @MethodMapping(db = "com_vsochina_maker", table = "tb_crm_company_words")
    public void handKeyword() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                String id = null;
                String username = null;
                boolean flag = false;
                for (Column column : columns) {
                    if ("username".equals(column.getName())) {
                        username = column.getValue();
                        id = crmHelpMapper.findIdByUsername(username);
                    }
                    if ("status".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                    if ("key_word".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                    if ("send_time".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                }
                if (event == EventType.DELETE) {
                    flag = true;
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    List<String> usernames = new ArrayList<>();
                    usernames.add(username);
                    List<HashMap<String,Object>> list = crmHelpMapper.findKeywords(usernames);
                    xb.startObject();
                    xb.startArray("keywords");
                    for (HashMap<String,Object> map : list) {
                        xb.startObject();
                        xb.field("keyword",  map.get("keyword"));
                        xb.field("updatetime",map.get("updatetime"));
                        xb.endObject();
                    }
                    xb.endArray();
                    xb.endObject();
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新Keyword表失败！");
            }
        }
    }

    @MethodMapping(db = "com_vsochina_maker", table = "tb_crm_industry_top")
    public void handCategoryTop() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                String username = null;
                String id = null;
                boolean flag = false;
                for (Column column : columns) {
                    if ("username".equals(column.getName())) {
                        username = column.getValue();
                        id = crmHelpMapper.findIdByUsername(username);
                    }
                    if ("status".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                    if ("indus_one".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                    if ("indus_two".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                    if ("send_time".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                }
                if (event == EventType.DELETE) {
                    flag = true;
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    ArrayList<String>  usernames = new ArrayList<>();
                    usernames.add(username);
                    List<HashMap<String, Object>> list = crmHelpMapper.findCats(usernames);
                    xb.startObject();
                    xb.startArray("categories");
                    for (HashMap<String, Object> map : list) {
                        xb.startObject();
                        int category = (Integer) map.get("indus_one");
                        if (map.get("indus_two") != null && Integer.parseInt(String.valueOf(map.get("indus_two"))) != 0) {
                            category = (Integer) map.get("indus_two");
                        }
                        xb.field("category", category);
                        xb.field("updatetime", map.get("updatetime"));
                        xb.endObject();
                    }
                    xb.endArray();
                    xb.endObject();
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新category表失败！");
            }
        }
    }

    @MethodMapping(db = "com_vsochina_maker", table = "tb_space_cases")
    public void handCase() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                boolean flag = false;
                String username = null;
                String id = null;
                for (Column column : columns) {
                    if ("deleted".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                    if ("is_show".equals(column.getName()) && column.updated) {
                        flag = true;
                    }
                    if ("username".equals(column.getName())) {
                        username = column.getValue();
                        id = crmHelpMapper.findIdByUsername(username);
                    }
                }
                if (event.equals(EventType.DELETE)) {
                    flag = true;
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    List<String> usernames = new ArrayList<>();
                    usernames.add(username);
                    List<CrmWork> list = crmHelpMapper.findWork(usernames);
                    xb.startObject();
                    xb.startArray("workList");
                    for (CrmWork work : list) {
                        xb.startObject();
                        xb.field("id", work.getId());
                        xb.endObject();
                    }
                    xb.endArray();
                    xb.endObject();
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新handCase表失败！");
            }
        }
    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_realname")
    public void handAuthRealname() throws SyncdbException {
        handAuth("auth_status", "realNameAuth", "username");

    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_enterprise")
    public void handAuthEnterprise() throws SyncdbException {
        handAuth("auth_status", "etpAuth", "username");

    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_mobile")
    public void handAuthMobile() throws SyncdbException {
        handAuth("auth_status", "mobileAuth", "username");

    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_email")
    public void handAuthEmail(Integer event, List<RowData> rowDataList) throws SyncdbException {
        handAuth("auth_status", "mailAuth", "username");

    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_bank")
    public void handAuthBank() throws SyncdbException {
        handAuth("auth_status", "bankAuth", "username");

    }

    @MethodMapping(db = "vsoucenter", table = "vso_user_auth_team")
    public void handAuthTeam() throws SyncdbException {
        handAuth("bind_status", "teamAuth", "leader_id");
    }

    @MethodMapping(db = "vsoucenter", table = "vso_vip2_privileges")
    public void handVip0() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                boolean flag = false;
                String id = null;
                Integer vipName = 0;
                for (Column column : columns) {
                    if ("username".equals(column.getName())) {
                        id = crmHelpMapper.findIdByUsername(column.getValue());
                    }
                    if ("group_id".equals(column.getName())) {
                        flag = true;
                        vipName = Integer.parseInt(column.getValue());
                    }
                }
                if (flag) {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject();
                    if (event == EventType.DELETE) {
                        xb.field("vipName", "");
                        xb.field("groupName", "");
                        xb.field("vipOldStartTime", 0);
                        xb.field("vipOldEndTime", 0);
                    } else {
                        xb.field("vipName", vipName);
                    }

                    xb.endObject();
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新handVipName表失败！");
            }
        }
    }

    @MethodMapping(db = "com_vsochina_vip", table = "mem_vip_user")
    public void handMemVip() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                boolean flag = false;
                String id = null;
                XContentBuilder xb = XContentFactory.jsonBuilder();
                xb.startObject();
                for (Column column : columns) {
                    if ("username".equals(column.getName())) {
                        String username = column.getValue();
                        id = crmHelpMapper.findIdByUsername(username);
                    }
                    if ("id".equals(column.getName()) && column.updated) {
                        xb.field("vipUserId", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                    if ("vipId".equals(column.getName()) && column.updated) {
                        xb.field("vipId", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                    if ("startAt".equals(column.getName()) && column.updated) {
                        xb.field("vipStartAt", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                    if ("expiredAt".equals(column.getName()) && column.updated) {
                        xb.field("vipExpireAt", Integer.parseInt(column.getValue()));
                        flag = true;
                    }
                }
                xb.endObject();
                if (flag) {
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新mem_vip_user失败");
            }
        }
    }

    @MethodMapping(db = "com_vsochina_vip", table = "mem_vip_busi_opt_push")
    public void handMemVipBOP() throws SyncdbException {
        for (RowData rowData : rowDataList) {
            try {
                List<Column> columns = rowData.getColumns();
                boolean flag = false;
                String username = null;
                String id = null;
                XContentBuilder xb = XContentFactory.jsonBuilder();
                xb.startObject();
                for (Column column : columns) {
                    if ("vipUserId".equals(column.getName())) {
                        username = crmHelpMapper.findUsernameByVipUserId(Integer.parseInt(column.getValue()));
                        id = crmHelpMapper.findIdByUsername(username);
                    }
                    if ("status".equals(column.getName()) && column.updated) {
                        xb.field("vipPushStatus", column.getValue());
                        flag = true;
                    }
                }
                if (event == EventType.DELETE) {
                    xb.field("vipPushStatus", 0);
                    flag = true;
                }
                xb.endObject();
                if (flag) {
                    doUpdate(xb, id);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SyncdbException("更新mem_vip_busi_opt_push失败");
            }
        }
    }


    public void handAuth(String columnName, String fieldName, String keyName) throws SyncdbException {
        for (RowData rowData : rowDataList) {
            List<Column> columns = rowData.getColumns();
            String username = null;
            Integer status = 0;
            boolean flag = false;
            for (Column column : columns) {
                if (columnName.equals(column.getName()) && column.updated) {
                    Integer auth_status = Integer.parseInt(column.getValue());
                    if (auth_status == 1) {
                        status = 1;
                    }
                    flag = true;
                }
                if (keyName.equals(column.getName())) {
                    username = column.getValue();
                }
            }
            if (flag) {
                try {
                    XContentBuilder xb = XContentFactory.jsonBuilder();
                    xb.startObject(); //
                    xb.field(fieldName, status);
                    xb.endObject();
                    String id = crmHelpMapper.findIdByUsername(username);
                    doUpdate(xb, id);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new SyncdbException("更新认证表失败");
                }

            }
        }
    }

    public void doUpdate(XContentBuilder xb, String id) throws SyncdbException {
        //当主表不存在数据的时候，不进行任何的子表操作
        if (id == null || !crmCompanyDAO.exists(id)) {
            return;
        }
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.doc(xb);
        updateRequest.index(SearchConstants.VSO_CRM_COMPANY);
        updateRequest.type(SearchConstants.NEWRC_TYPE_NAME);
        updateRequest.refresh(Boolean.TRUE);
        updateRequest.id(id);
        updateRequest.retryOnConflict(20);
        try {
            elasticsearchTemplate.getClient().update(updateRequest).get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SyncdbException("更新" + xb.toString() + "失败");
        }
    }

}
