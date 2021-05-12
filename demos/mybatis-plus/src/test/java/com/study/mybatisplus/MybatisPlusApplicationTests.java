package com.study.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.mybatisplus.entity.User;
import com.study.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    // 查询user表中所有数据
    @Test
    void findAll() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    // 添加操作
    @Test
    void addUser() {
        User user = new User();
        user.setName("lilei");
        user.setAge(30);
        user.setEmail("lucy@qq.com");
        /**
         * id由mybatis自动生成
         * 主键生成策略：
         * 1. 自动增长，缺点：分成多张表的情况下，必须知道前一张表最后一行数据的id，才能确定后一张表第一行数据的id
         * 2. UUID，每次生成随机唯一的值，缺点：通过id排序不方便
         * 3. redis生成id，INCR和INCRBY
         * 4. mybatis自带策略，雪花算法
         */

        int i = userMapper.insert(user);
        System.out.println("insert: " + i);
    }

    // 修改操作
    @Test
    public void updateUser() {
        User user = new User();
        user.setId(2L);
        user.setAge(120);

        int row = userMapper.updateById(user);
        System.out.println(row);
    }

    // 测试乐观锁
    @Test
    public void testOptimisticLocker() {
        // 根据id查询数据
        User user = userMapper.selectById(7L);
        // 进行修改
        user.setAge(200);
        userMapper.updateById(user);
    }

    // 多个id批量查询
    @Test
    public void findByIds() {
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L, 3L));
        System.out.println(users);
    }

    // 多个条件查询（and）
    @Test
    public void findByConditions() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Jone");
        map.put("age", 18);

        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    // 分页查询（类似PageHelper）
    @Test
    public void findWithPage() {
        // 传入两个参数：当前页和每页显示记录数
        Page<User> page = new Page<>(1, 3);
        // 调用mybatis分页查询方法
        // 底层会自动把分页所有数据封装到page对象里面
        userMapper.selectPage(page, null);
        // 通过page对象获取分页数据
        System.out.println("当前页：" + page.getCurrent());
        System.out.println("每页数据list集合：" + page.getRecords());
        System.out.println("每页显示记录数：" + page.getSize());
        System.out.println("总记录数：" + page.getTotal());
        System.out.println("总页数：" + page.getPages());
        System.out.println("是否有下一页：" + page.hasNext());
        System.out.println("是否有上一页：" + page.hasPrevious());
    }

    // 根据id删除
    @Test
    public void deleteById() {
        int result = userMapper.deleteById(8L);
        System.out.println(result);
    }

    // 批量删除
    @Test
    public void deleteBatchIds() {
        int result = userMapper.deleteBatchIds(Arrays.asList(6, 7));
        System.out.println(result);
    }

    // 条件删除
    @Test
    public void deleteByConditions() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Jone");
        map.put("age", 18);

        int result = userMapper.deleteByMap(map);
        System.out.println(result);
    }

    // 查询已逻辑删除的数据，需要使用sql语句查询

    // 复杂查询操作
    @Test
    public void complexQuery() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // ge、gt、le、lt 大于等于、大于、小于等于、小于
//        wrapper.ge("age", 30);

        // eq、ne 等于、不等于
//        wrapper.eq("name", "lilei");

        // between、notBetween
//        wrapper.between("age", 20, 30);

        // like 模糊查询
//        wrapper.like("name", "T");

        // orderByDesc、orderByAsc
//        wrapper.orderByDesc("id");

        // last
//        wrapper.last("limit 1");

        // 指定要查询的列
        wrapper.select("id", "name");

        List<User> list = userMapper.selectList(wrapper);
        System.out.println(list);
    }
}
