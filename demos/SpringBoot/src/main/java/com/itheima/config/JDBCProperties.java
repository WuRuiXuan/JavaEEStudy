package com.itheima.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 标注从application配置文件中读取配置项，prefix表示配置项的前缀
//@ConfigurationProperties(prefix = "jdbc")
public class JDBCProperties {

    String driverClassName;
    String url;
    String username;
    String password;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
