package com.itheima.controller;

import com.itheima.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import javafx.beans.DefaultProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/consumer")
@Slf4j
@DefaultProperties(defaultFallback = "defaultFallback")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

//    @GetMapping("/{id}")
    public User queryUserById(@PathVariable Long id) {
//        String url = "http://localhost:9091/user/" + id;
        // 获取eureka中注册的user-service中的实例
//        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("user-service");
//        ServiceInstance serviceInstance = serviceInstances.get(0);
//        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/user/" + id;
        String url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, User.class);
    }

    @GetMapping("/{id}")
//    @HystrixCommand(fallbackMethod = "queryByIdFallback")
    // fallbackMethod返回类型必须与请求返回类型一致
    @HystrixCommand
    public String queryStringById(@PathVariable Long id) {
        if (id == 1) {
            throw new RuntimeException("太忙了");
        }
        String url = "http://user-service/user/" + id;
        return restTemplate.getForObject(url, String.class);
    }

    public String queryByIdFallback(Long id) {
        log.error("查询用户信息失败。id: {}", id);
        return "对不起！网络太拥挤了！";
    }

    public String defaultFallback() {
        return "默认提示：对不起！网络太拥挤了！";
    }
}
