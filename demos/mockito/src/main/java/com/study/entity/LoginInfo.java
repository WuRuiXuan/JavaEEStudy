package com.study.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class LoginInfo {

    private String username;

    private String password;
}
