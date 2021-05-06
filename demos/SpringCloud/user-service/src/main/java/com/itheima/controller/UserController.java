package com.itheima.controller;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 自动刷新配置
@RefreshScope
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${test.name}")
    private String name;

    @GetMapping("/{id}")
    public User queryById(@PathVariable Long id) throws InterruptedException {
//        Thread.sleep(2000);
        // 配置好Bus后，需先POST请求http://127.0.0.1:12000/actuator/bus-refresh（请求格式必须为JSON），再次刷新地址才能获取最新的配置
        System.out.println("配置文件中的test.name为：" + name);
        return userService.queryById(id);
    }
}
