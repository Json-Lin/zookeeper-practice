package com.foo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2018/12/25
 */
public class ShareLock {

    public static void main(String[] args) throws Exception {
        CuratorFramework client = CreateClientExamples.createSimple("127.0.0.1:2181");
        client.start();
        InterProcessLock lock = new InterProcessMutex(client,"/my_lock");
        Thread.sleep(1000);
        lock.acquire();
        System.out.println("4464656");
        lock.release();
    }
}
