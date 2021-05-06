package com.itheima.provider;

import com.itheima.pojo.URL;
import com.itheima.registry.NativeRegistry;
import com.itheima.service.HelloService;
import com.itheima.service.impl.HelloServiceImpl;
import com.itheima.tomcat.HttpServer;

public class ServiceProvider {

    public static void main(String[] args) {
        // 真正的注册服务
        URL url = new URL("localhost", 8080);
        NativeRegistry.register(HelloService.class.getName(), url, HelloServiceImpl.class);
        // 启动tomcat，暴露服务
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }
}
