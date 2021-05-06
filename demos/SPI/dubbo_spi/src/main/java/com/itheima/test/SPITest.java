package com.itheima.test;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.itheima.service.HelloService;

public class SPITest {

    public static void main(String[] args) {
        ExtensionLoader<HelloService> extensionLoader = ExtensionLoader.getExtensionLoader(HelloService.class);
        HelloService service1 = extensionLoader.getExtension("service1");
        service1.sayHello();
    }

}
