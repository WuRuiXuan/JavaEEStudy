package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// 开启Eureka客户端发现功能
//@EnableDiscoveryClient
// 开启Hystrix熔断
//@EnableCircuitBreaker
//@SpringBootApplication
// 开启Feign
@EnableFeignClients
// 使用@SpringCloudApplication则不需要添加注解：@SpringBootApplication、@EnableCircuitBreaker、@EnableDiscoveryClient
@SpringCloudApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    // 开启Ribbon
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
