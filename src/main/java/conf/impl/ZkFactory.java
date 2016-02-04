package conf.impl;

import conf.ConfClient;
import conf.LockClient;

/**
 * Created by wanghongxing on 15/12/2.
 */
public class ZkFactory {

    public static ConfClient getConfClient(String ip,String port){
        return new ZkClient(ip,port);
    }

    public static LockClient getLockClient(String ip,String port) { return new ZkClient(ip,port); }

}
