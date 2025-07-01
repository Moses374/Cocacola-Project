package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CartManager {
    private static CartManager instance;
    private ObservableList<CartItem> cartItems;

    private CartManager() {
        cartItems = FXCollections.observableArrayList();
    }

    // Singleton pattern
    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Add item to cart
    public void addItem(CartItem item) {
        // Check if item already exists in cart
        for (CartItem existingItem : cartItems) {
            if (existingItem.getDisplayName().equals(item.getDisplayName()) &&
                    existingItem.getSelectedSize().equals(item.getSelectedSize())) {
                // Update quantity instead of adding new item
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        // Add new item if not found
        cartItems.add(item);
    }

    // Remove item from cart
    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    // Update item quantity
    public void updateItemQuantity(CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(item);
        } else {
            item.setQuantity(newQuantity);
        }
    }

    // Get all cart items
    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    // Get total number of items
    public int getTotalItems() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    // Get total amount
    public double getTotalAmount() {
        return cartItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    // Clear cart
    public void clearCart() {
        cartItems.clear();
    }

    // Check if cart is empty
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    // Get cart size
    public int getCartSize() {
        return cartItems.size();
    }

    // Property methods for JavaFX binding
    private javafx.beans.property.IntegerProperty totalItemsProperty;
    private javafx.beans.property.DoubleProperty totalAmountProperty;

    public javafx.beans.property.IntegerProperty totalItemsProperty() {
        if (totalItemsProperty == null) {
            totalItemsProperty = new javafx.beans.property.SimpleIntegerProperty(getTotalItems());
            // Update property when cart changes
            cartItems.addListener((javafx.collections.ListChangeListener<CartItem>) change -> {
                totalItemsProperty.set(getTotalItems());
                if (totalAmountProperty != null) {
                    totalAmountProperty.set(getTotalAmount());
                }
            });
        }
        return totalItemsProperty;
    }

    public javafx.beans.property.DoubleProperty totalAmountProperty() {
        if (totalAmountProperty == null) {
            totalAmountProperty = new javafx.beans.property.SimpleDoubleProperty(getTotalAmount());
        }
        return totalAmountProperty;
    }
}