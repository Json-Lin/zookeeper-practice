package com.foo.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2019/5/13
 */
public class MaterLeaderLatch {

    public static void main(String[] args) throws Exception {
        List<LeaderLatch> latches = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();
            clients.add(client);

            LeaderLatch leaderLatch = new LeaderLatch(client, "/master", "node-" + i, LeaderLatch.CloseMode.NOTIFY_LEADER);
            leaderLatch.addListener(new LeaderLatchListener() {
                @Override
                public void isLeader() {
                    System.out.println(leaderLatch.getId() + " is a leader ");
                }

                @Override
                public void notLeader() {
                    System.out.println(leaderLatch.getId() + " not a leader ");
                }
            });
            latches.add(leaderLatch);
        }
        for (LeaderLatch latch : latches) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.start();
                        latch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(latch.getId()+" elect end");
                }
            }).start();
        }
        Thread.sleep(10000);
        for (LeaderLatch latch : latches) {
            System.out.println(latch.getId() + " leader:" + latch.getLeader() + " isLeader:" + latch.hasLeadership());
            if(latch.hasLeadership()){
                latch.close();
            }
        }
        for (CuratorFramework client : clients) {
            System.out.println(client.getData() + " is close");
            client.close();
        }
        Thread.sleep(100000);
    }
}
