package com.study.entity;

import lombok.Data;

import javax.persistence.Id;

@Data
public class AccountInfo {

    @Id
    private Long id;

    private String username;

    private String password;
}
