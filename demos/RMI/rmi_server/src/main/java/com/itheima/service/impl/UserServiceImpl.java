package com.itheima.service.impl;

import com.itheima.service.UserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    public UserServiceImpl() throws RemoteException {
    }

    public String sayHello(String name) throws RemoteException {
        return name + "成功调用了服务端的服务";
    }

}
