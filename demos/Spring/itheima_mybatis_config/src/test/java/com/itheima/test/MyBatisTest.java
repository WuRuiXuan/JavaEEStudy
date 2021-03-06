package com.itheima.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.User;
import com.itheima.mapper.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class MyBatisTest {
    @Test
    public void test1() throws IOException {
        InputStream resourceStream = Resources.getResourceAsStream("sqlMapperConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User user = new User();
        user.setUsername("ceshi");
        user.setPassword("abc");
        user.setBirthday(new Date());
        mapper.save(user);

        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test2() throws IOException {
        InputStream resourceStream = Resources.getResourceAsStream("sqlMapperConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        User user = mapper.findById(8);
        System.out.println("user birthday: " + user.getBirthday());

        sqlSession.close();
    }

    @Test
    public void test3() throws IOException {
        InputStream resourceStream = Resources.getResourceAsStream("sqlMapperConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        // ???????????????????????? ?????????+?????????????????????
        PageHelper.startPage(2, 3);

        List<User> userList = mapper.findAll();
        for (User user : userList) {
            System.out.println(user);
        }

        // ???????????????????????????
        PageInfo<User> pageInfo = new PageInfo<User>(userList);
        System.out.println("????????????" + pageInfo.getPageNum());
        System.out.println("?????????????????????" + pageInfo.getPageSize());
        System.out.println("????????????" + pageInfo.getTotal());
        System.out.println("????????????" + pageInfo.getPages());
        System.out.println("????????????" + pageInfo.getPrePage());
        System.out.println("????????????" + pageInfo.getNextPage());
        System.out.println("?????????????????????" + pageInfo.isIsFirstPage());
        System.out.println("????????????????????????" + pageInfo.isIsLastPage());

        sqlSession.close();
    }
}
