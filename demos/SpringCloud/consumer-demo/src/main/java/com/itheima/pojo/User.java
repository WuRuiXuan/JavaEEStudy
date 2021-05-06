package com.itheima.pojo;

import lombok.Data;

import java.util.Date;

// 标注在编译阶段会根据注解自动生成对应的方法：get/set/hashCode/equals/toString等
@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private Integer sex;
    private Date birthday;
    private String note;
    private Date created;
    private Date updated;

}
