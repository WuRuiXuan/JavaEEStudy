package com.itheima.service.impl;

import com.itheima.dao.UserDao;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

// <bean id="userService" class="com.itheima.service.impl.UserServiceImpl" scope="singleton">
//        <property name="userDao" ref="userDao"></property>
//    </bean>

@Service("userService")
//@Scope("prototype")
@Scope("singleton")
public class UserServiceImpl implements UserService {

//    @Autowired // 按照数据类型从Spring容器中进行匹配
//    @Qualifier("userDao") // 按照id值从Spring容器中进行匹配，但是要结合@Autowired一起使用
    @Resource(name = "userDao") // 相当于@Autowired + @Qualifier
    private UserDao userDao;

//    public void setUserDao(UserDao userDao) {
//        this.userDao = userDao;
//    }

    public void save() {
        userDao.save();
    }

    @PostConstruct
    public void init() {
        System.out.println("UserService的初始化方法");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("UserService的销毁方法");
    }
}
