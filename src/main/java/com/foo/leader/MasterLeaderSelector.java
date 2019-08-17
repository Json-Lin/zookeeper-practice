package com.foo.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JasonLin
 * @version V1.0
 * @date 2019/5/13
 */
public class MasterLeaderSelector {

    public static void main(String[] args) throws InterruptedException {
        List<LeaderSelector> leaderSelectors = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();
            clients.add(client);

            LeaderSelector leaderSelector = new LeaderSelector(client, "/master", new LeaderSelectorListenerAdapter() {
                @Override
                public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                    System.out.println(Thread.currentThread().getName() + " is a leader");
                    Thread.sleep(3*1000);
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    super.stateChanged(client, newState);
                }
            });
            leaderSelectors.add(leaderSelector);
        }
        leaderSelectors.forEach(leaderSelector -> {
            leaderSelector.autoRequeue();
            leaderSelector.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("==================");
//        clients.forEach(client -> {
//            client.close();
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });

        Thread.sleep(100 * 1000);
    }
}
