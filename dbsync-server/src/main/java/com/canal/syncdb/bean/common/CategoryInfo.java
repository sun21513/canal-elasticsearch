package com.canal.syncdb.bean.common;

import java.io.Serializable;

/**
 * Created by qhe on 2017/7/14.
 */
public class CategoryInfo  implements Serializable {
    private  Long  category;
    private Long updatetime;

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Long updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "{" +
                "category=" + category +
                ", updatetime=" + updatetime +
                '}';
    }
}
