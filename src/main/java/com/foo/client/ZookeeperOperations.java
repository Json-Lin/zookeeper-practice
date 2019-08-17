package com.foo.client;

import com.foo.client.config.ZookeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.RetryForever;
import org.apache.zookeeper.CreateMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2019/8/15
 */
public class ZookeeperOperations {

    private CuratorFramework client;
    private ZookeeperConfiguration configuration;
    private final Map<String, TreeCache> caches = new HashMap<>();

    public ZookeeperOperations(ZookeeperConfiguration configuration) {
        this.configuration = configuration;
    }

    public void init() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(configuration.getServerLists())
                .retryPolicy(null != configuration.getRetryPolicy() ? configuration.getRetryPolicy() : new RetryForever(500))
                .namespace(configuration.getNamespace())
                .connectionTimeoutMs(2000);
        client = builder.build();
        client.start();
    }

    public CuratorFramework getCuratorFramework() {
        return client;
    }

    public void create(String path) throws Exception {
        client.create().forPath(path);
    }

    public void create(String path, CreateMode createMode) throws Exception {
        client.create().withMode(createMode).forPath(path);
    }

    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }

    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public boolean isConnected(String path) {
        return client.getZookeeperClient().isConnected();
    }

    public void close() {
        client.close();
    }


}
