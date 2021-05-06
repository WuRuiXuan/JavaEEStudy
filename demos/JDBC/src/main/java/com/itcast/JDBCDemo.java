package com.itcast;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCDemo {

    public static void main(String[] args) {
        List<Emp> list = fuzzyQuery();
        System.out.println(list);
    }

    public static List<Emp> findAll() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        List<Emp> list = new ArrayList<Emp>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql:///mydb?serverTimezone=GMT", "root", "123456");
            String sql = "select * from emp";
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Emp emp = null;
            while (rs.next()) {
                int id = rs.getInt("id");
                String ename = rs.getString("ename");
                int job_id = rs.getInt("job_id");
                int mgr = rs.getInt("mgr");
                Date joindate = rs.getDate("joindate");
                double salary = rs.getDouble("salary");
                double bouns = rs.getDouble("bouns");
                int dept_id = rs.getInt("dept_id");
                emp = new Emp();
                emp.setId(id);
                emp.setEname(ename);
                emp.setJob_id(job_id);
                emp.setMgr(mgr);
                emp.setJoindate(joindate);
                emp.setSalary(salary);
                emp.setBouns(bouns);
                emp.setDept_id(dept_id);
                list.add(emp);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
    }

    public static List<Emp> findAll2() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        List<Emp> list = new ArrayList<Emp>();
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select * from emp";
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            Emp emp = null;
            while (rs.next()) {
                int id = rs.getInt("id");
                String ename = rs.getString("ename");
                int job_id = rs.getInt("job_id");
                int mgr = rs.getInt("mgr");
                Date joindate = rs.getDate("joindate");
                double salary = rs.getDouble("salary");
                double bouns = rs.getDouble("bouns");
                int dept_id = rs.getInt("dept_id");
                emp = new Emp();
                emp.setId(id);
                emp.setEname(ename);
                emp.setJob_id(job_id);
                emp.setMgr(mgr);
                emp.setJoindate(joindate);
                emp.setSalary(salary);
                emp.setBouns(bouns);
                emp.setDept_id(dept_id);
                list.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, stat, conn);
            return list;
        }
    }

    public static List<Emp> fuzzyQuery() {
        Connection conn = null;
        PreparedStatement pstat = null;
        ResultSet rs = null;
        List<Emp> list = new ArrayList<Emp>();
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "select * from emp where ename like ?";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, "%工程师%");
            rs = pstat.executeQuery();
            Emp emp;
            while (rs.next()) {
                int id = rs.getInt("id");
                String ename = rs.getString("ename");
                int job_id = rs.getInt("job_id");
                int mgr = rs.getInt("mgr");
                Date joindate = rs.getDate("joindate");
                double salary = rs.getDouble("salary");
                double bouns = rs.getDouble("bouns");
                int dept_id = rs.getInt("dept_id");
                emp = new Emp();
                emp.setId(id);
                emp.setEname(ename);
                emp.setJob_id(job_id);
                emp.setMgr(mgr);
                emp.setJoindate(joindate);
                emp.setSalary(salary);
                emp.setBouns(bouns);
                emp.setDept_id(dept_id);
                list.add(emp);
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, pstat, conn);
            return list;
        }
    }
}
