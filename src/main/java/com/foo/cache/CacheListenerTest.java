package com.foo.cache;

import com.foo.client.ZookeeperOperations;
import com.foo.client.config.ZookeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.TreeCache;

import java.util.Arrays;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2019/8/16
 */
public class CacheListenerTest {

    public CuratorFramework getClient() {
        ZookeeperConfiguration conf = new ZookeeperConfiguration("localhost:2181");
        ZookeeperOperations zookeeperOperations = new ZookeeperOperations(conf);
        zookeeperOperations.init();
        return zookeeperOperations.getCuratorFramework();
    }

    /**
     * 对znode的子节点进行监控，当子节点有更改时触发监控
     *
     * @param client
     * @param path
     * @throws Exception
     */
    public void addPathCache(CuratorFramework client, String path) throws Exception {
        PathChildrenCache pathCache = new PathChildrenCache(client, path, true);
        pathCache.getListenable().addListener((curatorFramework, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    System.out.println(event.getData().getPath() + " " + event.getType() + " " + Arrays.toString(event.getData().getData()));
                    break;
                case CHILD_REMOVED:
                    System.out.println(event.getData().getPath() + " " + event.getType() + " " + Arrays.toString(event.getData().getData()));
                    break;
                case CHILD_UPDATED:
                    System.out.println(event.getData().getPath() + " " + event.getType() + " " + Arrays.toString(event.getData().getData()));
                    break;
                default:
                    break;
            }
        });
        pathCache.start();
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 对zode节点进行监控，当数据更改时触发
     *
     * @param client
     * @param path
     * @throws Exception
     */
    public void addNodeCache(CuratorFramework client, String path) throws Exception {
        NodeCache nodeCache = new NodeCache(client, path);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println(nodeCache.getCurrentData().getPath() + " " + Arrays.toString(nodeCache.getCurrentData().getData()));
            }
        });
        nodeCache.start();
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 监控znode和childre,同时具备pathcache和nodecache的功能
     *
     * @param client
     * @param path
     * @throws Exception
     */
    public void addTreeCache(CuratorFramework client, String path) throws Exception {
        TreeCache treeCache = new TreeCache(client, path);
        treeCache.getListenable().addListener((curatorFramework, event) -> {
            String path1 = null == event.getData() ? "" : event.getData().getPath();
            if (path1.isEmpty()) {
                return;
            }
            System.out.println(path1 + " -- " + event.getType().toString());
        });
        treeCache.start();
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void main(String[] args) throws Exception {
        CacheListenerTest cacheTest = new CacheListenerTest();
        //cacheTest.addPathCache(cacheTest.getClient(), "/task");
        //cacheTest.addNodeCache(cacheTest.getClient(), "/task");
        cacheTest.addTreeCache(cacheTest.getClient(), "/task");
    }

}
