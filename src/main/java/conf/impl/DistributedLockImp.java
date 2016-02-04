package conf.impl;

import conf.DistributedLock;
import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁接口
 *
 * Created by wanghongxing on 15/12/7.
 */
public class DistributedLockImp implements DistributedLock{

    /**
     * 所有lock的跟节点
     */
    public final static String LOCK_PRE_PATH = "/root_lock";

    /**
     * 默认节点
     */
    public final static String LOCK_DEF_NODE = "/lock";

    /**
     * zk连接
     */
    private ZooKeeper zk;

    /**
     * 锁路径
     */
    private String lockPath;

    /**
     * 当前节点路径
     */
    private String thisPath;

    /**
     * 线程等待
     */
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     *  前一个节点变化的watcher
     */
    private Watcher preNodeWatcher = event -> {

        if(event.getType() == Watcher.Event.EventType.NodeDeleted){//节点删除事件
            countDownLatch.countDown();
        }

    };


    public DistributedLockImp(ZooKeeper zk,String lockPath) {
        this.zk = zk;
        this.lockPath = lockPath;
    }

    @Override
    public void lock() {
        try {
            thisPath = zk.create(LOCK_PRE_PATH + lockPath, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);

            List<String> childNodes = zk.getChildren(LOCK_PRE_PATH + lockPath,false);
            if(childNodes.size() == 1){//获得锁
                return;
            }else{

                Collections.sort(childNodes);
                int index = 0;
                for(String childNode:childNodes){
                    if(childNode.equals(thisPath)){
                        break;
                    }
                    index++;
                }

                if(index == 0){//最小的
                    return;
                }else{//当前节点监听前一个节点
                    zk.exists(childNodes.get(index-1),preNodeWatcher);//节点变化
                    countDownLatch.await();
                    return;
                }
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 删除当前节点会唤醒下个等待的节点
     */
    @Override
    public void unLock() {

        try {
            zk.delete(thisPath, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }


}
