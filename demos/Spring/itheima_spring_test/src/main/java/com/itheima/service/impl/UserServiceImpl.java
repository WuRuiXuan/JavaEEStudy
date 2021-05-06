package com.itheima.service.impl;

import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.domain.Role;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    public List<User> list() {
        List<User> userList = userDao.findAll();
        // 封装userList中的每一个user的roles数据
        for (User user : userList) {
            // 获得user的id
            Long id = user.getId();
            // 将id作为参数 查询当前userId对应的Role集合数据
            List<Role> roles = roleDao.findRolesByUserId(id);
            user.setRoles(roles);
        }
        return userList;
    }

    public void save(User user, Long[] roleIds) {
        // 第一步 向sys_user表中存储数据
        Long userId = userDao.save(user);
        // 第二步 向sys_user_role关系表中存储多条数据
        userDao.saveUserRoleRel(userId, roleIds);
    }

    public void del(Long userId) {
        // 删除sys_user_role关系表
        userDao.delUserRoleRel(userId);
        // 删除sys_user表
        userDao.del(userId);
    }

    public User login(String username, String password) {
        try {
            User user = userDao.findByUsernameAndPassword(username, password);
            return user;
        }
        catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
