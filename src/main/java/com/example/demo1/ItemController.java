package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Drinks;

public class ItemController {

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private VBox container;

    private Drinks drink;
    private MyListener myListener;

    @FXML
    private void click(MouseEvent mouseEvent) {
        System.out.println("Item clicked: " + (drink != null ? drink.getName() : "null"));
        if (myListener != null && drink != null) {
            myListener.onClickListener(drink);
        }
    }

    public void setData(Drinks drink, MyListener myListener) {
        this.drink = drink;
        this.myListener = myListener;

        if (drink == null) {
            System.err.println("Warning: Drink is null in setData");
            return;
        }

        System.out.println("Setting data for: " + drink.getName());

        // Set the drink name
        if (nameLabel != null) {
            nameLabel.setText(drink.getName());
        }

        // Set the price range
        if (priceLabel != null) {
            priceLabel.setText(drink.getPriceRange());
        }

        // Load and set the image
        try {
            if (drink.getImgSrc() != null) {
                Image image = new Image(getClass().getResourceAsStream(drink.getImgSrc()));
                if (img != null) {
                    img.setImage(image);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading image for " + drink.getName() + ": " + drink.getImgSrc());
            e.printStackTrace();
            // Set a default image or leave empty
        }

        // Optional: Set background color based on drink color
        if (container != null && drink.getColor() != null) {
            // Add a subtle color hint to the item
            container.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #" +
                    drink.getColor() + "20); -fx-background-radius: 15;");
        }
    }
}