package com.example.dao;

import model.Inventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO extends BaseDAO {
    public void addInventory(Inventory inventory) throws SQLException {
        String sql = "INSERT INTO inventory (drink_id, branch_id, quantity, threshold) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventory.getDrinkId());
            stmt.setInt(2, inventory.getBranchId());
            stmt.setInt(3, inventory.getQuantity());
            stmt.setInt(4, inventory.getThreshold());
            stmt.executeUpdate();
        }
    }

    public Inventory getInventoryById(int id) throws SQLException {
        String sql = "SELECT * FROM inventory WHERE inventory_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("drink_id"),
                    rs.getInt("branch_id"),
                    rs.getInt("quantity"),
                    rs.getInt("threshold")
                );
            }
        }
        return null;
    }

    public List<Inventory> getAllInventory() throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                inventoryList.add(new Inventory(
                    rs.getInt("inventory_id"),
                    rs.getInt("drink_id"),
                    rs.getInt("branch_id"),
                    rs.getInt("quantity"),
                    rs.getInt("threshold")
                ));
            }
        }
        return inventoryList;
    }

    public void updateInventory(Inventory inventory) throws SQLException {
        String sql = "UPDATE inventory SET drink_id = ?, branch_id = ?, quantity = ?, threshold = ? WHERE inventory_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventory.getDrinkId());
            stmt.setInt(2, inventory.getBranchId());
            stmt.setInt(3, inventory.getQuantity());
            stmt.setInt(4, inventory.getThreshold());
            stmt.setInt(5, inventory.getInventoryId());
            stmt.executeUpdate();
        }
    }

    public void deleteInventory(int id) throws SQLException {
        String sql = "DELETE FROM inventory WHERE inventory_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
} 