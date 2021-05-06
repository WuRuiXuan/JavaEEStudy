package com.itheima;

import com.itheima.utils.JedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * 案例要求：
 * 1. 设定初级、中级、高级三个用户
 * 2. 初级用户限制20秒10次调用，中级用户限制20秒30次调用，高级用户不限制
 */
public class Service {
    String id;
    Long num = Long.MAX_VALUE;

    public Service(String id) {
        this.id = id;
    }

    public Service(String id, Long num) {
        this.id = id;
        this.num = num;
    }

    // 控制单元
    public void service() {
//        Jedis jedis = new Jedis("localhost", 6379);
        Jedis jedis = JedisUtils.getJedis();
        String value = jedis.get("compid:" + id);
        try {
            // 判断该值是否存在
            if (value == null) {
                // 不存在，创建该值
                jedis.setex("compid:" + id, 20, Long.MAX_VALUE - num + "");
            } else {
                // 存在，自增，调用业务
                Long val = jedis.incr("compid:" + id);
                business(id, num - (Long.MAX_VALUE - val));
            }
        } catch (JedisDataException e) {
            System.out.println("使用已经到达次数上限，请升级会员级别");
            return;
        } finally {
            jedis.close();
        }
    }

    // 业务操作
    public void business(String id, Long val) {
        System.out.println("用户" + id + " 业务操作执行第" + val + "次");
    }
}

class MyThread extends Thread {
    Service sc;

    public MyThread(String id) {
        sc = new Service(id);
    }

    public MyThread(String id, Long num) {
        sc = new Service(id, num);
    }

    @Override
    public void run() {
        while (true) {
            sc.service();
            try {
                Thread.sleep(400L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Main {
    public static void main(String[] args) {
        MyThread mt1 = new MyThread("初级用户", 10L);
        MyThread mt2 = new MyThread("中级用户", 30L);
        MyThread mt3 = new MyThread("高级用户");
        mt1.start();
        mt2.start();
        mt3.start();
    }
}