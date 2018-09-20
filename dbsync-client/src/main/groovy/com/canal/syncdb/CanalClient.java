package com.canal.syncdb;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.Message;
import com.canal.syncdb.constant.SearchConstants;
import com.canal.syncdb.exception.SyncdbException;
import com.canal.syncdb.thrift.RowData;
import com.canal.syncdb.thrift.SyncdbService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by qhe on 2017/7/4.
 */
@Component
public class CanalClient {
    public static Logger logger = LoggerFactory.getLogger(CanalClient.class);

    public static void main(String args[]) throws  Exception{
        ApplicationContext  context = new ClassPathXmlApplicationContext("application.xml");
        while(true){
            System.out.println(11);
            Thread.sleep(1000);
        }
     /*   CuratorFramework curatorFramework = (CuratorFramework)  context.getBean("curatorClient");
    //    clientZkUtil = new ClientZkUtil(curatorFramework,"/SyncDB" , true);
        CanalConnector connector =  CanalConnectors.newClusterConnector(SearchConstants.zkServers,SearchConstants.destination, SearchConstants.username, SearchConstants.password);
        int batchSize = 1000;
        int emptyCount = 0;
        try {
            connector.connect();
            connector.subscribe(SearchConstants.canalRegx);
            // connector.subscribe(".*\\..*");
            connector.rollback(); //初次连接定位metaid
            while (true) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    //  System.out.println("empty:" + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    try {
                        parse(message.getEntries());
                        connector.ack(batchId); // 提交确认
                    }catch (Exception e){
                        e.printStackTrace();
                        connector.rollback(batchId);// 处理失败, 回滚数据
                    }
                }
            }
        } finally {
            connector.disconnect();
        }*/
    }

    private static void parse(List<Entry> entrys) throws SyncdbException,TException,Exception{
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChange = null;
            try {
                rowChange = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }
            try {
                EventType eventType = rowChange.getEventType();
              //  List ipPort = clientZkUtil.getServerInfo();
                TTransport transport = new TSocket("127.0.0.1", 3333);
                TProtocol protocol = new TBinaryProtocol(transport);
                SyncdbService.Client client =new SyncdbService.Client(protocol);
                transport.open();
                List<RowData>  list =   parseRowChange(rowChange,eventType);
                logger.info("db:"+entry.getHeader().getSchemaName());
                logger.info("table:"+entry.getHeader().getTableName());
                logger.info("data:"+list.toString());
                client.dispatchData(entry.getHeader().getSchemaName(),entry.getHeader().getTableName(),eventType.getNumber(),list,SearchConstants.index);//调用ＲＰＣ服务
                transport.close();
            }catch (Exception e){
                e.printStackTrace();
                throw  new SyncdbException("同步失败！");
            }

        }
    }


    private  static List<RowData>  parseRowChange(RowChange rowChange, EventType eventType){
        List<RowData>  list  =  new ArrayList<>();
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            com.canal.syncdb.thrift.RowData rd = new com.canal.syncdb.thrift.RowData();
            List<Column> columns = null;
            List<com.canal.syncdb.thrift.Column> columnList = new ArrayList<>();
            if (eventType == EventType.DELETE) {
                columns =  rowData.getBeforeColumnsList();
            } else{
                columns =  rowData.getAfterColumnsList();
            }
            for(Column column:columns){
                com.canal.syncdb.thrift.Column col = new com.canal.syncdb.thrift.Column();
                col.setName(column.getName());
                col.setValue(column.getValue());
                col.setUpdated(column.getUpdated());
                columnList.add(col);
            }
            rd.setColumns(columnList);
            list.add(rd);
        }
        return  list;
    }

    private static void printColumn(List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
