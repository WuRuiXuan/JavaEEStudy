package com.itheima.mapper;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccountMapper {

    @Insert("insert into account values(#{id}, #{name}, #{balance})")
    public void save(Account account);

    @Select("select * from account")
    public List<Account> findAll();
}
