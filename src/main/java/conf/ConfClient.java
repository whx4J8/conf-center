package conf;

/**
 * Created by wanghongxing on 15/11/30.
 */
public interface ConfClient {
    
    void handleNodeData(String node,Watcher watcher);
    
}
