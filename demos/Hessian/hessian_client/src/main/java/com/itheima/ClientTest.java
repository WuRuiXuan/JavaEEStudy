package com.itheima;

import com.caucho.hessian.client.HessianProxyFactory;
import com.itheima.service.UserService;

import java.net.MalformedURLException;

public class ClientTest {

    public static void main(String[] args) throws MalformedURLException {
        String url = "http://localhost:8888/api/service";
        HessianProxyFactory hessianProxyFactory = new HessianProxyFactory();
        UserService userService = (UserService) hessianProxyFactory.create(UserService.class, url);
        String s = userService.sayHello("hessian客户端");
        System.out.println(s);
    }
}
