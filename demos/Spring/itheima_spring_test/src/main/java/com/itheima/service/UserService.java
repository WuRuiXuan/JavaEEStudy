package com.itheima.service;

import com.itheima.domain.User;

import java.util.List;

public interface UserService {
    public List<User> list();

    public void save(User user, Long[] roleIds);

    public void del(Long userId);

    public User login(String username, String password);
}
