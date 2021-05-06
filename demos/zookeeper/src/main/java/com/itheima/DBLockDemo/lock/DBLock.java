package com.itheima.DBLockDemo.lock;

import com.itheima.DBLockDemo.bean.LockRecord;
import com.itheima.DBLockDemo.mapper.LockRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component
public class DBLock implements Lock {

    private static final String LOCK_NAME = "db_lock_stock";

    @Autowired
    private LockRecordMapper lockRecordMapper;

    /**
     * 上锁：添加记录
     */
    public void lock() {
        try {
            while (true) {
                boolean b = tryLock();
                if (b) {
                    LockRecord lockRecord = new LockRecord();
                    lockRecord.setLockName(LOCK_NAME);
                    lockRecordMapper.insert(lockRecord);
                    return;
                }
                else {
                    System.out.println(Thread.currentThread().getName() + "：循环等待中...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            lock();
        }
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * 尝试获取锁：根据指定的名称在数据库表中发起一次查询
     * sql: select * from lock_record where lock_name = "db_lock_stock"
     * @return
     */
    public boolean tryLock() {
        Example example = new Example(LockRecord.class);
        example.createCriteria().andEqualTo("lockName", LOCK_NAME);
        LockRecord lockRecord = lockRecordMapper.selectOneByExample(example);
        if (lockRecord == null) {
            return true;
        }

        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * 解锁：删除指定名称的记录
     */
    public void unlock() {
        Example example = new Example(LockRecord.class);
        example.createCriteria().andEqualTo("lockName", LOCK_NAME);
        lockRecordMapper.deleteByExample(example);
    }

    public Condition newCondition() {
        return null;
    }
}
