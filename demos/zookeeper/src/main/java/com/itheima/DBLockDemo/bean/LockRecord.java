package com.itheima.DBLockDemo.bean;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "lock_record")
public class LockRecord {

    @Id
    private Integer id;

    private String lockName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}
