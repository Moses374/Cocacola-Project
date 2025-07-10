package com.example.demo1;

import model.Drinks;

import java.util.Objects;
import java.util.UUID;

public class CartItem {
    private final String id;
    private final Drinks drink;
    private final String selectedSize;
    private final double unitPrice;
    private int quantity;

    public CartItem(Drinks drink, String selectedSize, double unitPrice, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.drink = drink;
        this.selectedSize = selectedSize;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public Drinks getDrink() {
        return drink;
    }

    public String getDisplayName() {
        return drink != null ? drink.getName() : "Unknown Item";
    }

    public String getSelectedSize() {
        return selectedSize;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
        }
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    public double getTotalPrice() {
        return unitPrice * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) x%d - KSh %.2f each", 
                getDisplayName(), selectedSize, quantity, unitPrice);
    }
}