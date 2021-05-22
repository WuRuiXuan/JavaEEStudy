package com.study.mapper;

import com.study.entity.AccountInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface LoginMapper {

    @Select("select * from account_test where username=#{username} and password=#{password} limit 1")
    public AccountInfo findAccount(@Param("username") String username, @Param("password") String password);
}
