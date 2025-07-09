package com.example.dao;

import java.sql.*;

public abstract class BaseDAO {
    // Default to localhost, but can be changed for network setup
    private static String dbHost = "localhost";
    
    /**
     * Set the database host for network connections
     * @param host The hostname or IP address of the database server
     */
    public static void setDatabaseHost(String host) {
        dbHost = host;
    }
    
    /**
     * Get the current database host
     * @return The current database host
     */
    public static String getDatabaseHost() {
        return dbHost;
    }
    
    public Connection getConnection() throws SQLException {
        // Build connection URL with configurable host
        String url = "jdbc:mysql://" + dbHost + ":3306/drinks_distributor";
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