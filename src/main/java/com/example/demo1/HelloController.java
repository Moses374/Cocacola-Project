package com.example.demo1;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Drinks;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HelloController implements Initializable {

    // FXML Components
    @FXML private ImageView Drinkimg;
    @FXML private VBox chosendrinkcard;
    @FXML private Label drinkNameLabel;
    @FXML private Label drinkPriceLabel;
    @FXML private Label quantityLabel;
    @FXML private GridPane grid;
    @FXML private ScrollPane scroll;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> branchComboBox;
    @FXML private ComboBox<String> sizeComboBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private Button addToCartBtn;
    @FXML private Button increaseBtn;
    @FXML private Button decreaseBtn;
    @FXML private Button cartButton;
    @FXML private Label cartCountLabel;

    // Data and State
    private List<Drinks> allDrinks = new ArrayList<>();
    private List<Drinks> filteredDrinks = new ArrayList<>();
    private Drinks selectedDrink;
    private int selectedQuantity = 1;
    private MyListener myListener;
    private CartManager cartManager;

    // Constants
    private static final int COLUMNS_PER_ROW = 3;
    private static final double ITEM_WIDTH = 180.0;
    private static final double ITEM_HEIGHT = 220.0;
    private static final double GRID_HGAP = 20.0;
    private static final double GRID_VGAP = 20.0;
    private static final String DEFAULT_IMAGE_PATH = "/com/example/demo1/img/default_drink.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initializing HelloController...");

        try {
            initializeComponents();
            loadData();
            setupUI();
            setupEventListeners();

            if (!allDrinks.isEmpty()) {
                setChosenDrink(allDrinks.get(0));
                populateGrid();
            }

            updateCartCount();
            System.out.println("HelloController initialized successfully");
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
            showAlert("Initialization Error", "Failed to initialize the application properly.");
        }
    }

    private void initializeComponents() {
        // Initialize CartManager
        cartManager = CartManager.getInstance();

        // Setup cart count listener
        cartManager.getCartItems().addListener((javafx.collections.ListChangeListener<CartItem>) change -> {
            Platform.runLater(this::updateCartCount);
        });
    }

    private void loadData() {
        allDrinks.addAll(getData());
        filteredDrinks.addAll(allDrinks);
        System.out.println("Loaded " + allDrinks.size() + " drinks");
    }

    private void setupUI() {
        setupBranches();
        setupCategories();
        setupGrid();
        setupSizeSelectionListener();
    }

    private void setupEventListeners() {
        System.out.println("Setting up event listeners...");

        myListener = drink -> {
            System.out.println("Listener called for: " + drink.getName());
            setChosenDrink(drink);
        };

        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    searchDrinks();
                }
            });
        }

        if (branchComboBox != null) {
            branchComboBox.setOnAction(event -> filterByBranch());
        }

        // Category filter listener
        if (categoryComboBox != null) {
            categoryComboBox.setOnAction(event -> filterByCategory());
        }

        System.out.println("Event listeners setup complete");
    }

    private void setupBranches() {
        if (branchComboBox != null) {
            ObservableList<String> branches = FXCollections.observableArrayList(
                    "NAIROBI", "NAKURU", "MOMBASA", "KISUMU"
            );
            branchComboBox.setItems(branches);
            branchComboBox.setValue("NAIROBI");
        }
    }

    private void setupCategories() {
        if (categoryComboBox != null) {
            // Get unique categories from drinks
            List<String> categories = allDrinks.stream()
                    .map(Drinks::getCategory)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());

            categories.add(0, "All Categories");

            ObservableList<String> categoryList = FXCollections.observableArrayList(categories);
            categoryComboBox.setItems(categoryList);
            categoryComboBox.setValue("All Categories");
        }
    }

    private void setupGrid() {
        if (grid == null || scroll == null) return;

        grid.setHgap(GRID_HGAP);
        grid.setVgap(GRID_VGAP);
        grid.setPadding(new Insets(20));

        // Clear existing constraints
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();

        // Set up column constraints
        for (int i = 0; i < COLUMNS_PER_ROW; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setMinWidth(ITEM_WIDTH);
            colConstraints.setPrefWidth(ITEM_WIDTH);
            colConstraints.setMaxWidth(ITEM_WIDTH);
            colConstraints.setHgrow(Priority.SOMETIMES);
            grid.getColumnConstraints().add(colConstraints);
        }

        // Enhanced ScrollPane configuration
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setPannable(false);

        scroll.setOnScroll(event -> {
            double deltaY = event.getDeltaY();
            double scrollSpeed = 0.02;
            scroll.setVvalue(scroll.getVvalue() + (-deltaY * scrollSpeed / scroll.getContent().getBoundsInLocal().getWidth()));
            event.consume();
        });

        // Keyboard navigation
        scroll.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> scroll.setVvalue(Math.max(0, scroll.getVvalue() - 0.1));
                case DOWN -> scroll.setVvalue(Math.min(1, scroll.getVvalue() + 0.1));
                case PAGE_UP -> scroll.setVvalue(Math.max(0, scroll.getVvalue() - 0.3));
                case PAGE_DOWN -> scroll.setVvalue(Math.min(1, scroll.getVvalue() + 0.3));
                case HOME -> scroll.setVvalue(0);
                case END -> scroll.setVvalue(1);
            }
            event.consume();
        });

        scroll.setFocusTraversable(true);
    }

    private void setupSizeSelectionListener() {
        if (sizeComboBox != null) {
            sizeComboBox.setOnAction(event -> updatePriceLabel());
        }
    }

    private void updateCartCount() {
        if (cartCountLabel != null) {
            int totalItems = cartManager.getTotalItems();
            cartCountLabel.setText(totalItems > 0 ? String.valueOf(totalItems) : "");
            cartCountLabel.setVisible(totalItems > 0);
        }
    }

    private List<Drinks> getData() {
        List<Drinks> drinks = new ArrayList<>();


        drinks.add(createDrink("Fanta Orange", "/com/example/demo1/img/fanta_orange.png",
                "FF8C00", "Soft Drinks", "Refreshing orange flavored soda with natural citrus taste",
                new String[]{"300ml", "500ml", "1L"}, new double[]{50.0, 80.0, 120.0}));

        drinks.add(createDrink("Coca Cola", "/com/example/demo1/img/coca_cola.png",
                "F40009", "Soft Drinks", "The original cola taste that refreshes",
                new String[]{"300ml", "500ml", "1L"}, new double[]{55.0, 85.0, 130.0}));

        drinks.add(createDrink("Minute Maid", "/com/example/demo1/img/MinuteMaid.png",
                "FFD700", "Juices", "100% natural fruit juice with vitamins",
                new String[]{"330ml", "500ml", "1L"}, new double[]{70.0, 100.0, 150.0}));

        drinks.add(createDrink("Pepsi", "/com/example/demo1/img/pepsi.png",
                "004B93", "Soft Drinks", "Bold cola flavor for the young generation",
                new String[]{"300ml", "500ml", "1L"}, new double[]{50.0, 80.0, 125.0}));

        drinks.add(createDrink("Red Bull", "/com/example/demo1/img/red_bull.png",
                "1E90FF", "Energy Drinks", "Wings when you need them most",
                new String[]{"250ml", "350ml"}, new double[]{150.0, 200.0}));

        drinks.add(createDrink("Schweppes", "/com/example/demo1/img/Schweppes-Tonic-Water.png",
                "008B8B", "Mixers", "Premium tonic water with quinine",
                new String[]{"300ml", "500ml"}, new double[]{60.0, 90.0}));

        drinks.add(createDrink("Coca Cola Diet", "/com/example/demo1/img/coca_cola_Diet.png",
                "C0C0C0", "Soft Drinks", "Zero calories, same great taste",
                new String[]{"300ml", "500ml", "1L"}, new double[]{55.0, 85.0, 130.0}));

        drinks.add(createDrink("Sprite", "/com/example/demo1/img/sprite.png",
                "32CD32", "Soft Drinks", "Crisp lemon-lime taste that cuts through",
                new String[]{"300ml", "500ml", "1L"}, new double[]{50.0, 80.0, 120.0}));

        drinks.add(createDrink("Water", "/com/example/demo1/img/water_bottle.png",
                "87CEEB", "Water", "Pure drinking water for hydration",
                new String[]{"500ml", "1L", "1.5L"}, new double[]{30.0, 50.0, 70.0}));

        drinks.add(createDrink("7UP", "/com/example/demo1/img/7up.png",
                "00FF7F", "Soft Drinks", "The uncola with natural lemon lime flavor",
                new String[]{"300ml", "500ml", "1L"}, new double[]{50.0, 80.0, 120.0}));

        drinks.add(createDrink("Fanta Pineapple", "/com/example/demo1/img/Pineapple.png",
                "FFD700", "Soft Drinks", "Tropical pineapple flavor explosion",
                new String[]{"300ml", "500ml", "1L"}, new double[]{50.0, 80.0, 120.0}));


        return drinks;
    }

    private Drinks createDrink(String name, String imgSrc, String color, String category,
                               String description, String[] sizes, double[] prices) {
        Drinks drink = new Drinks();
        drink.setName(name);
        drink.setImgSrc(imgSrc);
        drink.setColor(color);
        drink.setCategory(category);
        drink.setDescription(description);

        for (int i = 0; i < sizes.length && i < prices.length; i++) {
            drink.addSize(sizes[i], prices[i]);
        }

        return drink;
    }

    private void setChosenDrink(Drinks drink) {
        if (drink == null) return;

        System.out.println("Setting chosen drink to: " + drink.getName());
        this.selectedDrink = drink;
        this.selectedQuantity = 1;

        // Update UI labels
        if (drinkNameLabel != null) drinkNameLabel.setText(drink.getName());
        if (quantityLabel != null) quantityLabel.setText(String.valueOf(selectedQuantity));

        // Load image with error handling
        loadDrinkImage(drink);

        // Update card color with validation
        updateCardColor(drink);

        // Setup size options
        setupSizeOptions(drink);

        System.out.println("Drink selection updated successfully");
    }

    private void loadDrinkImage(Drinks drink) {
        if (Drinkimg == null) return;

        try {
            Image image = new Image(getClass().getResourceAsStream(drink.getImgSrc()));
            if (!image.isError()) {
                Drinkimg.setImage(image);
                System.out.println("Image loaded successfully for: " + drink.getName());
            } else {
                loadDefaultImage();
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + drink.getImgSrc());
            loadDefaultImage();
        }
    }

    private void loadDefaultImage() {
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH));
            if (!defaultImage.isError()) {
                Drinkimg.setImage(defaultImage);
            }
        } catch (Exception e) {
            System.err.println("Error loading default image");
        }
    }

    private void updateCardColor(Drinks drink) {
        if (chosendrinkcard == null) return;

        try {
            String color = drink.getColor();
            if (color != null && !color.isEmpty()) {
                // Ensure color starts with #
                if (!color.startsWith("#")) {
                    color = "#" + color;
                }
                chosendrinkcard.setStyle("-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 5);");
            }
        } catch (Exception e) {
            System.err.println("Error updating card color: " + e.getMessage());
            // Set default style
            chosendrinkcard.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 30;");
        }
    }

    private void setupSizeOptions(Drinks drink) {
        if (sizeComboBox == null) return;

        try {
            ObservableList<String> sizes = FXCollections.observableArrayList(drink.getSizes().keySet());
            sizeComboBox.setItems(sizes);

            if (!sizes.isEmpty()) {
                sizeComboBox.setValue(sizes.get(0));
                updatePriceLabel();
            }
        } catch (Exception e) {
            System.err.println("Error setting up size options: " + e.getMessage());
        }
    }

    private void updatePriceLabel() {
        if (selectedDrink != null && sizeComboBox != null &&
                sizeComboBox.getValue() != null && drinkPriceLabel != null) {
            try {
                double price = selectedDrink.getPrice(sizeComboBox.getValue());
                double totalPrice = price * selectedQuantity;
                drinkPriceLabel.setText(String.format("KSh %.2f", totalPrice));
            } catch (Exception e) {
                System.err.println("Error updating price label: " + e.getMessage());
                drinkPriceLabel.setText("Price unavailable");
            }
        }
    }

    private void populateGrid() {
        if (grid == null) return;

        System.out.println("Populating grid with " + filteredDrinks.size() + " drinks...");

        Platform.runLater(() -> {
            grid.getChildren().clear();
            grid.getRowConstraints().clear();

            int column = 0;
            int row = 0;

            try {
                for (int i = 0; i < filteredDrinks.size(); i++) {
                    Drinks drink = filteredDrinks.get(i);

                    if (loadDrinkItem(drink, column, row)) {
                        column++;
                        if (column == COLUMNS_PER_ROW) {
                            column = 0;
                            row++;
                        }
                    }
                }

                optimizeGridDimensions();
                scroll.setVvalue(0);

                System.out.println("Grid populated successfully");
            } catch (Exception e) {
                System.err.println("Error populating grid: " + e.getMessage());
                e.printStackTrace();
                showAlert("Error", "Failed to load drinks catalog");
            }
        });
    }

    private boolean loadDrinkItem(Drinks drink, int column, int row) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/example/demo1/Item.fxml"));

            if (fxmlLoader.getLocation() == null) {
                System.err.println("Could not find Item.fxml file!");
                return false;
            }

            AnchorPane anchorPane = fxmlLoader.load();
            ItemController itemController = fxmlLoader.getController();

            if (itemController == null || myListener == null) {
                System.err.println("Controller or listener is null for item: " + drink.getName());
                return false;
            }

            itemController.setData(drink, myListener);
            grid.add(anchorPane, column, row);

            // Add row constraints if needed
            while (grid.getRowConstraints().size() <= row) {
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setMinHeight(ITEM_HEIGHT);
                rowConstraints.setPrefHeight(ITEM_HEIGHT);
                rowConstraints.setMaxHeight(ITEM_HEIGHT);
                rowConstraints.setVgrow(Priority.SOMETIMES);
                grid.getRowConstraints().add(rowConstraints);
            }

            return true;
        } catch (IOException e) {
            System.err.println("Error loading item for " + drink.getName() + ": " + e.getMessage());
            return false;
        }
    }

    private void optimizeGridDimensions() {
        if (grid == null) return;

        int totalRows = (int) Math.ceil((double) filteredDrinks.size() / COLUMNS_PER_ROW);
        double totalHeight = (totalRows * ITEM_HEIGHT) + ((totalRows - 1) * GRID_VGAP) + 40;
        double gridWidth = (COLUMNS_PER_ROW * ITEM_WIDTH) + ((COLUMNS_PER_ROW - 1) * GRID_HGAP) + 40;

        grid.setPrefHeight(totalHeight);
        grid.setMinHeight(totalHeight);
        grid.setPrefWidth(gridWidth);
        grid.setMinWidth(gridWidth);

        System.out.println("Grid optimized - Width: " + gridWidth + ", Height: " + totalHeight);
    }

    @FXML
    private void increaseQuantity() {
        if (selectedQuantity < 99) { // Add reasonable limit
            selectedQuantity++;
            if (quantityLabel != null) quantityLabel.setText(String.valueOf(selectedQuantity));
            updatePriceLabel();
        }
    }

    @FXML
    private void decreaseQuantity() {
        if (selectedQuantity > 1) {
            selectedQuantity--;
            if (quantityLabel != null) quantityLabel.setText(String.valueOf(selectedQuantity));
            updatePriceLabel();
        }
    }

    @FXML
    private void addToCart() {
        if (!validateCartAddition()) return;

        try {
            String selectedSize = sizeComboBox.getValue();
            double unitPrice = selectedDrink.getPrice(selectedSize);

            // Create and add CartItem
            CartItem cartItem = new CartItem(selectedDrink, selectedSize, unitPrice, selectedQuantity);
            cartManager.addItem(cartItem);

            showAddToCartConfirmation(cartItem);
            resetQuantity();

            System.out.println("Item added to cart: " + selectedDrink.getName() +
                    " (" + selectedSize + ") x" + selectedQuantity);
        } catch (Exception e) {
            System.err.println("Error adding to cart: " + e.getMessage());
            showAlert("Error", "Failed to add item to cart. Please try again.");
        }
    }

    private boolean validateCartAddition() {
        if (selectedDrink == null) {
            showAlert("No Selection", "Please select a drink first.");
            return false;
        }

        if (sizeComboBox == null || sizeComboBox.getValue() == null) {
            showAlert("Size Required", "Please select a size before adding to cart.");
            return false;
        }

        return true;
    }

    private void showAddToCartConfirmation(CartItem cartItem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Added to Cart");
        alert.setHeaderText("Item Successfully Added!");
        alert.setContentText(String.format(
                "%dx %s (%s) - KSh %.2f each\n" +
                        "Total: KSh %.2f\n\n" +
                        "Cart now has %d items",
                cartItem.getQuantity(),
                cartItem.getDisplayName(),
                cartItem.getSelectedSize(),
                cartItem.getUnitPrice(),
                cartItem.getTotalPrice(),
                cartManager.getTotalItems()));

        ButtonType viewCartButton = new ButtonType("View Cart");
        ButtonType continueShoppingButton = new ButtonType("Continue Shopping", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(viewCartButton, continueShoppingButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == viewCartButton) {
                openCart();
            }
        });
    }

    private void resetQuantity() {
        selectedQuantity = 1;
        if (quantityLabel != null) quantityLabel.setText(String.valueOf(selectedQuantity));
        updatePriceLabel();
    }

    @FXML
    private void openCart() {
        navigateToView("/com/example/demo1/Cart.fxml", "Shopping Cart", addToCartBtn);
    }

    @FXML
    private void viewCart() {
        openCart();
    }

    @FXML
    private void searchDrinks() {
        if (searchField == null) return;

        String searchText = searchField.getText().toLowerCase().trim();
        applyFilters(searchText,
                categoryComboBox != null ? categoryComboBox.getValue() : "All Categories");
    }

    @FXML
    private void filterByCategory() {
        if (categoryComboBox == null) return;

        String searchText = searchField != null ? searchField.getText().toLowerCase().trim() : "";
        applyFilters(searchText, categoryComboBox.getValue());
    }

    @FXML
    private void filterByBranch() {
        if (branchComboBox == null) return;

        String selectedBranch = branchComboBox.getValue();
        if (selectedBranch != null) {
            System.out.println("Selected branch: " + selectedBranch);
            // You can implement branch-specific logic here
            showAlert("Branch Selected", "You have selected: " + selectedBranch +
                    "\nAll drinks are available at this location.");
        }
    }

    private void applyFilters(String searchText, String selectedCategory) {
        filteredDrinks.clear();

        for (Drinks drink : allDrinks) {
            boolean matchesSearch = searchText.isEmpty() ||
                    drink.getName().toLowerCase().contains(searchText) ||
                    drink.getCategory().toLowerCase().contains(searchText) ||
                    drink.getDescription().toLowerCase().contains(searchText);

            boolean matchesCategory = selectedCategory == null ||
                    "All Categories".equals(selectedCategory) ||
                    drink.getCategory().equals(selectedCategory);

            if (matchesSearch && matchesCategory) {
                filteredDrinks.add(drink);
            }
        }

        populateGrid();

        // Update selected drink if current one is not in filtered results
        if (!filteredDrinks.contains(selectedDrink) && !filteredDrinks.isEmpty()) {
            setChosenDrink(filteredDrinks.get(0));
        }

        System.out.println("Filters applied - " + filteredDrinks.size() + " drinks shown");
    }

    private void navigateToView(String fxmlPath, String title, javafx.scene.Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) sourceNode.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to open " + title + ": " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    // Getter methods for testing or external access
    public List<Drinks> getAllDrinks() {
        return new ArrayList<>(allDrinks);
    }

    public List<Drinks> getFilteredDrinks() {
        return new ArrayList<>(filteredDrinks);
    }

    public Drinks getSelectedDrink() {
        return selectedDrink;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public CartManager getCartManager() {
        return cartManager;
    }

    // Additional utility methods
    public void refreshDrinks() {
        allDrinks.clear();
        filteredDrinks.clear();
        loadData();
        populateGrid();
        if (!allDrinks.isEmpty()) {
            setChosenDrink(allDrinks.get(0));
        }
    }

    public void setSelectedQuantity(int quantity) {
        if (quantity > 0 && quantity <= 99) {
            this.selectedQuantity = quantity;
            if (quantityLabel != null) quantityLabel.setText(String.valueOf(selectedQuantity));
            updatePriceLabel();
        }
    }
}