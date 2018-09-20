package com.canal.syncdb.bean.crm;

import com.canal.syncdb.constant.SearchConstants;
import com.canal.syncdb.bean.common.CategoryInfo;
import com.canal.syncdb.bean.common.KeywordInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by-- qhe on 2016/10/31.
 */
@Document(indexName = SearchConstants.VSO_CRM_COMPANY, type = SearchConstants.NEWRC_TYPE_NAME, shards = SearchConstants.DEFAULT_SHARDS,
        replicas = SearchConstants.DEFAULT_REPLICAS, indexStoreType = SearchConstants.INDEX_STORE_TYPE)
public class CrmCompany implements Serializable {

    @Id
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private  String id;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String username;  //用户id

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer providerId;  //用户id

    @Field(type = FieldType.String, analyzer = "ik", searchAnalyzer = "ik", store = true, index = FieldIndex.analyzed)
    private String nickname;  //商户名称（取自用户昵称）

    @Field(type = FieldType.String, analyzer = "ik", searchAnalyzer = "ik", index = FieldIndex.analyzed, store = true)
    private String tagName; //标签名

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String tagId; //标签id

    @Field(type = FieldType.String, store = true, index = FieldIndex.analyzed)
    private String categoryOne; //一级分类id

    @Field(type = FieldType.String, store = true, analyzer = "standard", searchAnalyzer = "standard", index = FieldIndex.analyzed)
    private String categoryOneName; //一级分类名称

    @Field(type = FieldType.String, store = true, index = FieldIndex.analyzed)
    private String categoryTwo; //二级分类id

    @Field(type = FieldType.String,  store = true,index = FieldIndex.analyzed)
    private String categoryThree; //三级分类*/

    @Field(type = FieldType.Integer, store = true, index = FieldIndex.not_analyzed)
    private Integer provinceId;   //所在省份id

    @Field(type = FieldType.String, store = true, index = FieldIndex.not_analyzed)
    private String provinceName;   //所在地区id

    @Field(type = FieldType.Integer, store = true, index = FieldIndex.not_analyzed)
    private Integer cityId;   //所在地区id

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String cityName; //地区名

    @Field(type = FieldType.Integer, store = true, index = FieldIndex.not_analyzed)
    private Integer userType; // 1 个人  2企业 3工作室


    @Field(type = FieldType.Nested, index = FieldIndex.not_analyzed)
    private List<CrmWork> workList = new ArrayList<>();  //用户案例

    @Field(type = FieldType.Nested, index = FieldIndex.not_analyzed)
    private List<KeywordInfo> keywords = new ArrayList<>();  //用户案例

    @Field(type = FieldType.Nested, index = FieldIndex.not_analyzed)
    private List<CategoryInfo> categories = new ArrayList<>();  //用户案例

    @Field(type = FieldType.Integer, store = true, index = FieldIndex.not_analyzed)
    private Integer workCount = 0; //用户作品总数量

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer realNameAuth; //实名认证（个人认证）

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer etpAuth; //企业认证

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer mobileAuth; // 邮箱认证

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer mailAuth; //邮箱认证

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer bankAuth; //银行卡认证

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer teamAuth; //工作室认证

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String vipName; //会员等级，默认0

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String groupName; //会员等级注释

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer vipOldStartTime; //老会员起始时间

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer vipOldEndTime; //老会员结束时间

    public Integer getVipOldStartTime() {
        return vipOldStartTime;
    }

    public void setVipOldStartTime(Integer vipOldStartTime) {
        this.vipOldStartTime = vipOldStartTime;
    }

    public Integer getVipOldEndTime() {
        return vipOldEndTime;
    }

