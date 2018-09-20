package com.canal.syncdb.bean.common;

import java.io.Serializable;

/**
 * Created by qhe on 2017/7/14.
 */
public class KeywordInfo  implements Serializable {
     private String keyword;
     private Integer updatetime;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Integer updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "{" +
                "keyword='" + keyword + '\'' +
                ", updatetime=" + updatetime +
                '}';
    }
}
