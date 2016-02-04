package conf;

/**
 * 分布式锁接口
 *
 * Created by wanghongxing on 15/12/7.
 */
public interface DistributedLock {

    /**
     * 加锁
     */
    public void lock();

    /**
     * 解锁
     */
    public void unLock();

}
