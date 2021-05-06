package com.itheima.service.impl;

import com.itheima.service.HelloService;

public class HelloServiceImpl implements HelloService {
    public String sayHello(String name) {
        return name + "调用了myRPC的服务";
    }
}
