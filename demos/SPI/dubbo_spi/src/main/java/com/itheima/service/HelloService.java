package com.itheima.service;

import com.alibaba.dubbo.common.extension.SPI;

@SPI
public interface HelloService {
    void sayHello();
}
