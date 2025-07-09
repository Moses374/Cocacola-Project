// CartController.java
package com.example.demo1;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Branch;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class CartController implements Initializable {
    @FXML
    private ListView<CartItem> cartListView;

    @FXML
    private Label totalLabel;

    @FXML
    private Button checkoutButton;

    @FXML
    private Button clearCartButton;

    private ObservableList<CartItem> cartItems;
    private HelloController parentController;
    private Branch currentBranch;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the controller
    }

    public void setCartItems(ObservableList<CartItem> cartItems) {
        this.cartItems = cartItems;
        cartListView.setItems(cartItems);
        updateTotal();
    }

    public void setParentController(HelloController controller) {
        this.parentController = controller;
    }
    
    /**
     * Sets the branch for this cart
     * @param branch The branch this device represents
     */
    public void setBranch(Branch branch) {
        this.currentBranch = branch;
    }
    
    /**
     * Sets the current user
     * @param user The current user
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getUnitPrice() * item.getQuantity();
        }
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    @FXML
    private void onCheckoutClicked() {
        if (cartItems.isEmpty()) {
            showAlert("Cart is empty", "Please add items to your cart before checking out.");
            return;
        }
        
        // Create order confirmation message
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Order confirmed for ");
        
        if (currentBranch != null) {
            orderDetails.append(currentBranch.getBranchName()).append(" Branch\n\n");
        } else {
            orderDetails.append("your branch\n\n");
        }
        
        orderDetails.append("Items:\n");
        for (CartItem item : cartItems) {
            orderDetails.append(String.format("- %s x%d: $%.2f\n", 
                item.getDisplayName(), 
                item.getQuantity(), 
                item.getUnitPrice() * item.getQuantity()));
        }
        
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getUnitPrice() * item.getQuantity();
        }
        
        orderDetails.append(String.format("\nTotal: $%.2f", total));
        
        // In a real application, this would save the order to the database
        
        // Show confirmation
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Confirmed");
        alert.setHeaderText("Thank you for your order!");
        alert.setContentText(orderDetails.toString());
        alert.showAndWait();

        // Clear the cart and close the window
        if (parentController != null) {
            parentController.clearCart();
        }
        
        Stage stage = (Stage) checkoutButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onClearCartClicked() {
        cartItems.clear();
        if (parentController != null) {
            parentController.clearCart();
        }
        updateTotal();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}