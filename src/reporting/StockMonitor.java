package com;

package reporting;

import java.sql.*;

public class StockMonitor {

    public static void checkLowStock() {
        String query = "SELECT product_name, stock FROM products WHERE stock < 10";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("⚠ LOW STOCK ALERTS:");
            boolean found = false;

            while (rs.next()) {
                String name = rs.getString("product_name");
                int stock = rs.getInt("stock");
                System.out.println("→ " + name + " only has " + stock + " units left.");
                found = true;
            }

            if (!found) {
                System.out.println("All products have sufficient stock.");
            }

        } catch (SQLException e) {
            System.err.println("Error checking stock: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        checkLowStock();
    }
}
