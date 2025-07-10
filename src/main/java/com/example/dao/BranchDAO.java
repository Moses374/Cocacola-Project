package com.example.dao;

import model.Branch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BranchDAO extends BaseDAO {
    public Branch getBranchById(int id) throws SQLException {
        String sql = "SELECT * FROM branches WHERE branch_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Branch(
                    rs.getInt("branch_id"),
                    rs.getString("branch_name"),
                    rs.getString("location")
                );
            }
        }
        return null;
    }
    
    public List<Branch> getAllBranches() throws SQLException {
        List<Branch> branches = new ArrayList<>();
        String sql = "SELECT * FROM branches";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                branches.add(new Branch(
                    rs.getInt("branch_id"),
                    rs.getString("branch_name"),
                    rs.getString("location")
                ));
            }
        }
        return branches;
    }
    
    public void addBranch(Branch branch) throws SQLException {
        String sql = "INSERT INTO branches (branch_name, location) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, branch.getBranchName());
            stmt.setString(2, branch.getLocation());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                branch.setBranchId(rs.getInt(1));
            }
        }
    }
    
    public void updateBranch(Branch branch) throws SQLException {
        String sql = "UPDATE branches SET branch_name = ?, location = ? WHERE branch_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, branch.getBranchName());
            stmt.setString(2, branch.getLocation());
            stmt.setInt(3, branch.getBranchId());
            stmt.executeUpdate();
        }
    }
    
    public void deleteBranch(int id) throws SQLException {
        String sql = "DELETE FROM branches WHERE branch_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
} 