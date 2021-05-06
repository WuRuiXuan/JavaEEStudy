package com.itheima.service;

import com.itheima.domain.Account;

import java.io.IOException;
import java.util.List;

public interface AccountService {

    public void save(Account account) throws IOException;

    public List<Account> findAll() throws IOException;
}
