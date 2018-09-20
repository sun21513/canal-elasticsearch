package com.canal.syncdb.bean.demand;

import com.canal.syncdb.constant.SearchConstants;
import com.canal.syncdb.bean.common.CategoryInfo;
import com.canal.syncdb.bean.common.KeywordInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 需求类
 * Created by jinnanwang on 2016/8/5.
 */
@Document(indexName = "vso_req_index", type = "demand_type" ,shards = SearchConstants.DEFAULT_SHARDS,
        replicas = SearchConstants.DEFAULT_REPLICAS, indexStoreType = SearchConstants.INDEX_STORE_TYPE)
public class Demand implements Serializable {

    @Id
    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long taskBn;       // req_demands task_bn

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String username; //发布需求人  req_demands username

    @Field(type = FieldType.String, analyzer = "standard", searchAnalyzer="standard", store = true, index = FieldIndex.analyzed)
    private String title; //标题 req_demand title

    @Field(type = FieldType.String, analyzer = "mmseg", searchAnalyzer="mmseg", store = true, index = FieldIndex.analyzed)
    private String description; //描述 req_demands_detail description

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer indusPid; // 一级分类id  通过二级分类找 keke_witkey_utf8.keke_witkey_industry 还是 com_vsochina_maker.tb_industry

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer indusId; // 二级分类id   req_demand indus_id

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed, store = true)
    private BigDecimal minCash; //最小金额

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed, store = true)
    private BigDecimal maxCash; //最大金额

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer step; //阶段 1 发布 2 投稿 3 选稿 4 公示 5 制作 6 结束

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer status; //需求状态 状态 0系统处理中 1待完善 2 待付款 3 待审核 4 审核未通过 5 冻结 6 审核通过（投标中） 7 选标中 8 已流标（无人投稿） 9 已流标（未选标） 10公示中 11 制作未完成 12 制作期甲方未托管 13 已结束 14 仲裁中 15 已仲裁 16 制作期甲方未付款 17 制作期乙方未交稿 18 选稿期承诺选稿稿件数量不足

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer model; //需求类型 1:比稿 2:招标 3:雇佣  req_demands model 注释的类型和 页面上不一致，页面是悬赏和招标， 悬赏怎么区分单人和计件

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer isMark; //招标类型 1:明标 2:暗标 req_demands is_mark

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer hostedFee; //托管金额  req_demands hosted_percent

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer totalViews = 0; //总浏览量 req_demands_summary total_views

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer totalBids = 0; //总投标数 req_demands_summary total_bids

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer createTime; //创建时间 req_demands create_time

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer endTime; //截止时间

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer top; //置顶服务次数

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer urgent; //加急服务次数

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer isSelect; //是否承诺选稿 1:不承诺 2：承诺

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer hide; //隐标服务次数

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer producer; //制片服务次数

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer oldTask; //是否旧需求数据 0 不是  1 是

    @Field(type = FieldType.Integer, index = FieldIndex.not_analyzed, store = true)
    private Integer isShow; //是否显示 0 不显示 1显示

    @Field(type = FieldType.Double, index = FieldIndex.not_analyzed, store = true)
    private Double taskCash; //是否显示 0 不显示 1显示

    @Field(type = FieldType.Nested, index = FieldIndex.not_analyzed)
    private List<KeywordInfo> keywords = new ArrayList<>();  //用户案例

    @Field(type = FieldType.Nested, index = FieldIndex.not_analyzed)
    private List<CategoryInfo> categories = new ArrayList<>();  //用户案例


    public Long getTaskBn() {
        return taskBn;
    }

    public void setTaskBn(Long taskBn) {
        this.taskBn = taskBn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIndusPid() {
        return indusPid;
    }

    public void setIndusPid(Integer indusPid) {
        this.indusPid = indusPid;
    }

    public Integer getIndusId() {
        return indusId;
    }

    public void setIndusId(Integer indusId) {
        this.indusId = indusId;
    }

    public BigDecimal getMinCash() {
        return minCash;
    }

    public void setMinCash(BigDecimal minCash) {
        this.minCash = minCash;
    }

    public BigDecimal getMaxCash() {
        return maxCash;
    }

    public void setMaxCash(BigDecimal maxCash) {
        this.maxCash = maxCash;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Integer getIsMark() {
        return isMark;
    }

    public void setIsMark(Integer isMark) {
        this.isMark = isMark;
    }

    public Integer getHostedFee() {
        return hostedFee;
    }

    public void setHostedFee(Integer hostedFee) {
        this.hostedFee = hostedFee;
    }

    public Integer getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Integer totalViews) {
        this.totalViews = totalViews;
    }

    public Integer getTotalBids() {
        return totalBids;
    }

    public void setTotalBids(Integer totalBids) {
        this.totalBids = totalBids;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getUrgent() {
        return urgent;
    }

    public void setUrgent(Integer urgent) {
        this.urgent = urgent;
    }

    public Integer getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Integer isSelect) {
        this.isSelect = isSelect;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public Integer getProducer() {
        return producer;
    }

    public void setProducer(Integer producer) {
        this.producer = producer;
    }

    public Integer getOldTask() {
        return oldTask;
    }

    public void setOldTask(Integer oldTask) {
        this.oldTask = oldTask;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Double getTaskCash() {
        return taskCash;
    }

    public void setTaskCash(Double taskCash) {
        this.taskCash = taskCash;
    }

    public List<KeywordInfo> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KeywordInfo> keywords) {
        this.keywords = keywords;
    }

    public List<CategoryInfo> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryInfo> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "taskBn=" + taskBn +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", indusPid=" + indusPid +
                ", indusId=" + indusId +
                ", minCash=" + minCash +
                ", maxCash=" + maxCash +
                ", step=" + step +
                ", status=" + status +
                ", model=" + model +
                ", isMark=" + isMark +
                ", hostedFee=" + hostedFee +
                ", totalViews=" + totalViews +
                ", totalBids=" + totalBids +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                ", top=" + top +
                ", urgent=" + urgent +
                ", isSelect=" + isSelect +
                ", hide=" + hide +
                ", producer=" + producer +
                ", oldTask=" + oldTask +
                ", isShow=" + isShow +
                ", taskCash=" + taskCash +
                '}';
    }
}
