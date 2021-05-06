package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

// 标注spring boot工程的入口类
@SpringBootApplication
// mybatis的mapper扫描（包路径下的类不用添加@Mapper注解）引用的是org.mybatis.spring.annotation.MapperScan
// 通用mapper扫描 引用的是tk.mybatis.spring.annotation.MapperScan
@MapperScan("com.itheima.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
