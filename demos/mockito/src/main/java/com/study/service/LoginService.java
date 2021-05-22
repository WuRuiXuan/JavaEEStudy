package com.study.service;

import com.study.entity.AccountInfo;
import com.study.mapper.LoginMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    @Autowired
    private LoginMapper loginMapper;

    public LoginMapper getLoginMapper() {
        return loginMapper;
    }

    public AccountInfo findAccount(String username, String password) {
        return loginMapper.findAccount(username, password);
    }
}
