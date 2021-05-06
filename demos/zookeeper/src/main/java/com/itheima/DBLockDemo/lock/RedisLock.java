package com.itheima.DBLockDemo.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component
public class RedisLock implements Lock {

    private static final String LOCK_NAME = "redis_lock_stock";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void lock() {
        while (true) {
            // 设置过期时间避免死锁
            Boolean b = redisTemplate.opsForValue().setIfAbsent("lockName", LOCK_NAME, 15, TimeUnit.SECONDS);
            if (b) {
                return;
            }
            else {
                System.out.println(Thread.currentThread().getName() + "：循环等待中...");
            }
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
        redisTemplate.delete("lockName");
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
