package com.example.locofx.UserDB;

import java.sql.*;

public class Connect {
    public Connection con;

    public Connect() {
        String url = "jdbc:postgresql://db.fe.up.pt:5432/meec1t0104";
        String user = "meec1t0104";
        String password = "loco1234";

        try {
            con = DriverManager.getConnection(url, user, password);
            String sql = "SET search_path TO loco_db;";
            PreparedStatement stmt= con.prepareStatement(sql);
            stmt.execute();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet sendStatement(String sql, String change1, String change2, int n) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql);

        switch (n){
            case 0 -> {
                stmt.setString(1, change1);
                return stmt.executeQuery();
            }
            case 1 -> {
                stmt.setString(1, change1);
                stmt.setString(2, change2);
                stmt.executeUpdate();
            }
            case 2 -> {
                stmt.setString(1, change1);
                stmt.setString(2, change2);
                return stmt.executeQuery();
            }
        }
        return null;
    }
}
