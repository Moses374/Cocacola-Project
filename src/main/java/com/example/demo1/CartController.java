package com.example.demo1;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.User;
import model.Branch;
import model.Customer;
import com.example.dao.UserDAO;
import com.example.dao.BranchDAO;
import com.example.dao.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import model.Order;
import model.OrderItem;
import com.example.dao.OrderDAO;
import com.example.dao.OrderItemDAO;
import com.example.dao.DrinkDAO;
import java.sql.Timestamp;

public class CartController implements Initializable {

    @FXML private ListView<CartItem> cartListView;
    @FXML private Label totalLabel;
    @FXML private Button backToShoppingBtn;
    @FXML private Button clearCartBtn;
    @FXML private Button checkoutBtn;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<Branch> branchComboBox;

    private CartManager cartManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Get cart manager instance
            cartManager = CartManager.getInstance();
            
            // Setup ListView
            setupCartListView();
            
            // Update total
            updateTotal();
            
            // Add listener for cart changes
            cartManager.getCartItems().addListener((ListChangeListener<CartItem>) change -> {
                while (change.next()) {
                    if (change.wasAdded() || change.wasRemoved() || change.wasUpdated()) {
                        updateTotal();
                    }
                }
            });

            // Load customers and branches for dropdowns
            loadCustomers();
            loadBranches();

            System.out.println("CartController initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing CartController: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to initialize cart view: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try {
            CustomerDAO customerDAO = new CustomerDAO();
            List<Customer> customers = customerDAO.getAllCustomers();
            customerComboBox.getItems().setAll(customers);
            if (!customers.isEmpty()) {
                customerComboBox.setValue(customers.get(0));
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load customers: " + e.getMessage());
        }
    }

