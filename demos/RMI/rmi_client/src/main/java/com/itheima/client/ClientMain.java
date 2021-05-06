package com.itheima.client;

import com.itheima.service.UserService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class ClientMain {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        UserService userService = (UserService) Naming.lookup("rmi://localhost:8888/UserService");
        String s = userService.sayHello("客户端");
        System.out.println(s);
    }
}
