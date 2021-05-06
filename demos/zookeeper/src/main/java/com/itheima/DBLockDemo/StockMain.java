package com.itheima.DBLockDemo;

import com.itheima.DBLockDemo.bean.Stock;
import com.itheima.DBLockDemo.lock.DBLock;
import com.itheima.DBLockDemo.lock.RedisLock;
import com.itheima.DBLockDemo.lock.ZKLock;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StockMain {

//    private static Lock lock = new ReentrantLock();

//    @Autowired
//    private static DBLock lock;

//    @Autowired
//    private static RedisLock lock;

//    private static RLock lock;

    private static ZKLock lock;

    static {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//        lock = classPathXmlApplicationContext.getBean(DBLock.class);
//        lock = classPathXmlApplicationContext.getBean(RedisLock.class);

//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
//        Redisson redisson = (Redisson) Redisson.create(config);
//        lock = redisson.getLock("redisson_lock_stock");

        lock = new ZKLock("192.168.3.26:2181", "zk_lock_stock");
    }

    static class StockThread implements Runnable {

        public void run() {
            // 上锁
            lock.lock();

            // 调用减少库存的方法
            boolean b = new Stock().reduceStock();

            // 解锁
            lock.unlock();

            if (b) {
                System.out.println(Thread.currentThread().getName() + "：减少库存成功");
            }
            else {
                System.out.println(Thread.currentThread().getName() + "：减少库存失败");
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new StockThread(), "线程1").start();
        new Thread(new StockThread(), "线程2").start();
    }
}
