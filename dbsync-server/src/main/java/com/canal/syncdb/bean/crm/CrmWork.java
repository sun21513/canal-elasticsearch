package com.canal.syncdb.bean.crm;

import java.io.Serializable;

/**
 * 服务商案例类
 * Created by jinnanwang on 2016/7/18.
 */
public class CrmWork implements Serializable {

    private Long id;

    private String username;

    private String work_name;  //作品名称

    private String work_url;  //作品地址

    private String cover_url; //封面地址

    private Integer pic_or_video; //作品类型  作品类型（1=>图片，2=>视频，3=>文本，4=>压缩包）

    public CrmWork() {
    }

    public CrmWork(Long id, String work_name, String work_url, String cover_url, Integer pic_or_video) {
        this.id = id;
        this.work_name = work_name;
        this.work_url = work_url;
        this.cover_url = cover_url;
        this.pic_or_video = pic_or_video;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getWork_url() {
        return work_url;
    }

    public void setWork_url(String work_url) {
        this.work_url = work_url;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public Integer getPic_or_video() {
        return pic_or_video;
    }

    public void setPic_or_video(Integer pic_or_video) {
        this.pic_or_video = pic_or_video;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", work_name='" + work_name + '\'' +
                ", work_url='" + work_url + '\'' +
                ", cover_url='" + cover_url + '\'' +
                ", pic_or_video=" + pic_or_video +
                '}';
    }
}
