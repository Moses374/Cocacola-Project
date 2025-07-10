package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Drinks;

public class DrinkItemController {
    @FXML private ImageView drinkImage;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private ComboBox<String> sizeComboBox;
    @FXML private Button addToCartButton;
    
    private Drinks drink;
    private CartManager cartManager;
    
    @FXML
    public void initialize() {
        cartManager = CartManager.getInstance();
        
        // Add listener for size changes
        sizeComboBox.setOnAction(e -> updatePrice());
    }
    
    public void setDrink(Drinks drink) {
        this.drink = drink;
        
        // Set name and initial price
        nameLabel.setText(drink.getName());
        
        // Load image
        try {
            Image image = new Image(getClass().getResourceAsStream(drink.getImgSrc()));
            drinkImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + drink.getImgSrc());
        }
        
        // Set up sizes
        sizeComboBox.getItems().clear();
        sizeComboBox.getItems().addAll(drink.getAvailableSizes());
        if (!sizeComboBox.getItems().isEmpty()) {
            sizeComboBox.setValue(sizeComboBox.getItems().get(0)); // Default to first size
        }
        
        updatePrice();
    }
    
    private void updatePrice() {
        String selectedSize = sizeComboBox.getValue();
        if (selectedSize != null) {
            double price = drink.getPrice(selectedSize);
            priceLabel.setText(String.format("KSh %.2f", price));
        }
    }
    
    @FXML
    private void handleAddToCart() {
        String selectedSize = sizeComboBox.getValue();
        if (selectedSize != null) {
            double unitPrice = drink.getPrice(selectedSize);
            CartItem item = new CartItem(drink, selectedSize, unitPrice, 1);
            cartManager.addItem(item);
        }
    }
} 