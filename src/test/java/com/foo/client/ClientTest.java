package com.foo.client;

import com.foo.client.config.ZookeeperConfiguration;
import org.apache.zookeeper.CreateMode;
import org.junit.Before;
import org.junit.Test;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2019/8/15
 */
public class ClientTest {

    ZookeeperOperations zookeeperOperations;

    @Before
    public void before(){
        ZookeeperConfiguration conf = new ZookeeperConfiguration("localhost:2181");
        conf.setNamespace("zookeeper-test");
        zookeeperOperations = new ZookeeperOperations(conf);
        zookeeperOperations.init();
    }

    @Test
    public void  test_create() throws Exception {
        zookeeperOperations.create("/test_create_em", CreateMode.EPHEMERAL);
    }


}
