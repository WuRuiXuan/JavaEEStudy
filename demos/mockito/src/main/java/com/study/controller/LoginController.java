package com.study.controller;

import com.study.entity.AccountInfo;
import com.study.entity.LoginInfo;
import com.study.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginInfo loginInfo) {
        try {
            AccountInfo account = loginService.findAccount(loginInfo.getUsername(), loginInfo.getPassword());
            if (account == null) {
                return "Fail!";
            }
            else {
                System.out.println(account);
                return "Success!";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return "/505";
        }
    }
}
