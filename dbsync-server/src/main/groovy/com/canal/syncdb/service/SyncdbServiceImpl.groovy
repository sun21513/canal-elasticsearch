package com.canal.syncdb.service

import com.canal.syncdb.bean.common.EventType
import com.canal.syncdb.handler.BaseHandler
import com.canal.syncdb.thrift.Column
import com.canal.syncdb.thrift.RowData
import com.canal.syncdb.thrift.SyncdbService
import com.canal.syncdb.util.AnnoManageUtil
import com.canal.syncdb.util.ExecutorBean
import org.apache.thrift.TException
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional



/**
 * 业务实现类
 * Created by qhe on 2017/5/4.
 */
@Service
class SyncdbServiceImpl implements SyncdbService.Iface {

    final Logger logger = LoggerFactory.getLogger(getClass())

    /**
     * 分发数据
     * @param db
     * @param table
     * @param event
     * @param columns
     * @throws TException
     */
    @Override
    @Transactional
       void dispatchData(String db, String table, int event, List<RowData> rowDataList,String index) throws TException {
            logger.info("received thrift :" + rowDataList?.size())
            String   url = index+"/"+db+"/"+table
            Map<String,ExecutorBean>  map =  AnnoManageUtil.getHandlers()
            ExecutorBean  executorBean = map.get(url)
            if(executorBean != null){
                executorBean.setParams([] as Object[])
                BaseHandler baseHandler =  (BaseHandler)executorBean.getObject()
                baseHandler.setEvent(event)
                baseHandler.setRowDataList(rowDataList)
               /* if(executorBean.getPrimary()  && EventType.DELETE == event){
                    baseHandler.deletePrimaryData()
                }*/
                executorBean.getMethod().invoke(baseHandler,executorBean.getParams())
            }
    }

    @Override
    @Transactional
    void init( String content){
        JSONObject object  = new JSONObject(content)
        String  db = object.get("db")
        String table = object.get("table")
        String index = object.get("index")
        JSONArray rows = object.get("rows")
        List<RowData>  rowDataList =  new ArrayList<>()
        for(int i=0;i< rows.length();i++){
            RowData rowData  = new RowData(new ArrayList<Column>())
            JSONObject col =  rows.get(i)
            for(String key:col.keys()){
                Column column = new Column()
                column.setName(key)
                column.setValue(col.get(key))
                column.setUpdated(true)
                rowData.getColumns().add(column)
            }
            rowDataList.add(rowData)
        }
        dispatchData(db,table,EventType.INSERT,rowDataList,index)

    }


}