    public void setVipOldEndTime(Integer vipOldEndTime) {
        this.vipOldEndTime = vipOldEndTime;
    }

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer isVip; //1 是会员  0 不是

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer orderNum;  //交易量

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed, store = true)
    private Double orderCash; //交易额

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer reqScore;  //综合积分

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer checkTime;  //入驻审核时间

    @Field(type = FieldType.Integer, store = true, index = FieldIndex.not_analyzed)
    private Integer userDecorateLevel;   //装修等级

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer status; // 审核状态（1-草稿；2-入驻待审核；3-驳回 ；5-入驻通过）

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer isDeleted; //删除状态（1=正常，2=已删除），默认1

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer vipStartAt; // 会员起始时间

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer vipPushStatus; // 0 待推送，1 已推送，2 停止，3 已完成，默认值-1

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer vipExpireAt; // 会员失效时间

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer vipId; // 会员等级

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String vipMemName; // 会员名称

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed)
    private Integer vipUserId; // 会员用户id


    public String getVipMemName() {
        return vipMemName;
    }

    public void setVipMemName(String vipMemName) {
        this.vipMemName = vipMemName;
    }


    public String getCategoryThree() {
        return categoryThree;
    }

    public void setCategoryThree(String categoryThree) {
        this.categoryThree = categoryThree;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCategoryOne() {
        return categoryOne;
    }

    public void setCategoryOne(String categoryOne) {
        this.categoryOne = categoryOne;
    }

    public String getCategoryTwo() {
        return categoryTwo;
    }

    public void setCategoryTwo(String categoryTwo) {
        this.categoryTwo = categoryTwo;
    }

    /*public String getCategoryThree() {
        return categoryThree;
    }

    public void setCategoryThree(String categoryThree) {
        this.categoryThree = categoryThree;
    }
*/
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public List<CrmWork> getWorkList() {
        return workList;
    }

    public void setWorkList(List<CrmWork> workList) {
        this.workList = workList;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public Integer getRealNameAuth() {
        return realNameAuth;
    }

    public void setRealNameAuth(Integer realNameAuth) {
        this.realNameAuth = realNameAuth;
    }

    public Integer getEtpAuth() {
        return etpAuth;
    }

    public void setEtpAuth(Integer etpAuth) {
        this.etpAuth = etpAuth;
    }

    public Integer getMobileAuth() {
        return mobileAuth;
    }

    public void setMobileAuth(Integer mobileAuth) {
        this.mobileAuth = mobileAuth;
    }

    public Integer getMailAuth() {
        return mailAuth;
    }

    public void setMailAuth(Integer mailAuth) {
        this.mailAuth = mailAuth;
    }

    public Integer getBankAuth() {
        return bankAuth;
    }

    public void setBankAuth(Integer bankAuth) {
        this.bankAuth = bankAuth;
    }

    public Integer getTeamAuth() {
        return teamAuth;
    }

    public void setTeamAuth(Integer teamAuth) {
        this.teamAuth = teamAuth;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Double getOrderCash() {
        return orderCash;
    }

    public void setOrderCash(Double orderCash) {
        this.orderCash = orderCash;
    }

    public Integer getReqScore() {
        return reqScore;
    }

    public void setReqScore(Integer reqScore) {
        this.reqScore = reqScore;
    }

    public List<CategoryInfo> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryInfo> categories) {
        this.categories = categories;
    }


    public Integer getIsVip() {
        return isVip;
    }

    public void setIsVip(Integer isVip) {
        this.isVip = isVip;
    }

    public Integer getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Integer checkTime) {
        this.checkTime = checkTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getUserDecorateLevel() {
        return userDecorateLevel;
    }

    public void setUserDecorateLevel(Integer userDecorateLevel) {
        this.userDecorateLevel = userDecorateLevel;
    }

    public String getCategoryOneName() {
        return categoryOneName;
    }

    public void setCategoryOneName(String categoryOneName) {
        this.categoryOneName = categoryOneName;
    }

    public List<KeywordInfo> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordInfo> keywords) {
        this.keywords = keywords;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Integer getVipStartAt() {
        return vipStartAt;
    }

    public void setVipStartAt(Integer vipStartAt) {
        this.vipStartAt = vipStartAt;
    }

    public Integer getVipPushStatus() {
        return vipPushStatus;
    }

    public void setVipPushStatus(Integer vipPushStatus) {
        this.vipPushStatus = vipPushStatus;
    }

    public Integer getVipExpireAt() {
        return vipExpireAt;
    }

    public void setVipExpireAt(Integer vipExpireAt) {
        this.vipExpireAt = vipExpireAt;
    }

    public Integer getVipId() {
        return vipId;
    }

    public void setVipId(Integer vipId) {
        this.vipId = vipId;
    }

    public Integer getVipUserId() {
        return vipUserId;
    }

    public void setVipUserId(Integer vipUserId) {
        this.vipUserId = vipUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
