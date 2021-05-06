package com.itcast;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class C3P0Demo {
    public static void main(String[] args) throws Exception {
//        DataSource ds = new ComboPooledDataSource();
        DataSource ds = new ComboPooledDataSource("otherc3p0");
        for (int i = 1; i <= 11; i++) {
            Connection conn = ds.getConnection();
            System.out.println(conn + ":" + i);
            if (i == 5) {
                conn.close();
            }
        }
    }
}
