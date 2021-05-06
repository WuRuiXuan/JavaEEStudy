package com.itheima.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserService extends Remote {

    public String sayHello(String name) throws RemoteException;

}
