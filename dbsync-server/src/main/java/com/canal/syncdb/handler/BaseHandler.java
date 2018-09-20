package com.canal.syncdb.handler;

import com.canal.syncdb.thrift.Column;
import com.canal.syncdb.thrift.RowData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by qhe on 2017/9/1.
 */
public class BaseHandler {

    public Integer  event;

    public List<RowData> rowDataList;

    public void  deletePrimaryData(ElasticsearchRepository<Object,String> repository, List<RowData> rowDataList,String primaryKey){
        for (RowData rowData : rowDataList) { //一次可能多行插入
            List<Column> columns = rowData.getColumns();
            for (Column column : columns) {
                if (primaryKey.equals(column.getName())) { //主键必须携带
                    repository.delete(column.getValue());
                }
            }
        }
    }



    public Integer getEvent() {
        return event;
    }

    public void setEvent(Integer event) {
        this.event = event;
    }

    public List<RowData> getRowDatvoidaList() {
        return rowDataList;
    }

    public void setRowDataList(List<RowData> rowDataList) {
        this.rowDataList = rowDataList;
    }


}
