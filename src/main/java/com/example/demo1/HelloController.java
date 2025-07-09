package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Branch;
import model.Drinks;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vbox;

    @FXML
    private Label cartCountLabel;

    @FXML
    private Button viewCartButton;

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    
    private Branch currentBranch;
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the cart count label
        updateCartCount();

        // Create a grid of products
        createProductGrid();
    }
    
    /**
     * Sets the branch for this ordering interface
     * @param branch The branch this device represents
     */
    public void setBranch(Branch branch) {
        this.currentBranch = branch;
        welcomeText.setText("Welcome to " + branch.getBranchName() + " Branch");
    }
    
    /**
     * Sets the current user
     * @param user The current user
     */
    public void setUser(User user) {
        this.currentUser = user;
        // Update UI based on user role if needed
        if (user != null && "admin".equalsIgnoreCase(user.getRole())) {
            welcomeText.setText(welcomeText.getText() + " (Admin Mode)");
        }
    }

    private void createProductGrid() {
        // Sample products with images
        String[] products = {
                "Coca-Cola", "Diet Coke", "Coca-Cola Zero", "Sprite",
                "Fanta", "Dr Pepper", "7UP", "Pepsi", "Mountain Dew"
        };

        for (String product : products) {
            try {
                // Load the product item view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("CartItemView.fxml"));
                Parent productView = loader.load();
                CartItemController controller = loader.getController();

                // Set product details
                String imageName = product.toLowerCase().replace(" ", "_").replace("-", "_");
                String imagePath = "img/" + imageName + ".png";
                
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                
                // Create a drink object and pass it to the controller
                Drinks drink = new Drinks(product, imagePath, "#FFFFFF", "Soda", "Refreshing beverage");
                drink.setUnitPrice(2.99);
                
                CartItem cartItem = new CartItem();
                cartItem.setDisplayName(product);
                cartItem.setUnitPrice(2.99);
                cartItem.setImage(image);
                cartItem.setDrink(drink);
                
                controller.setCartItem(cartItem);

                // Add to the VBox
                vbox.getChildren().add(productView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addToCart(String productName, double price, int quantity, Image productImage) {
        // Check if the product is already in the cart
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getDisplayName().equals(productName)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        // If not found, add as new item
        if (!found) {
            CartItem newItem = new CartItem();
            newItem.setDisplayName(productName);
            newItem.setUnitPrice(price);
            newItem.setQuantity(quantity);
            newItem.setImage(productImage);
            cartItems.add(newItem);
        }

        // Update the cart count
        updateCartCount();
    }

    private void updateCartCount() {
        int totalItems = 0;
        for (CartItem item : cartItems) {
            totalItems += item.getQuantity();
        }
        cartCountLabel.setText(String.valueOf(totalItems));
    }

    @FXML
    private void onViewCartClicked(ActionEvent event) {
        try {
            // Load the cart view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Cart.fxml"));
            Parent root = loader.load();
            
            // Get the controller and set the cart items
            CartController controller = loader.getController();
            controller.setCartItems(cartItems);
            controller.setParentController(this);
            
            // If branch is set, pass it to the cart controller
            if (currentBranch != null) {
                controller.setBranch(currentBranch);
            }
            
            // If user is set, pass it to the cart controller
            if (currentUser != null) {
                controller.setUser(currentUser);
            }
            
            // Show the cart in a new window
            Stage stage = new Stage();
            stage.setTitle("Shopping Cart");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearCart() {
        cartItems.clear();
        updateCartCount();
    }
}