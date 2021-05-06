package com.itheima.dao;

import com.itheima.domain.User;

import java.util.List;

public interface UserDao {

    public List<User> findAll();

    public Long save(User user);

    public void saveUserRoleRel(Long userId, Long[] roleIds);

    public void delUserRoleRel(Long userId);

    public void del(Long userId);

    public User findByUsernameAndPassword(String username, String password);
}
