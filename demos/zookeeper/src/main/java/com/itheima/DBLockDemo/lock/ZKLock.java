package com.itheima.DBLockDemo.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ZKLock implements Lock {

    // zk客户端
    private ZooKeeper zk;
    // zk是一个目录结构，locks
    private String LOCK_ROOT_PATH = "/locks";
    // 锁的名称
    private String lockName;
    // 当前线程创建的序列node
    private ThreadLocal<String> nodeId = new ThreadLocal<>();
    // 用来同步等待zkclient连接到了服务端
    private CountDownLatch connectedSignal = new CountDownLatch(1);
    private final static int sessionTimeout = 3000;
    // 监视器对象，监视上一个节点是否被删除
    private Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeDeleted) {
                synchronized (this) {
                    notifyAll();
                }
            }
        }
    };

    public ZKLock(String config, String lockName) {
        this.lockName = lockName;

        try {
            zk = new ZooKeeper(config, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    // 建立连接
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("连接成功");
                        connectedSignal.countDown();
                    }
                }
            });

            connectedSignal.await();

            // 判断locks是否存在，不存在则创建
            Stat stat = zk.exists(LOCK_ROOT_PATH, false);
            if (stat == null) {
                // 创建根节点
                zk.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void lock() {
        try {
            // 创建临时有序节点
            String myNode = zk.create(LOCK_ROOT_PATH + "/" + lockName, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + ":" + myNode + " created");

            attemptLock(myNode);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 尝试获取锁
    private void attemptLock(String myNode) throws Exception {
        // 获取locks节点下所有的子节点
        List<String> subNodes = zk.getChildren(LOCK_ROOT_PATH, false);
        // 对子节点进行排序
        TreeSet<String> sortedNodes = new TreeSet<>();
        for (String node : subNodes) {
            sortedNodes.add(LOCK_ROOT_PATH + "/" + node);
        }
        String smallNode = sortedNodes.first();
        if (myNode.equals(smallNode)) {
            // 如果是最小的节点，则表示取得锁
            System.out.println(Thread.currentThread().getName() + ":" + myNode + " get lock");
            this.nodeId.set(myNode);
            return;
        }

        String preNode = sortedNodes.lower(myNode);

        // 同时注册监听
        Stat stat = zk.exists(preNode, watcher);
        // 判断比自己小一个数的节点是否存在，如果不存在则无需等待锁，同时注册监听
        if (stat == null) {
            attemptLock(myNode);
        }
        else {
            System.out.println(Thread.currentThread().getName() + ":" + myNode + " waiting for " + LOCK_ROOT_PATH + "/" + preNode + " released lock");
            // 一直等待其它线程释放锁
            synchronized (watcher) {
                watcher.wait();
            }
            attemptLock(myNode);
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            System.out.println(Thread.currentThread().getName() + " unlock");
            if (nodeId != null) {
                zk.delete(nodeId.get(), -1);
            }
            nodeId.remove();
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
