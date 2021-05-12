package com.study.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    /**
     * 设置id生成策略：
     * 1. AUTO 自动增长
     * 2. ID_WORKER 自带策略，生成19位值，数字类型使用这种策略
     * 3. ID_WORKER_STR 自带策略，生成19位值，字符串类型使用这种策略
     * 4. INPUT 需要设置id值
     * 5. NONE 没有策略，也需要设置id值
     * 6. UUID 随机唯一值
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private String email;

    // create_time
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    // update_time
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 版本号
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    // 逻辑删除字段（表中设置默认值0）
    @TableLogic
    private Integer deleted;
}
