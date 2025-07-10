package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import model.Drinks;

import java.util.Objects;

public class ItemController {
    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView img;

    private Drinks drink;
    private MyListener myListener;

    @FXML
    private void click(MouseEvent mouseEvent) {
        if (myListener != null) {
            myListener.onClickListener(drink);
        }
    }

    public void setData(Drinks drink, MyListener myListener) {
        this.drink = drink;
        this.myListener = myListener;
        nameLabel.setText(drink.getName());
        
        // Show price range or base price
        if (!drink.getSizes().isEmpty()) {
            String firstSize = drink.getSizes().keySet().iterator().next();
            double price = drink.getPrice(firstSize);
            priceLabel.setText(String.format("KSh %.2f", price));
        } else {
            priceLabel.setText("Price N/A");
        }
        
        // Load image with error handling
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(drink.getImgSrc())));
            img.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + drink.getImgSrc());
            try {
                // Load default image if the drink image fails to load
                Image defaultImage = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/example/demo1/img/default_drink.png")));
                img.setImage(defaultImage);
            } catch (Exception ex) {
                System.err.println("Error loading default image");
            }
        }
    }
}