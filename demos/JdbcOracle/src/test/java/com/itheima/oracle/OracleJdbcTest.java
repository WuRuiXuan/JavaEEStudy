package com.itheima.oracle;

import oracle.jdbc.OracleTypes;
import org.junit.Test;

import java.sql.*;

public class OracleJdbcTest {

    @Test
    public void javaCallOracle() throws Exception {
        // 加载数据库驱动
        Class.forName("oracle.jdbc.driver.OracleDriver");
        // 得到Connection连接
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.3.26:1521:xe", "itheima", "itheima");
        // 得到预编译的Statement对象
        PreparedStatement pstm = conn.prepareStatement("select  * from emp where empno = ?");
        // 给参数赋值
        pstm.setObject(1, 7788);
        // 执行查询操作
        ResultSet rs = pstm.executeQuery();
        // 输出结果
        while (rs.next()) {
            System.out.println(rs.getString("ename"));
        }
        // 释放资源
        rs.close();
        pstm.close();
        conn.close();
    }

    /**
     * java调用存储过程
     * {call <procedure-name>[(<arg1>,<arg2>, ...)]}
     * @throws Exception
     */
    @Test
    public void javaCallProcedure() throws Exception {
        // 加载数据库驱动
        Class.forName("oracle.jdbc.driver.OracleDriver");
        // 得到Connection连接
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.3.26:1521:xe", "itheima", "itheima");
        // 得到预编译的Statement对象
        CallableStatement cstm = conn.prepareCall("{call p_yearsal(?, ?)}");
        // 给参数赋值
        cstm.setObject(1, 7788);
        cstm.registerOutParameter(2, OracleTypes.NUMBER);
        cstm.execute();
        // 输出结果（第二个参数）
        System.out.println(cstm.getObject(2));
        // 释放资源
        cstm.close();
        conn.close();
    }

    /**
     * java调用存储函数
     * {?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
     * @throws Exception
     */
    @Test
    public void javaCallFunction() throws Exception {
        // 加载数据库驱动
        Class.forName("oracle.jdbc.driver.OracleDriver");
        // 得到Connection连接
        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.3.26:1521:xe", "itheima", "itheima");
        // 得到预编译的Statement对象
        CallableStatement cstm = conn.prepareCall("{?= call f_yearsal(?)}");
        // 给参数赋值
        cstm.setObject(2, 7788);
        cstm.registerOutParameter(1, OracleTypes.NUMBER);
        cstm.execute();
        // 输出结果（第二个参数）
        System.out.println(cstm.getObject(1));
        // 释放资源
        cstm.close();
        conn.close();
    }
}
