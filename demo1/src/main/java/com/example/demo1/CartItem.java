package com.example.demo1;

import javafx.scene.image.Image;
import model.Drinks;

public class CartItem {
    private String displayName;
    private String selectedSize;
    private double unitPrice;
    private int quantity;
    private Image image;
    private Drinks drink;

    // Constructors
    public CartItem() {}

    public CartItem(Drinks drink, String selectedSize, double unitPrice, int quantity) {
        this.drink = drink;
        this.displayName = drink.getName();
        this.selectedSize = selectedSize;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    // Getters
    public String getDisplayName() {
        return displayName;
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

    public double getTotalPrice() {
        return unitPrice * quantity;
    }

    public Image getImage() {
        return image;
    }

    public Drinks getDrink() {
        return drink;
    }

    // Setters
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setSelectedSize(String selectedSize) {
        this.selectedSize = selectedSize;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setDrink(Drinks drink) {
        this.drink = drink;
        if (drink != null) {
            this.displayName = drink.getName();
        }
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "displayName='" + displayName + '\'' +
                ", selectedSize='" + selectedSize + '\'' +
                ", unitPrice=" + unitPrice +
                ", quantity=" + quantity +
                '}';
    }
}