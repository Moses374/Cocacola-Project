package com.example.dao;

import model.Drinks;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DrinkDAO extends BaseDAO {
    public void addDrink(Drinks drink) throws SQLException {
        String sql = "INSERT INTO drinks (name, brand, unit_price) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, drink.getName());
            stmt.setString(2, drink.getBrand());
            stmt.setDouble(3, drink.getUnitPrice());
            stmt.executeUpdate();
        }
    }

    public Drinks getDrinkById(int id) throws SQLException {
        String sql = "SELECT * FROM drinks WHERE drink_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Drinks(
                    rs.getInt("drink_id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getDouble("unit_price")
                );
            }
        }
        return null;
    }

    public List<Drinks> getAllDrinks() throws SQLException {
        List<Drinks> drinks = new ArrayList<>();
        String sql = "SELECT * FROM drinks";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                drinks.add(new Drinks(
                    rs.getInt("drink_id"),
                    rs.getString("name"),
                    rs.getString("brand"),
                    rs.getDouble("unit_price")
                ));
            }
        }
        return drinks;
    }

    public void updateDrink(Drinks drink) throws SQLException {
        String sql = "UPDATE drinks SET name = ?, brand = ?, unit_price = ? WHERE drink_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, drink.getName());
            stmt.setString(2, drink.getBrand());
            stmt.setDouble(3, drink.getUnitPrice());
            stmt.setInt(4, drink.getDrinkId());
            stmt.executeUpdate();
        }
    }

    public void deleteDrink(int id) throws SQLException {
        String sql = "DELETE FROM drinks WHERE drink_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void decrementStock(int drinkId, int quantity) throws SQLException {
        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE drink_id = ? AND quantity >= ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, drinkId);
            stmt.setInt(3, quantity);
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Not enough stock for drink ID: " + drinkId);
            }
        }
    }
} 