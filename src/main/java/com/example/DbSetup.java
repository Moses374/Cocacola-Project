package com.example;

import com.example.dao.BaseDAO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to set up the database by executing SQL script
 */
public class DbSetup {
    
    /**
     * Main method to run the database setup
     */
    public static void main(String[] args) {
        System.out.println("Starting database setup...");
        
        try {
            executeSqlScript("drinks_distributer.sql");
            System.out.println("Database setup completed successfully!");
        } catch (Exception e) {
            System.err.println("Error setting up database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Execute SQL script from file
     */
    private static void executeSqlScript(String scriptPath) throws IOException, SQLException {
        System.out.println("Reading SQL script from: " + scriptPath);
        
        // Read SQL file
        List<String> sqlStatements = parseSqlFile(scriptPath);
        System.out.println("Found " + sqlStatements.size() + " SQL statements to execute");
        
        // Get connection using BaseDAO
        BaseDAO baseDAO = new BaseDAO() {};
        try (Connection conn = baseDAO.getConnection()) {
            conn.setAutoCommit(false);
            
            try (Statement stmt = conn.createStatement()) {
                // Execute each statement
                for (int i = 0; i < sqlStatements.size(); i++) {
                    String sql = sqlStatements.get(i);
                    try {
                        stmt.execute(sql);
                        System.out.println("Executed statement " + (i + 1) + "/" + sqlStatements.size());
                    } catch (SQLException e) {
                        System.err.println("Error executing SQL: " + sql);
                        System.err.println("Error message: " + e.getMessage());
                        // Continue execution even if one statement fails
                    }
                }
                
                // Commit all changes
                conn.commit();
                System.out.println("All SQL statements committed successfully");
            } catch (SQLException e) {
                // Rollback on error
                conn.rollback();
                throw e;
            } finally {
                // Reset auto-commit
                conn.setAutoCommit(true);
            }
        }
    }
    
    /**
     * Parse SQL file into individual statements
     */
    private static List<String> parseSqlFile(String filePath) throws IOException {
        List<String> statements = new ArrayList<>();
        StringBuilder currentStatement = new StringBuilder();
        boolean inMultiLineComment = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Handle comments
                if (line.trim().startsWith("--")) {
                    continue;
                }
                
                // Handle multi-line comments
                if (inMultiLineComment) {
                    if (line.contains("*/")) {
                        inMultiLineComment = false;
                        line = line.substring(line.indexOf("*/") + 2);
                    } else {
                        continue;
                    }
                }
                
                if (line.contains("/*")) {
                    if (line.contains("*/")) {
                        line = line.substring(0, line.indexOf("/*")) + 
                               line.substring(line.indexOf("*/") + 2);
                    } else {
                        line = line.substring(0, line.indexOf("/*"));
                        inMultiLineComment = true;
                    }
                }
                
                // Add line to current statement
                currentStatement.append(line).append(" ");
                
                // Check if statement is complete
                if (line.trim().endsWith(";")) {
                    statements.add(currentStatement.toString());
                    currentStatement = new StringBuilder();
                }
            }
            
            // Add the last statement if it doesn't end with a semicolon
            if (currentStatement.length() > 0) {
                statements.add(currentStatement.toString());
            }
        }
        
        return statements;
    }
} 