package com.canal.syncdb.bean.user;

import com.canal.syncdb.constant.SearchConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by qhe on 2017/8/29.
 */
@Document(indexName = SearchConstants.USER, type = SearchConstants.USER_TYPE, shards = SearchConstants.DEFAULT_SHARDS,
        replicas = SearchConstants.DEFAULT_REPLICAS, indexStoreType = SearchConstants.INDEX_STORE_TYPE)
public class User {
    @Id
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String username;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String nickname;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String realname;

    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed, store = true)
    private String  qq;//

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private  String email;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String  truename;//

    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed, store = true)
    private String  sex;//

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String avatar;

    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed, store = true)
    private Long create_time;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String spreadNum;//注册邀请人

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String sourceUrl;//注册来源地址

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String domain;//注册来源域名

    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed, store = true)
    private  Integer user_type;//用户类型（1=>个人用户，2=>企业用户，6=>校园用户）

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private  Integer status; //用户状态 1:已激活（启用） 2:未激活 3:注销 4:禁用

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private  Integer lastLoginTime;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String birthday;

    @Field(type = FieldType.String,store = true, index = FieldIndex.not_analyzed)
    private String mobile; // 手机号

    @Field(type = FieldType.String,store = true, index = FieldIndex.not_analyzed)
    private String company_name; //

    @Field(type = FieldType.String,store = true, index = FieldIndex.not_analyzed)
    private String mix_name; //

    @Field(type = FieldType.String,store = true, index = FieldIndex.not_analyzed)
    private String team_name; //

    @Field(type = FieldType.String,store = true, index = FieldIndex.analyzed)
    private String userRole; //  用户角色，1 => "发需求"* 2 => "接需求"* 3 => "孵化项目"* 4 => "投资项目"* 5 => "渲染服务"* 6 => "选购素材"     * 7 => "出售素材"* 8 => "版权保护"

    @Field(type = FieldType.String,store = true, index = FieldIndex.analyzed)
    private  String vipCategory;

    @Field(type = FieldType.String,store = true, index = FieldIndex.analyzed)
    private  String vipPackage;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String country;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String province;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String city;

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer  personAuth; //个人认证 ,1已认证，0未认证

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer  crmAuth; //企业认证，1已认证，0未认证

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer  crmStatus;//服务商入驻状态， 审核状态（1-草稿；2-入驻待审核；3-驳回 ；5-入驻通过）

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer  channel;//0非渠道用户，1渠道用户



    public Integer getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Integer lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Integer getUser_type() {
        return user_type;
    }

    public void setUser_type(Integer user_type) {
        this.user_type = user_type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Integer getCrmStatus() {
        return crmStatus;
    }

    public void setCrmStatus(Integer crmStatus) {
        this.crmStatus = crmStatus;
    }


    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getVipCategory() {
        return vipCategory;
    }

    public void setVipCategory(String vipCategory) {
        this.vipCategory = vipCategory;
    }

    public String getVipPackage() {
        return vipPackage;
    }

    public void setVipPackage(String vipPackage) {
        this.vipPackage = vipPackage;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getMix_name() {
        return mix_name;
    }

    public void setMix_name(String mix_name) {
        this.mix_name = mix_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public Integer getPersonAuth() {
        return personAuth;
    }

    public void setPersonAuth(Integer personAuth) {
        this.personAuth = personAuth;
    }

    public Integer getCrmAuth() {
        return crmAuth;
    }

    public void setCrmAuth(Integer crmAuth) {
        this.crmAuth = crmAuth;
    }

    public String getSpreadNum() {
        return spreadNum;
    }

    public void setSpreadNum(String spreadNum) {
        this.spreadNum = spreadNum;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
