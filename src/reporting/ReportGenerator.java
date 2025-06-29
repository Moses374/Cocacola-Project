package com;

package reporting;

import java.sql.*;

public class ReportGenerator {

    public static void generateSalesReport() {
        String query = "SELECT p.product_name, SUM(o.quantity) AS total_quantity, SUM(o.quantity * p.price) AS total_revenue " +
                       "FROM orders o " +
                       "JOIN products p ON o.product_id = p.id " +
                       "GROUP BY p.product_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.printf("%-20s %-15s %-15s%n", "Product", "Quantity Sold", "Total Revenue (KES)");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                String name = rs.getString("product_name");
                int qty = rs.getInt("total_quantity");
                double revenue = rs.getDouble("total_revenue");

                System.out.printf("%-20s %-15d %-15.2f%n", name, qty, revenue);
            }

        } catch (SQLException e) {
            System.err.println("Error generating sales report: " + e.getMessage());
        }
    }

    // Optional CSV export
    public static void exportReportToCSV(String filename) {
        String query = "SELECT p.product_name, SUM(o.quantity) AS total_quantity, SUM(o.quantity * p.price) AS total_revenue " +
                       "FROM orders o " +
                       "JOIN products p ON o.product_id = p.id " +
                       "GROUP BY p.product_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();
             java.io.FileWriter writer = new java.io.FileWriter(filename)) {

            writer.write("Product,Quantity Sold,Total Revenue\n");

            while (rs.next()) {
                String name = rs.getString("product_name");
                int qty = rs.getInt("total_quantity");
                double revenue = rs.getDouble("total_revenue");

                writer.write(name + "," + qty + "," + revenue + "\n");
            }

            System.out.println("Report exported to " + filename);

        } catch (Exception e) {
            System.err.println("Error exporting report to CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        generateSalesReport(); // Run report to console
        // exportReportToCSV("sales_report.csv"); // Optional file export
    }
}
