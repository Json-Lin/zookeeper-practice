package com.foo.lock;

import com.foo.client.ZookeeperOperations;
import com.foo.client.config.ZookeeperConfiguration;
import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

public class InterProcessMutexTest {

	 private InterProcessMutex lock;
	 
	 public InterProcessMutexTest (InterProcessMutex lock){
		 this.lock = lock;
	 }
	
	 public void doWork(){
		 try {
			 if(lock.acquire(1000, TimeUnit.MILLISECONDS)){
				 System.out.println("获取锁");
			 }
			 Thread.sleep(Integer.MAX_VALUE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				lock.release();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	 }
	 
	public static void main(String[] args) {
		CuratorFramework client = getClient();
		String path = "/test-lock";
		InterProcessMutex myLock = new InterProcessMutex(client, path);
		InterProcessMutexTest interProcessMutexTest = new InterProcessMutexTest(myLock );
		interProcessMutexTest.doWork();
	}
	public static CuratorFramework getClient() {
		ZookeeperConfiguration conf = new ZookeeperConfiguration("localhost:2181");
		//conf.setNamespace("/dip-elastic-task");
		ZookeeperOperations zookeeperOperations = new ZookeeperOperations(conf);
		zookeeperOperations.init();
		return zookeeperOperations.getCuratorFramework();
	}
}
