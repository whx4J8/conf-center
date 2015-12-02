# conf-center
使用zookeeper做的配置数据中心，将配置的信息通知到各个服务中

@Component
public class ConfWorker implements InitializingBean{  

    private ConfClient confClient = null;

    @Override
    public void afterPropertiesSet() throws Exception {

        confClient = ConfFactory.getClient("127.0.0.1","2181");

        confClient.handleNodeData("/zoo2", (path, message) ->
            System.out.println("get message from conf-center , path " + path + " message : " + message)
        );

        confClient.handleNodeData("/zoo1",(path, message) ->
                System.out.println("get message from conf-center , path " + path + " message : " + message)
        );

    }

}

需要在代码中需要引用一个ConfClient，添加ip，端口，和监听的路径和回调方法

Test包下有测试代码，使用测试代码，可以看到回调借口执行情况

public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        ZooKeeper zk = connect();
        update(zk,"/zoo1","test");
}


解决zk的watcher只能被使用一次的情况，使用代理，watcher每次被调用之后重新注入，具体实现在ZkEventHandler中
