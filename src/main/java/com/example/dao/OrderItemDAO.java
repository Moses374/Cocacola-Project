package com.example.dao;

import model.OrderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO extends BaseDAO {
    public void addOrderItem(OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, drink_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getDrinkId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPrice());
            stmt.executeUpdate();
        }
    }

    public OrderItem getOrderItemById(int id) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("drink_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                );
            }
        }
        return null;
    }

    public List<OrderItem> getAllOrderItems() throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM order_items";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                items.add(new OrderItem(
                    rs.getInt("order_item_id"),
                    rs.getInt("order_id"),
                    rs.getInt("drink_id"),
                    rs.getInt("quantity"),
                    rs.getDouble("price")
                ));
            }
        }
        return items;
    }

    public void updateOrderItem(OrderItem item) throws SQLException {
        String sql = "UPDATE order_items SET order_id = ?, drink_id = ?, quantity = ?, price = ? WHERE order_item_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getDrinkId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getOrderItemId());
            stmt.executeUpdate();
        }
    }

    public void deleteOrderItem(int id) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
} 