    private void loadBranches() {
        try {
            BranchDAO branchDAO = new BranchDAO();
            List<Branch> branches = branchDAO.getAllBranches();
            branchComboBox.getItems().setAll(branches);
            if (!branches.isEmpty()) {
                branchComboBox.setValue(branches.get(0));
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load branches: " + e.getMessage());
        }
    }

    private void setupCartListView() {
        // Set items
        cartListView.setItems(cartManager.getCartItems());
        
        // Set cell factory for custom rendering
        cartListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(CartItem item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        // Create a custom cell with controls
                        HBox cellContent = createCartItemCell(item);
                        setGraphic(cellContent);
                    } catch (Exception e) {
                        setText(item.toString());
                        System.err.println("Error creating cell for item: " + e.getMessage());
                    }
                }
            }
        });
    }

    private HBox createCartItemCell(CartItem item) {
        // Create cell components
        Label nameLabel = new Label(item.getDisplayName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nameLabel.setPrefWidth(200);
        
        Label sizeLabel = new Label(item.getSelectedSize());
        sizeLabel.setPrefWidth(100);
        
        Label priceLabel = new Label(String.format("KSh %.2f", item.getUnitPrice()));
        priceLabel.setPrefWidth(100);
        
        // Quantity controls
        Button decreaseBtn = new Button("-");
        decreaseBtn.setOnAction(e -> {
            item.decrementQuantity();
            updateCartItem(item);
        });
        
        Label quantityLabel = new Label(String.valueOf(item.getQuantity()));
        quantityLabel.setStyle("-fx-alignment: center;");
        quantityLabel.setPrefWidth(30);
        
        Button increaseBtn = new Button("+");
        increaseBtn.setOnAction(e -> {
            item.incrementQuantity();
            updateCartItem(item);
        });
        
        Label totalLabel = new Label(String.format("KSh %.2f", item.getTotalPrice()));
        totalLabel.setPrefWidth(100);
        totalLabel.setStyle("-fx-font-weight: bold;");
        
        Button removeBtn = new Button("Remove");
        removeBtn.setStyle("-fx-background-color: #ff5722; -fx-text-fill: white;");
        removeBtn.setOnAction(e -> removeCartItem(item));
        
        // Create layout
        HBox quantityBox = new HBox(5, decreaseBtn, quantityLabel, increaseBtn);
        quantityBox.setStyle("-fx-alignment: center;");
        
        HBox cell = new HBox(15, nameLabel, sizeLabel, priceLabel, quantityBox, totalLabel, removeBtn);
        cell.setStyle("-fx-alignment: center-left; -fx-padding: 5;");
        
        return cell;
    }

    private void updateCartItem(CartItem item) {
        // Refresh the ListView
        cartListView.refresh();
        updateTotal();
    }

    private void removeCartItem(CartItem item) {
        cartManager.removeItem(item);
        updateTotal();
    }

    private void updateTotal() {
        double total = cartManager.getTotalPrice();
        totalLabel.setText(String.format("KSh %.2f", total));
        
        // Enable/disable checkout button based on cart status
        checkoutBtn.setDisable(cartManager.isEmpty());
    }

    @FXML
    private void backToShopping() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/hello-view.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) backToShoppingBtn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error navigating back to shopping: " + e.getMessage());
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to return to shopping view");
        }
    }

    @FXML
    private void clearCart() {
        if (cartManager.isEmpty()) {
            showAlert("Cart Empty", "Your cart is already empty");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cart");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This will remove all items from your cart.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cartManager.clearCart();
            updateTotal();
        }
    }

    @FXML
    private void checkout() {
        if (cartManager.isEmpty()) {
            showAlert("Empty Cart", "Please add items to your cart before checkout");
            return;
        }
        if (customerComboBox.getValue() == null) {
            showAlert("Customer Required", "Please select a customer for this order.");
            return;
        }
        if (branchComboBox.getValue() == null) {
            showAlert("Branch Required", "Please select a branch for this order.");
            return;
        }
        try {
            // 1. Save order
            OrderDAO orderDAO = new OrderDAO();
            OrderItemDAO orderItemDAO = new OrderItemDAO();
            DrinkDAO drinkDAO = new DrinkDAO();
            int customerId = customerComboBox.getValue().getCustomerId();
            int branchId = branchComboBox.getValue().getBranchId();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            Order order = new Order(customerId, branchId, now);
            int orderId = orderDAO.addOrder(order);
            if (orderId == -1) throw new Exception("Failed to save order.");
            // 2. Save order items and update stock
            for (CartItem item : cartManager.getCartItems()) {
                int drinkId = item.getDrink().getDrinkId();
                int quantity = item.getQuantity();
                double price = item.getUnitPrice();
                OrderItem orderItem = new OrderItem(orderId, drinkId, quantity, price);
                orderItemDAO.addOrderItem(orderItem);
                drinkDAO.decrementStock(drinkId, quantity);
            }
            // 3. Show confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed");
            alert.setHeaderText("Thank you for your order!");
            StringBuilder orderDetails = new StringBuilder();
            orderDetails.append("Order Summary:\n\n");
            for (CartItem item : cartManager.getCartItems()) {
                orderDetails.append(String.format("%s (%s) x%d - KSh %.2f\n", 
                        item.getDisplayName(), item.getSelectedSize(), 
                        item.getQuantity(), item.getTotalPrice()));
            }
            orderDetails.append("\nTotal: KSh ").append(String.format("%.2f", cartManager.getTotalPrice()));
            orderDetails.append("\n\nYour order will be delivered shortly.");
            alert.setContentText(orderDetails.toString());
            alert.showAndWait();
            // 4. Clear cart after successful checkout
            cartManager.clearCart();
            // 5. Return to shopping view
            backToShopping();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Checkout Error", "Failed to place order: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
            
            // Create a label with the message
            Label label = new Label(message);
            label.setWrapText(true);
            
            // Create a scroll pane for long messages
            ScrollPane scrollPane = new ScrollPane(label);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(200);
            
            // Create a dialog pane with the scroll pane
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.setContent(scrollPane);
            dialogPane.setPrefWidth(500); // Make the dialog wider
            
        alert.showAndWait();
        });
    }
}