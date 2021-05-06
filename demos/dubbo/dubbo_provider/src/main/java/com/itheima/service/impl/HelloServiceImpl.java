package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.HelloService;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        return "服务被成功调用了...";
    }

}
