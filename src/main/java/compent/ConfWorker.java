package compent;

import conf.impl.ZkClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by wanghongxing on 15/12/1.
 */
@Component
public class ConfWorker implements InitializingBean{

    private ZkClient zkClient = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        zkClient = new ZkClient("127.0.0.1", "2181");
        zkClient.handleNodeData("/zoo2", event -> {
            if (event.getType().equals(Watcher.Event.EventType.NodeDataChanged)) {
                String path = event.getPath();
                zkClient.get(path);
            }
        });

    }

}