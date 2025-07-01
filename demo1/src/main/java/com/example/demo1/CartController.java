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
import java.util.Optional;
import java.util.ResourceBundle;

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

    private CartManager cartManager;

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
            // Process checkout - you can implement payment processing here
            processCheckout();
        }
    }

    private void processCheckout() {
        // Simulate checkout process
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Order Confirmed");
        successAlert.setHeaderText("Thank you for your order!");
        successAlert.setContentText(String.format(
                "Your order of %d items totaling KSh %.2f has been confirmed.\n" +
                        "You will receive a confirmation email shortly.",
                cartManager.getTotalItems(),
                cartManager.getTotalAmount()));

        successAlert.showAndWait();

        // Clear cart after successful checkout
        cartManager.clearCart();

        // Navigate back to main view
        goBack();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}