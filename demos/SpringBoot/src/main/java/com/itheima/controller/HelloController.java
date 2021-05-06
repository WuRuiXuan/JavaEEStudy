package com.itheima.controller;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

// 标注只返回字符串，不能返回页面（相当于 @Controller + @ResponseBody）
@RestController
public class HelloController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Value("${itcast.url}")
    private String itcastUrl;

    @Value("${itheima.url}")
    private String itheimaUrl;

    @GetMapping("hello")
    public String hello() {
        System.out.println("dataSource = " + dataSource);
        System.out.println("itcastUrl = " + itcastUrl);
        System.out.println("itheimaUrl = " + itheimaUrl);
        return "Hello, Spring Boot!";
    }

    @GetMapping("user/{id}")
    public User queryById(@PathVariable Long id) {
        return userService.queryById(id);
    }
}
