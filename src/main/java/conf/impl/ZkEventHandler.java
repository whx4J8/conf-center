package conf.impl;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;


/**
 *
 * zk消息回调捕捉
 *
 * Created by wanghongxing on 15/12/1.
 */
public class ZkEventHandler {

    /**
     * zk客户端
     */
    private ZooKeeper zooKeeper = null;

    /**
     * 被监听的节点
     */
    private String path;

    /**
     * 代理watcher
     */
    private Watcher proxyWatcher = new Watcher() {

        @Override
        public void process(WatchedEvent event) {
            watcher.process(event);
            registerExists(this, zooKeeper);
        }
    };

    /**
     * 被代理watcher
     */
    private Watcher watcher = null;

    /**
     * watcher一次有效性,使用完重新注册
     * @param watcher
     * @param zk
     */
    private void registerExists(Watcher watcher,ZooKeeper zk){
        try {
            zk.exists(path,watcher);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ZkEventHandler(String path, Watcher watcher,ZooKeeper zooKeeper) {

        this.path = path;
        this.watcher = watcher;
        this.zooKeeper = zooKeeper;

        registerExists(proxyWatcher,zooKeeper);
    }

}
