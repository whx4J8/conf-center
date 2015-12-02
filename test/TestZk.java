import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by wanghongxing on 15/11/29.
 */
public class TestZk {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        ZooKeeper zk = connect();
        update(zk,"/zoo1","ads22f1f");

        update(zk,"/zoo2","asdfadsf");
        //create(zk,"/zoo1");
//        update(zk,"/zoo2","adfs");

//        create(zk,"/zoo2");

//        List<String> list = getChilden("/",zk);
//        for(String path:list){
//            System.out.println(path);
//            //delete(zk,"/zoo2/"+ path);
//        }
//
//
//        delete(zk,"/zoo2");

        //testFastUpdate(zk,"/zoo2");
        //update(zk,"/zoo2","h222232");
        //testQueryChild(zk,"/zoo2");
    }

    private static void testFastUpdate(ZooKeeper zk , String path){
        for(int i=0;i<10;i++){
            try {
                update(zk,path,String.valueOf(i));
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }



    public static void testQueryChild(ZooKeeper zk,String path){
        List<String> children = null;
        try {

            children = getChilden(path,zk);
            for(String child:children){
                System.out.println(child);
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static List<String> getChilden(String path, ZooKeeper zk) throws KeeperException, InterruptedException {

        List<String> children = zk.getChildren(path, event -> {
            System.out.println(event.getState());
        });

        return children;
    }

    public static void create(ZooKeeper zkClient,String path) throws KeeperException, InterruptedException {
        System.out.println("创建节点");
        zkClient.create(path, "mydata2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
    }

    public static void update(ZooKeeper zkClient,String path,String data) throws KeeperException, InterruptedException {
        System.out.println("修改节点数据");
        zkClient.setData(path, data.getBytes(), -1);

    }

    public static void delete(ZooKeeper zk,String path){
        System.out.println("删除节点");
        try {
            zk.delete(path, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public static void operate(ZooKeeper zkClient) throws KeeperException, InterruptedException {

        System.out.println("查看创建成功");
        System.out.println(new String(zkClient.getData("/zoo2", true, null)));

        System.out.println("查看修改成功");
        System.out.println(new String(zkClient.getData("/zoo2", true, null)));

        System.out.println("查看节点被删除");
        System.out.println(zkClient.exists("/zoo2", false));

    }

    public static ZooKeeper connect() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 500 * 1000, watchedEvent -> {
            if(watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected){
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        return zk;
    }

}
