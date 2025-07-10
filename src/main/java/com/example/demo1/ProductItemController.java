package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Drinks;

public class ProductItemController {
    @FXML private VBox productBox;
    @FXML private ImageView productImage;
    @FXML private Label nameLabel;
    @FXML private Label priceLabel;
    
    private Drinks drink;
    private HelloController mainController;
    
    @FXML
    public void initialize() {
        // Add click event to the entire box
        productBox.setOnMouseClicked(this::onProductClicked);
    }
    
    public void setDrink(Drinks drink) {
        this.drink = drink;
        
        // Set name
        nameLabel.setText(drink.getName());
        
        // Set price range
        String priceRange = formatPriceRange(drink.getUnitPrice());
        priceLabel.setText(priceRange);
        
        // Load image
        try {
            String imageName = drink.getName().toLowerCase().replace(" ", "_").replace("-", "_");
            // Handle specific cases
            if (imageName.contains("fanta")) {
                imageName = "fanta_orange";
            } else if (imageName.contains("coca")) {
                imageName = "coca_cola";
            } else if (imageName.contains("minute")) {
                imageName = "minute_maid";
            } else if (imageName.contains("schweppes")) {
                imageName = "schweppes";
            } else if (imageName.contains("7up")) {
                imageName = "7up";
            }
            
            Image image = new Image(getClass().getResourceAsStream("img/" + imageName + ".png"));
            productImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image for " + drink.getName() + ": " + e.getMessage());
        }
    }
    
    private String formatPriceRange(double basePrice) {
        double smallPrice = basePrice * 0.6;
        double mediumPrice = basePrice;
        double largePrice = basePrice * 1.5;
        
        return String.format("KSh %.2f - %.2f", smallPrice, largePrice);
    }
    
    private void onProductClicked(MouseEvent event) {
        if (mainController != null && drink != null) {
            mainController.setChosenDrink(drink);
        }
    }
    
    public void setMainController(HelloController controller) {
        this.mainController = controller;
    }
} 