package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Drinks;

public class CartManager {
    private static CartManager instance;
    private final ObservableList<CartItem> cartItems;

    private CartManager() {
        cartItems = FXCollections.observableArrayList();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }

    public void addItem(CartItem item) {
        // Check if the item with same drink and size already exists
        for (CartItem existingItem : cartItems) {
            if (existingItem.getDrink().equals(item.getDrink()) && 
                existingItem.getSelectedSize().equals(item.getSelectedSize())) {
                // Update quantity instead of adding new item
                int newQuantity = existingItem.getQuantity() + item.getQuantity();
                existingItem.setQuantity(newQuantity);
                return;
            }
        }
        // If not found, add as new item
        cartItems.add(item);
    }

    public void removeItem(CartItem item) {
        cartItems.remove(item);
    }

    public void clearCart() {
        cartItems.clear();
    }
    
    public void updateItemQuantity(CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(item);
        } else {
            item.setQuantity(newQuantity);
        }
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}