package com.itheima.demo;

import com.itheima.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class UserController {

    public static void main(String[] args) {
        ApplicationContext app = new FileSystemXmlApplicationContext("C:\\Users\\hp650\\Desktop\\Spring\\itheima_spring_ioc\\src\\main\\resources\\applicationContext.xml");
//        UserService userService = (UserService) app.getBean("userService");
        UserService userService = app.getBean(UserService.class);
        userService.save();
    }
}
