package com.itheima.service;

public class HelloServiceMock implements HelloService {
    public String sayHello() {
        System.out.println("服务调用失败...");
        return null;
    }
}
