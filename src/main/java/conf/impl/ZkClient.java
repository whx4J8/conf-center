package conf.impl;

import conf.ConfClient;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wanghongxing on 15/11/30.
 */
public class ZkClient implements ConfClient{

    /**
     * 一个client持有一个zookeeper客户端
     */
    private ZooKeeper zooKeeper;

    /**
     * 持有消息回调list
     */
    private List<ZkEventHandler> eventHandlers = new ArrayList<>();

    public ZkClient(String ip,String port) {
        zooKeeper = initZookeeper(ip,port);
    }

    /**
     * 注册节点消息变化
     * @param path
     */
    public void handleNodeData(String path,Watcher watcher){
        ZkEventHandler handler = new ZkEventHandler(path,watcher,zooKeeper);
        eventHandlers.add(handler);
    }

    /**
     * 初始化zookeeper客户端
     * @param ip
     * @param port
     * @return
     */
    private ZooKeeper initZookeeper(String ip,String port){

        ZooKeeper zk = null;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        try {
            zk = new ZooKeeper(ip+":"+port, 500 * 1000, watchedEvent -> {
                if(watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                }
            });

            countDownLatch.await();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return zk;
    }

    /**
     *
     * @param path
     * @return
     */
    public String get(String path){
        byte[] data = null;
        try {
            data = zooKeeper.getData(path,false,null);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new String(data);
    }


}
