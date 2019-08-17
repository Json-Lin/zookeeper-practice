package com.foo.cache;

import com.foo.client.ZookeeperOperations;
import com.foo.client.config.ZookeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

import java.util.Arrays;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2019/8/16
 */
public class WatcherTest {

    public CuratorFramework getClient() {
        ZookeeperConfiguration conf = new ZookeeperConfiguration("localhost:2181");
        //conf.setNamespace("/dip-elastic-task");
        ZookeeperOperations zookeeperOperations = new ZookeeperOperations(conf);
        zookeeperOperations.init();
        return zookeeperOperations.getCuratorFramework();
    }

    public void addWatcher(CuratorFramework client, String path) throws Exception {
        byte[] data = client.getData().usingWatcher(new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                System.out.println("watcher监听 : " + watchedEvent);
            }
        }).forPath(path);
        System.out.println(Arrays.toString(data));
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        WatcherTest cacheTest = new WatcherTest();
        cacheTest.addWatcher(cacheTest.getClient(), "/zookeeper-test");
    }
}
