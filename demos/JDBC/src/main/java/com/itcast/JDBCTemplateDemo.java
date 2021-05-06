package com.itcast;

import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCTemplateDemo {

    private JdbcTemplate template = new JdbcTemplate(DruidUtils.getDataSource());

    public static void main(String[] args) {
        JdbcTemplate template = new JdbcTemplate(DruidUtils.getDataSource());
        String sql = "update account set balance = 1000 where id = ?";
        int count = template.update(sql, 3);
        System.out.println(count);
    }

    // JUnit单元测试，可以让方法独立执行

    @Test
    public void test1() {
        String sql = "update emp set salary = 10000 where id = 1";
        int count = template.update(sql);
        System.out.println("update: " + count);
    }

    @Test
    public void test2() {
        String sql = "insert into emp(id, ename, dept_id) values(?, ?, ?)";
        int count = template.update(sql, null, "产品经理", 10);
        System.out.println("update: " + count);
    }

    @Test
    public void test3() {
        String sql = "delete from emp where ename = ?";
        int count = template.update(sql, "产品经理");
        System.out.println("update: " + count);
    }

    @Test
    public void test4() {
        String sql = "select * from emp where id = ?";
        // 查询结果集长度只能为1，列名作key，值作value
        Map<String, Object> map = template.queryForMap(sql, 1);
        System.out.println(map);
    }

    @Test
    public void test5() {
        String sql = "select * from emp where id = ? or id = ?";
        // 查询结果集长度只能为1，列名作key，值作value
        List<Map<String, Object>> list = template.queryForList(sql, 1, 2);
        for (Map<String, Object> map : list) {
            System.out.println(map);
        }
    }

    @Test
    public void test6() {
        String sql = "select * from emp";
//        List<Emp> list = template.query(sql, new RowMapper<Emp>() {
//            @Override
//            public Emp mapRow(ResultSet rs, int i) throws SQLException {
//                int id = rs.getInt("id");
//                String ename = rs.getString("ename");
//                int job_id = rs.getInt("job_id");
//                int mgr = rs.getInt("mgr");
//                Date joindate = rs.getDate("joindate");
//                double salary = rs.getDouble("salary");
//                double bouns = rs.getDouble("bouns");
//                int dept_id = rs.getInt("dept_id");
//                Emp emp = new Emp();
//                emp.setId(id);
//                emp.setEname(ename);
//                emp.setJob_id(job_id);
//                emp.setMgr(mgr);
//                emp.setJoindate(joindate);
//                emp.setSalary(salary);
//                emp.setBouns(bouns);
//                emp.setDept_id(dept_id);
//                return emp;
//            }
//        });
        List<Emp> list = template.query(sql, new BeanPropertyRowMapper<Emp>(Emp.class));
        for (Emp emp : list) {
            System.out.println(emp);
        }
    }

    @Test
    public void test7() {
        String sql = "select count(id) from emp";
        Long total = template.queryForObject(sql, Long.class);
        System.out.println(total);
    }
}
