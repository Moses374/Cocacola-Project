package com.example.demo1;

import com.example.dao.DrinkDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import model.Drinks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductGridController {
    @FXML private GridPane productGrid;
    private DrinkDAO drinkDAO;
    private Map<String, Double> defaultSizes;
    
    @FXML
    public void initialize() {
        drinkDAO = new DrinkDAO();
        setupDefaultSizes();
        loadDrinksFromDatabase();
    }
    
    private void setupDefaultSizes() {
        defaultSizes = new HashMap<>();
        defaultSizes.put("Small", 0.8);
        defaultSizes.put("Medium", 1.0);
        defaultSizes.put("Large", 1.2);
    }
    
    private void loadDrinksFromDatabase() {
        try {
            List<Drinks> drinks = drinkDAO.getAllDrinks();
            int column = 0;
            int row = 0;
            
            for (Drinks drink : drinks) {
                // Set up sizes and prices based on unit price
                Map<String, Double> sizes = new HashMap<>();
                for (Map.Entry<String, Double> size : defaultSizes.entrySet()) {
                    sizes.put(size.getKey(), drink.getUnitPrice() * size.getValue());
                }
                drink.setSizes(sizes);
                
                // Set image path based on drink name
                String imageName = drink.getName().toLowerCase().replace(" ", "_").replace("-", "_");
                drink.setImgSrc("img/" + imageName + ".png");
                
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("DrinkItemView.fxml"));
                    VBox drinkBox = loader.load();
                    DrinkItemController controller = loader.getController();
                    controller.setDrink(drink);
                    
                    if (column > 2) {
                        column = 0;
                        row++;
                    }
                    
                    productGrid.add(drinkBox, column++, row);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error dialog
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR,
                "Error loading drinks from database: " + e.getMessage()
            );
            alert.showAndWait();
        }
    }
} 