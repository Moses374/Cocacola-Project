// CartItemController.java
package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartItemController {

    @FXML private ImageView productImageView;
    @FXML private Label productNameLabel;
    @FXML private Label productSizeLabel;
    @FXML private Label unitPriceLabel;
    @FXML private Button decreaseButton;
    @FXML private Label quantityLabel;
    @FXML private Button increaseButton;
    @FXML private Label totalPriceLabel;
    @FXML private Button removeButton;

    private CartItem cartItem;
    private CartManager cartManager;

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
        this.cartManager = CartManager.getInstance();
        updateDisplay();
    }

    private void updateDisplay() {
        if (cartItem == null) return;

        // Check if drink is not null before accessing its properties
        if (cartItem.getDrink() != null) {
            productNameLabel.setText(cartItem.getDrink().getName());

            // Load product image
            try {
                Image image = new Image(getClass().getResourceAsStream(cartItem.getDrink().getImgSrc()));
                productImageView.setImage(image);
            } catch (Exception e) {
                System.err.println("Error loading image: " + cartItem.getDrink().getImgSrc());
                // You can set a default image here if needed
            }
        } else {
            productNameLabel.setText(cartItem.getDisplayName());
        }

        productSizeLabel.setText(cartItem.getSelectedSize());
        unitPriceLabel.setText(String.format("KSh %.2f each", cartItem.getUnitPrice()));
        quantityLabel.setText(String.valueOf(cartItem.getQuantity()));
        totalPriceLabel.setText(String.format("KSh %.2f", cartItem.getTotalPrice()));

        // Disable decrease button if quantity is 1
        decreaseButton.setDisable(cartItem.getQuantity() <= 1);
    }

    @FXML
    private void increaseQuantity() {
        cartManager.updateItemQuantity(cartItem, cartItem.getQuantity() + 1);
        updateDisplay();
    }

    @FXML
    private void decreaseQuantity() {
        if (cartItem.getQuantity() > 1) {
            cartManager.updateItemQuantity(cartItem, cartItem.getQuantity() - 1);
            updateDisplay();
        }
    }

    @FXML
    private void removeFromCart() {
        cartManager.removeItem(cartItem);
    }
}