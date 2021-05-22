package com.study;

import com.study.controller.LoginController;
import com.study.entity.AccountInfo;
import com.study.entity.LoginInfo;
import com.study.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MockByRunnerTest {

    private LoginService loginService;
    private LoginInfo loginInfo;
    private LoginController loginController;

    @Before
    public void init() {
        this.loginService = mock(LoginService.class, RETURNS_SMART_NULLS);
        this.loginInfo = mock(LoginInfo.class, RETURNS_SMART_NULLS);
        this.loginController = new LoginController(loginService);
    }

    @Test
    public void testLoginSuccess() {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(1L);
        accountInfo.setUsername("张三");
        accountInfo.setPassword("3333");
        when(loginInfo.getUsername()).thenReturn("李四");
        when(loginInfo.getPassword()).thenReturn("4444");
        when(loginService.findAccount(anyString(), anyString())).thenReturn(accountInfo);
        assertThat(loginController.login(loginInfo), equalTo("Success!"));
    }

    @Test
    public void testLoginFail() {
        when(loginInfo.getUsername()).thenReturn("李四");
        when(loginInfo.getPassword()).thenReturn("4444");
        when(loginService.findAccount(anyString(), anyString())).thenThrow(UnsupportedOperationException.class);
        assertThat(loginController.login(loginInfo), equalTo("/505"));
    }
}
