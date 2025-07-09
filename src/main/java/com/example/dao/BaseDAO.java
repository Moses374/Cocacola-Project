package com.example.dao;

import java.sql.*;

public abstract class BaseDAO {
    public Connection getConnection() throws SQLException {
        // Update with your DB credentials if needed
        String url = "jdbc:mysql://localhost:3306/drinks_distributor";
        String user = "root";
        String password = "Noobacious99";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
        return DriverManager.getConnection(url, user, password);
    }
} 