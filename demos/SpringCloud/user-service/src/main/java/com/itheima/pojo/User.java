package com.itheima.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

// 标注在编译阶段会根据注解自动生成对应的方法：get/set/hashCode/equals/toString等
@Data
// 通用mapper标注表
@Table(name = "users")
public class User {

    // 标注主键
    @Id
    // 标注主键回填（数据库插入完后会将新增的id值回填到新增的对象中）
    @KeySql(useGeneratedKeys = true)
    private Long id;

    // 标注数据库字段名称（完全不一致才需要标注，如user_name或userName则不需要标注）
//    @Column(name = "abc")
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
