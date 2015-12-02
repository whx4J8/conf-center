package conf.impl;

import conf.ConfClient;

/**
 * Created by wanghongxing on 15/12/2.
 */
public class ConfFactory {

    public static ConfClient getClient(String ip,String port){
        return new ZkClient(ip,port);
    }

}
