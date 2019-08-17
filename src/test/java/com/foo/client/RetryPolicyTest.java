package com.foo.client;

import com.foo.client.config.ZookeeperConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2019/8/15
 */

public class RetryPolicyTest {

    ZookeeperConfiguration conf;

    @Before
    public void before(){
        conf = new ZookeeperConfiguration("localhost:2182");
    }

    @Test
    public void test_RetryForever() throws Exception {
        conf.setRetryPolicy(new RetryForever(100));
    }

    @Test
    public void test_RetryNTimes() throws Exception {
        conf.setRetryPolicy(new RetryNTimes(3,100));
    }

    @Test
    public void test_RetryOneTime() throws Exception {
        conf.setRetryPolicy(new RetryOneTime(100));
    }

    @After
    public void after() throws Exception {
        ZookeeperOperations zookeeperOperations = new ZookeeperOperations(conf);
        zookeeperOperations.init();
        CuratorFramework framework = zookeeperOperations.getCuratorFramework();
        framework.create().forPath("/test");
    }

}
