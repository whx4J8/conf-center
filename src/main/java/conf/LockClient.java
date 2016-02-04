package conf;

/**
 * Created by wanghongxing on 15/12/9.
 */
public interface LockClient {

    public void lock(String path);

    public void unLock(String path);

}
