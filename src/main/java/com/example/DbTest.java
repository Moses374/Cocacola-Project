package com.example;

import com.example.dao.BaseDAO;
import com.example.dao.BranchDAO;
import model.Branch;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Utility class to test database connectivity and retrieve branch information
 */
public class DbTest {
    
    // Concrete implementation of BaseDAO for connection testing
    private static class TestDAO extends BaseDAO {
        // No additional implementation needed
    }
    
    public static void main(String[] args) {
        testDbConnection();
        listAvailableBranches();
    }
    
    /**
     * Tests the database connection
     */
    public static void testDbConnection() {
        System.out.println("Testing database connection...");
        try {
            TestDAO dao = new TestDAO();
            Connection conn = dao.getConnection();
            System.out.println("Connection successful!");
            System.out.println("Connected to: " + conn.getMetaData().getURL());
            System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName() + " " + 
                    conn.getMetaData().getDatabaseProductVersion());
            conn.close();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Lists all available branches in the database
     */
    public static void listAvailableBranches() {
        System.out.println("\nRetrieving available branches...");
        try {
            BranchDAO branchDAO = new BranchDAO();
            List<Branch> branches = branchDAO.getAllBranches();
            
            if (branches.isEmpty()) {
                System.out.println("No branches found in the database.");
            } else {
                System.out.println("Found " + branches.size() + " branches:");
                for (Branch branch : branches) {
                    System.out.println(branch.getBranchId() + "-" + branch.getBranchName() + 
                            " (" + branch.getLocation() + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving branches: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 