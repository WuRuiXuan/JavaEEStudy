package com.itheima.service.impl;

import com.itheima.service.UserService;

public class UserServiceImpl implements UserService {
    public String sayHello(String name) {
        return name + "成功调用了hessian服务端的服务";
    }
}
