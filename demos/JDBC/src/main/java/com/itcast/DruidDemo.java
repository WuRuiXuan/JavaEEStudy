package com.itcast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DruidDemo {
    public static void main(String[] args) {
        PreparedStatement pstat = null;
        Connection conn = null;
        try {
            conn = DruidUtils.getConnection();
            String sql = "insert into account values(null, ?, ?)";
            pstat = conn.prepareStatement(sql);
            pstat.setString(1, "李六");
            pstat.setDouble(2, 6000);
            int count = pstat.executeUpdate();
            System.out.println(count);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DruidUtils.close(pstat, conn);
        }
    }
}
