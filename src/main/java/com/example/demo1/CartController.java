// CartController.java
package com.example.demo1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import java.util.ResourceBundle;

import com.example.dao.CustomerDAO;
import com.example.dao.BranchDAO;
import com.example.dao.OrderDAO;
import com.example.dao.OrderItemDAO;
import com.example.dao.InventoryDAO;
import model.*;
import com.example.dao.BaseDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CartController implements Initializable {

    @FXML private Button backButton;
    @FXML private Button clearCartButton;
    @FXML private ScrollPane cartScrollPane;
    @FXML private VBox cartItemsContainer;
    @FXML private VBox emptyCartMessage;
    @FXML private VBox cartSummary;
    @FXML private Label totalItemsLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Button checkoutButton;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<Branch> branchComboBox;

    private CartManager cartManager;
    private CustomerDAO customerDAO = new CustomerDAO();
    private BranchDAO branchDAO = new BranchDAO();
    private OrderDAO orderDAO = new OrderDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cartManager = CartManager.getInstance();

        // Bind labels to cart manager properties
        totalItemsLabel.textProperty().bind(cartManager.totalItemsProperty().asString());
        totalAmountLabel.textProperty().bind(cartManager.totalAmountProperty().asString("KSh %.2f"));

        // Listen for cart changes to update UI
        cartManager.getCartItems().addListener((javafx.collections.ListChangeListener<CartItem>) change -> {
            Platform.runLater(this::updateCartDisplay);
        });

        // Initial display update
        updateCartDisplay();

        // Populate customer and branch combo boxes using DAOs
        try {
            customerComboBox.getItems().setAll(customerDAO.getAllCustomers());
            branchComboBox.getItems().setAll(branchDAO.getAllBranches());
        } catch (Exception e) {
            showAlert("Error", "Failed to load customers or branches: " + e.getMessage());
        }
    }

    private void updateCartDisplay() {
        cartItemsContainer.getChildren().clear();

        if (cartManager.getCartItems().isEmpty()) {
            emptyCartMessage.setVisible(true);
            cartSummary.setVisible(false);
            clearCartButton.setDisable(true);
            checkoutButton.setDisable(true);
        } else {
            emptyCartMessage.setVisible(false);
            cartSummary.setVisible(true);
            clearCartButton.setDisable(false);
            checkoutButton.setDisable(false);

            // Add cart items to container
            for (CartItem item : cartManager.getCartItems()) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/CartItemView.fxml"));
                    Node cartItemNode = loader.load();
                    CartItemController controller = loader.getController();
                    controller.setCartItem(item);
                    cartItemsContainer.getChildren().add(cartItemNode);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to load cart item: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void goBack() {
        try {
            // Navigate back to main drinks view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/hello-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to main view: " + e.getMessage());
        }
    }

    @FXML
    private void clearCart() {
        if (cartManager.getCartItems().isEmpty()) {
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Clear Cart");
        confirmAlert.setHeaderText("Are you sure you want to clear your cart?");
        confirmAlert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cartManager.clearCart();
            showAlert("Cart Cleared", "Your cart has been cleared successfully.");
        }
    }

    @FXML
    private void checkout() {
        if (cartManager.getCartItems().isEmpty()) {
            showAlert("Empty Cart", "Your cart is empty. Add items before checkout.");
            return;
        }
        Customer selectedCustomer = customerComboBox.getValue();
        Branch selectedBranch = branchComboBox.getValue();
        if (selectedCustomer == null || selectedBranch == null) {
            showAlert("Missing Info", "Please select a customer and branch before checkout.");
            return;
        }

        // Create checkout summary
        StringBuilder summary = new StringBuilder();
        summary.append("Order Summary:\n\n");

        for (CartItem item : cartManager.getCartItems()) {
            summary.append(String.format("%dx %s - KSh %.2f\n",
                    item.getQuantity(),
                    item.getDisplayName(),
                    item.getTotalPrice()));
        }

        summary.append(String.format("\nTotal: KSh %.2f", cartManager.getTotalAmount()));
        summary.append("\n\nProceed with checkout?");

        Alert checkoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        checkoutAlert.setTitle("Checkout");
        checkoutAlert.setHeaderText("Confirm Your Order");
        checkoutAlert.setContentText(summary.toString());

        Optional<ButtonType> result = checkoutAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            processCheckout(selectedCustomer, selectedBranch);
        }
    }

    private void processCheckout(Customer customer, Branch branch) {
        Connection conn = null;
        try {
            // Fix any cart items with missing drink IDs
            fixCartItemDrinkIds();
            
            // Get a connection for transaction
            conn = new BaseDAO() {}.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Create order
            Order order = new Order(customer.getCustomerId(), branch.getBranchId(), Timestamp.from(Instant.now()));
            
            // Insert order manually with the connection
            String orderSql = "INSERT INTO orders (customer_id, branch_id, order_date) VALUES (?, ?, ?)";
            int orderId;
            try (PreparedStatement stmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, order.getCustomerId());
                stmt.setInt(2, order.getBranchId());
                stmt.setTimestamp(3, order.getOrderDate());
                stmt.executeUpdate();
                
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                    System.out.println("Created order with ID: " + orderId);
                } else {
                    throw new Exception("Failed to get order ID");
                }
            }
            
            // 2. For each cart item, add order item and update inventory
            for (CartItem item : cartManager.getCartItems()) {
                // Verify drink exists
                String checkDrinkSql = "SELECT 1 FROM drinks WHERE drink_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(checkDrinkSql)) {
                    stmt.setInt(1, item.getDrink().getDrinkId());
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        throw new Exception("Drink with ID " + item.getDrink().getDrinkId() + " does not exist");
                    }
                }
                
                // Add order item
                String itemSql = "INSERT INTO order_items (order_id, drink_id, quantity, price) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(itemSql)) {
                    stmt.setInt(1, orderId);
                    stmt.setInt(2, item.getDrink().getDrinkId());
                    stmt.setInt(3, item.getQuantity());
                    stmt.setDouble(4, item.getUnitPrice());
                    stmt.executeUpdate();
                    System.out.println("Added order item for drink: " + item.getDrink().getName());
                }
                
                // Update inventory
                String updateInvSql = "UPDATE inventory SET quantity = quantity - ? WHERE drink_id = ? AND branch_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateInvSql)) {
                    stmt.setInt(1, item.getQuantity());
                    stmt.setInt(2, item.getDrink().getDrinkId());
                    stmt.setInt(3, branch.getBranchId());
                    int updated = stmt.executeUpdate();
                    if (updated == 0) {
                        throw new Exception("No inventory found for drink " + item.getDrink().getName() + " at branch " + branch.getBranchName());
                    }
                    System.out.println("Updated inventory for drink: " + item.getDrink().getName());
                }
            }
            
            // Commit transaction
            conn.commit();
            
            showAlert("Order Confirmed", "Your order has been placed successfully!");
            cartManager.clearCart();
            goBack();
        } catch (Exception e) {
            // Rollback transaction on error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            showAlert("Checkout Error", "Failed to process order: " + e.getMessage());
        } finally {
            // Close connection
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Add this method to fix cart items with missing drink IDs
    private void fixCartItemDrinkIds() {
        try {
            for (CartItem item : cartManager.getCartItems()) {
                if (item.getDrink() == null || item.getDrink().getDrinkId() == 0) {
                    // Try to find the drink by name
                    String drinkName = item.getDisplayName();
                    System.out.println("Fixing drink ID for: " + drinkName);
                    
                    String sql = "SELECT * FROM drinks WHERE name LIKE ?";
                    try (Connection conn = new BaseDAO(){}.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, "%" + drinkName + "%");
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            Drinks drink = new Drinks(
                                rs.getInt("drink_id"),
                                rs.getString("name"),
                                rs.getString("brand"),
                                rs.getDouble("unit_price")
                            );
                            item.setDrink(drink);
                            System.out.println("Fixed drink ID for " + drinkName + ": " + drink.getDrinkId());
                        } else {
                            System.out.println("Could not find matching drink for: " + drinkName);
                        }
                    }
                } else {
                    System.out.println("Drink ID already set for " + item.getDisplayName() + ": " + item.getDrink().getDrinkId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}