package com.canal.syncdb

import com.canal.syncdb.service.SyncdbServiceImpl
import com.canal.syncdb.thrift.SyncdbService
import com.canal.syncdb.util.AnnoManageUtil
import org.apache.curator.framework.CuratorFramework
import org.apache.zookeeper.CreateMode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.server.TServer
import org.apache.thrift.server.TThreadPoolServer
import org.apache.thrift.transport.TServerSocket
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext


class SyncdbMain {

  /*  static final Logger logger = LoggerFactory.getLogger(getClass())

    static void main(String[] args) {
        final IPADDR = args ? args[0] : InetAddress.getLocalHost().getHostAddress()
        final PORT = args ? args[1].toInteger() : 9876
        final ZK_PATH_PREFIX = "/SyncDB"
        try {
            //启动时先加载handlers
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application.xml")
            AnnoManageUtil.getHandlers(applicationContext)
            //向zk写入服务ip和端口r
            CuratorFramework curatorFramework = applicationContext.getBean(CuratorFramework.class)
            def serverZkUtil = new ServerZkUtil(curatorFramework, ZK_PATH_PREFIX + "/" + IPADDR + ":" + PORT,
                    CreateMode.EPHEMERAL, true, true)
            serverZkUtil.createPath()
            serverZkUtil.watchNode()
            //启动thrift服务
            logger.info("Start vip user thrift server")
            TServerSocket serverTransport = new TServerSocket(PORT)
            TThreadPoolServer.Args ttArgs = new TThreadPoolServer.Args(serverTransport)
            SyncdbService.Processor process = new SyncdbService.Processor(new SyncdbServiceImpl())
            TBinaryProtocol.Factory portFactory = new TBinaryProtocol.Factory(true, true)
            ttArgs.processor(process)
            ttArgs.protocolFactory(portFactory)
            ttArgs.minWorkerThreads(10)
            TServer server = new TThreadPoolServer(ttArgs)
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                void run() {
                    serverZkUtil.close()
                    server.stop()
                }
            }))
            server.serve()

        } catch (Exception e) {
            e.printStackTrace()
        }
    }*/

}
