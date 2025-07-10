package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import model.Drinks;

public class DrinkViewController {
    @FXML private Label drinkNameLabel;
    @FXML private ImageView Drinkimg;
    @FXML private ComboBox<String> sizeComboBox;
    @FXML private Label quantityLabel;
    @FXML private Button decreaseBtn;
    @FXML private Button increaseBtn;
    @FXML private Button addToCartBtn;
    @FXML private Label drinkPriceLabel;
    @FXML private VBox chosendrinkcard;
    
    private int quantity = 1;
    private double unitPrice = 0.0;
    private Drinks currentDrink;
    
    @FXML
    public void initialize() {
        // Initialize size options
        sizeComboBox.getItems().addAll("Small", "Medium", "Large");
        sizeComboBox.setValue("Medium");
        
        // Initialize quantity
        updateQuantityLabel();
        
        // Add listener for size changes
        sizeComboBox.setOnAction(e -> updatePrice());
    }
    
    @FXML
    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            updateQuantityLabel();
            updatePrice();
        }
    }
    
    @FXML
    private void increaseQuantity() {
        quantity++;
        updateQuantityLabel();
        updatePrice();
    }
    
    @FXML
    private void addToCart() {
        if (currentDrink == null) {
            // If no drink is set, create a simple item with just the name
            String drinkName = drinkNameLabel.getText();
            String size = sizeComboBox.getValue();
            
            // Create a temporary drink object
            Drinks tempDrink = new Drinks();
            tempDrink.setName(drinkName);
            
            // Create cart item
            CartItem item = new CartItem(tempDrink, size, unitPrice, quantity);
            
            // Add to cart using CartManager
            CartManager.getInstance().addItem(item);
        } else {
            // Use the current drink object
            String size = sizeComboBox.getValue();
            CartItem item = new CartItem(currentDrink, size, unitPrice, quantity);
            CartManager.getInstance().addItem(item);
        }
        
        // Reset quantity
        quantity = 1;
        updateQuantityLabel();
        updatePrice();
    }
    
    private void updateQuantityLabel() {
        quantityLabel.setText(String.valueOf(quantity));
        // Disable decrease button if quantity is 1
        decreaseBtn.setDisable(quantity <= 1);
    }
    
    private void updatePrice() {
        double totalPrice = calculatePrice();
        drinkPriceLabel.setText(String.format("KSh %.2f", totalPrice));
    }
    
    private double calculatePrice() {
        double sizeMultiplier = 1.0;
        switch (sizeComboBox.getValue()) {
            case "Small": sizeMultiplier = 0.8; break;
            case "Large": sizeMultiplier = 1.2; break;
            default: sizeMultiplier = 1.0; // Medium
        }
        return unitPrice * quantity * sizeMultiplier;
    }
    
    public void setDrinkDetails(String name, double price, String imagePath) {
        drinkNameLabel.setText(name);
        this.unitPrice = price;
        updatePrice();
        
        // Load image if provided
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Drinkimg.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath)));
            } catch (Exception e) {
                System.err.println("Error loading image: " + imagePath);
            }
        }
    }
    
    public void setDrink(Drinks drink) {
        this.currentDrink = drink;
        drinkNameLabel.setText(drink.getName());
        
        // Set up size options if available
        if (!drink.getSizes().isEmpty()) {
            sizeComboBox.getItems().clear();
            sizeComboBox.getItems().addAll(drink.getAvailableSizes());
            sizeComboBox.setValue(sizeComboBox.getItems().get(0));
            this.unitPrice = drink.getPrice(sizeComboBox.getValue());
        }
        
        updatePrice();
        
        // Load image
        try {
            Drinkimg.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(drink.getImgSrc())));
        } catch (Exception e) {
            System.err.println("Error loading image: " + drink.getImgSrc());
        }
    }
} 