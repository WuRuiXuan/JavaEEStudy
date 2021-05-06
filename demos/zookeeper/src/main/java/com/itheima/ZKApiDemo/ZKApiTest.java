package com.itheima.ZKApiDemo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ZKApiTest {

    // 创建zookeeper连接
    public ZooKeeper createZooKeeper() throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.3.26:2181", 2000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("触发了" + watchedEvent.getType() + "的事件");
            }
        });
        return zooKeeper;
    }

    // 创建父节点
    @Test
    public void createFatherNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = createZooKeeper();
        String path = zooKeeper.create("/itheima", "itheimaValue".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(path);
    }

    // 创建子节点
    @Test
    public void createChildrenNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = createZooKeeper();
        String childrenPath = zooKeeper.create("/itheima/children", "childrenValue".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(childrenPath);
    }

    // 获取节点中的值
    @Test
    public void getNodeData() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = createZooKeeper();
        byte[] data = zooKeeper.getData("/itheima", false, null);
        System.out.println(new String(data));
    }

    // 获取子节点列表
    @Test
    public void getChildrenList() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = createZooKeeper();
        List<String> children = zooKeeper.getChildren("/itheima", false);
        for (String child : children) {
            System.out.println(child);
        }
    }

    // 修改节点的值
    @Test
    public void updateNodeData() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = createZooKeeper();
        // version 数据版本号，如果为-1则可以匹配任何版本
        Stat stat = zooKeeper.setData("/itheima", "itheimaUpdate".getBytes(), -1);
        System.out.println(stat);
        getNodeData();
    }

    // 判断某个节点是否存在
    @Test
    public void isNodeExists() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = createZooKeeper();
        Stat stat = zooKeeper.exists("/itheima/children", false);
        System.out.println(stat);
    }

    // 删除节点
    @Test
    public void deleteNode() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = createZooKeeper();
        zooKeeper.delete("/itheima/children", -1);
        isNodeExists();
    }

}
