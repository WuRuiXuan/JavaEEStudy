package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Reference
    private HelloService helloService;

    @RequestMapping(value = "/sayHello", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String sayHello() {
        // 完成对服务的远程调用
        String s = helloService.sayHello();
        System.out.println(s);
        return s;
    }
}
