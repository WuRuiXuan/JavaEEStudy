package com.itheima.server;

import com.itheima.service.impl.UserServiceImpl;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerMain {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        // 1. 启动RMI注册服务，指定端口号
        LocateRegistry.createRegistry(8888);
        // 2. 创建要被访问的远程对象的实例
        UserServiceImpl userService = new UserServiceImpl();
        // 3. 把远程对象实例注册到RMI注册中心服务上
        Naming.bind("rmi://localhost:8888/UserService", userService);

        System.out.println("服务端启动中...");
    }

}
