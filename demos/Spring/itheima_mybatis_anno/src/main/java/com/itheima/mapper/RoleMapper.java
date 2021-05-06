package com.itheima.mapper;

import com.itheima.domain.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {

    @Select("select * from user_role ur, role r where ur.user_id=r.id and ur.user_id=#{uid}")
    public List<Role> findByUid(int uid);
}
