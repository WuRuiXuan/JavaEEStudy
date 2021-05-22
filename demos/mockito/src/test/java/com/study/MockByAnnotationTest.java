package com.study;

import com.study.controller.LoginController;
import com.study.entity.AccountInfo;
import com.study.entity.LoginInfo;
import com.study.service.LoginService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class MockByAnnotationTest {

    @Autowired
    // 标注将当前测试类中所有的Mock对象注入进来
    @InjectMocks
    private LoginController loginController;

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private LoginService loginService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeepMock() {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUsername("张三");
        loginInfo.setPassword("3333");

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(1L);
        accountInfo.setUsername("王五");
        accountInfo.setPassword("5555");
        when(loginService.findAccount(anyString(), anyString())).thenReturn(accountInfo);
        String result = loginController.login(loginInfo);
        System.out.println(result);
    }
}
