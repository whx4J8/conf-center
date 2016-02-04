import conf.LockClient;
import conf.impl.ZkFactory;

/**
 * Created by wanghongxing on 15/12/10.
 */
public class TestLock {

    public static int num=1;

    public static class Run implements Runnable{
        @Override
        public void run() {
            LockClient client = ZkFactory.getLockClient("127.0.0.1", "2181");
            client.lock("/lock-num");
            num++;
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client.unLock("/lock-num");
            System.out.println(num);
        }
    }

}
