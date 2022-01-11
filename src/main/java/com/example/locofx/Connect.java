package com.example.locofx;

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

            System.out.println("Connected to the PostgreSQL server successfully.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
