package model;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.example.dao.InventoryDAO;
import com.example.dao.BranchDAO;
import com.example.dao.DrinkDAO;
import com.example.dao.OrderDAO;
import com.example.dao.CustomerDAO;
import com.example.dao.OrderItemDAO;

public class HeadquartersDashboard extends JFrame {
    // Theme colors - flat design
    private Color primaryColor = new Color(237, 28, 36);    // Coca-Cola red
    private Color accentColor = new Color(30, 39, 46);      // Dark slate
    private Color backgroundColor = new Color(248, 249, 250); // Off white
    private Color cardColor = Color.WHITE;
    
    // GUI components
    private JPanel mainPanel;
    private JTable stockTable;
    private JPanel alertsPanel;
    private JPanel chartPanel;
    private JTextField nameField, priceField, stockField;
    private JComboBox<String> productBox;
    private JButton refreshButton;
    
    private InventoryDAO inventoryDAO = new InventoryDAO();
    private BranchDAO branchDAO = new BranchDAO();
    private DrinkDAO drinkDAO = new DrinkDAO();
    private OrderDAO orderDAO = new OrderDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private OrderItemDAO orderItemDAO = new OrderItemDAO();
    
    public HeadquartersDashboard() {
        setTitle("Coca-Cola HQ");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set up main container
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Add components
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createTabbedPane(), BorderLayout.CENTER);
        
        // Add refresh button
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshDashboard());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(refreshButton);
        mainPanel.add(topPanel, BorderLayout.SOUTH);
        
        // Set as content pane
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        
        // Initial data load
        refreshDashboard();
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        
        // Logo and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🥤");
        logoLabel.setFont(new Font("Dialog", Font.PLAIN, 28));
        titlePanel.add(logoLabel);
        
        JLabel titleLabel = new JLabel("Headquarters Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titlePanel.add(titleLabel);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Date display
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        JLabel dateLabel = new JLabel(dateFormat.format(new Date()));
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        header.add(dateLabel, BorderLayout.EAST);
        
        return header;
    }
    
    private JTabbedPane createTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // Inventory tab
        tabs.addTab("Inventory", createStyledIcon("📋"), createInventoryPanel());
        
        // Low Stock Alerts tab
        tabs.addTab("Low Stock Alerts", createStyledIcon("⚠️"), createAlertsPanel());
        
        // Sales Reports tab
        tabs.addTab("Sales Reports", createStyledIcon("📊"), createReportsPanel());
        
        // Visual Overview tab
        tabs.addTab("Visual Overview", createStyledIcon("📈"), createVisualPanel());
        
        return tabs;
    }
    
    private ImageIcon createStyledIcon(String emoji) {
        // This is a simple way to use emojis as icons for students
        // In a real app you'd use proper icons
        JLabel label = new JLabel(emoji);
        label.setFont(new Font("Dialog", Font.PLAIN, 16));
        return null; // Return null to use text-only tabs - simpler for students
    }
    
    // INVENTORY PANEL
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        
        // Table for inventory
        String[] columns = {"Product", "Price (KES)", "Stock", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        stockTable = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
            
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                
                // Color rows based on stock status
                if (column == 3 && "LOW STOCK".equals(getValueAt(row, 3))) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
        };
        
        // Style the table
        stockTable.setRowHeight(30);
        stockTable.setShowGrid(false);
        stockTable.setIntercellSpacing(new Dimension(0, 0));
        stockTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        stockTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(stockTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Product management form in a card
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(cardColor);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Form title
        JLabel formTitle = new JLabel("Add or Edit Products");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Dropdown for existing products
        JPanel selectPanel = new JPanel(new BorderLayout(5, 0));
        selectPanel.setOpaque(false);
        selectPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel selectLabel = new JLabel("Select Product: ");
        productBox = new JComboBox<>();
        
        selectPanel.add(selectLabel, BorderLayout.WEST);
        selectPanel.add(productBox, BorderLayout.CENTER);
        formPanel.add(selectPanel);
        formPanel.add(Box.createVerticalStrut(10));
        
        // Input fields
        JPanel inputsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputsPanel.setOpaque(false);
        inputsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        nameField = new JTextField(15);
        priceField = new JTextField(15);
        stockField = new JTextField(15);
        
        inputsPanel.add(new JLabel("Product Name:"));
        inputsPanel.add(nameField);
        inputsPanel.add(new JLabel("Price (KES):"));
        inputsPanel.add(priceField);
        inputsPanel.add(new JLabel("Stock:"));
        inputsPanel.add(stockField);
        
        formPanel.add(inputsPanel);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton addButton = createStyledButton("Add New", primaryColor);
        JButton updateButton = createStyledButton("Update", accentColor);
        
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        formPanel.add(buttonsPanel);
        
        // Add listeners
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        
        try {
            productBox.removeAllItems();
            for (Drinks d : drinkDAO.getAllDrinks()) productBox.addItem(d.getName());
        } catch (Exception e) { e.printStackTrace(); }
        
        panel.add(formPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    // LOW STOCK ALERTS PANEL
    private JPanel createAlertsPanel() {
        alertsPanel = new JPanel(new BorderLayout());
        JTextArea alertsArea = new JTextArea();
        alertsArea.setEditable(false);
        try {
            List<Inventory> lowStock = inventoryDAO.getAllInventory().stream()
                .filter(i -> i.getQuantity() < i.getThreshold())
                .toList();
            StringBuilder sb = new StringBuilder();
            for (Inventory inv : lowStock) {
                Branch branch = branchDAO.getAllBranches().stream().filter(b -> b.getBranchId() == inv.getBranchId()).findFirst().orElse(null);
                Drinks drink = drinkDAO.getAllDrinks().stream().filter(d -> d.getDrinkId() == inv.getDrinkId()).findFirst().orElse(null);
                sb.append("LOW STOCK: ")
                  .append(drink != null ? drink.getName() : "Drink#"+inv.getDrinkId())
                  .append(" at ")
                  .append(branch != null ? branch.getBranchName() : "Branch#"+inv.getBranchId())
                  .append(" (Qty: ").append(inv.getQuantity()).append(", Threshold: ").append(inv.getThreshold()).append(")\n");
            }
            alertsArea.setText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            alertsArea.setText("Error loading alerts: " + e.getMessage());
        }
        alertsPanel.add(new JScrollPane(alertsArea), BorderLayout.CENTER);
        return alertsPanel;
    }
    
    // SALES REPORTS PANEL
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JTextArea customersArea = new JTextArea();
        JTextArea ordersArea = new JTextArea();
        JTextArea salesArea = new JTextArea();
        JTextArea transferArea = new JTextArea();
        customersArea.setEditable(false);
        ordersArea.setEditable(false);
        salesArea.setEditable(false);
        transferArea.setEditable(false);
        try {
            // Customers who made orders
            StringBuilder sb = new StringBuilder("Customers who made orders:\n");
            for (Order order : orderDAO.getAllOrders()) {
                Customer c = customerDAO.getCustomerById(order.getCustomerId());
                if (c != null) sb.append(c.getName()).append(" (Contact: ").append(c.getContact()).append(")\n");
            }
            customersArea.setText(sb.toString());
            // Orders per branch
            sb = new StringBuilder("Orders per branch:\n");
            for (Branch branch : branchDAO.getAllBranches()) {
                long count = orderDAO.getAllOrders().stream().filter(o -> o.getBranchId() == branch.getBranchId()).count();
                sb.append(branch.getBranchName()).append(": ").append(count).append("\n");
            }
            ordersArea.setText(sb.toString());
            // Sales per branch
            sb = new StringBuilder("Sales per branch:\n");
            for (Branch branch : branchDAO.getAllBranches()) {
                double total = 0;
                for (Order order : orderDAO.getAllOrders()) {
                    if (order.getBranchId() == branch.getBranchId()) {
                        for (OrderItem item : orderItemDAO.getAllOrderItems()) {
                            if (item.getOrderId() == order.getOrderId()) {
                                total += item.getPrice() * item.getQuantity();
                            }
                        }
                    }
                }
                sb.append(branch.getBranchName()).append(": KSh ").append(String.format("%.2f", total)).append("\n");
            }
            salesArea.setText(sb.toString());
        } catch (Exception e) {
            customersArea.setText("Error: " + e.getMessage());
            ordersArea.setText("Error: " + e.getMessage());
            salesArea.setText("Error: " + e.getMessage());
        }
        // Stock transfer UI
        JPanel transferPanel = new JPanel(new FlowLayout());
        JComboBox<Drinks> drinkBox = new JComboBox<>();
        JComboBox<Branch> branchBox = new JComboBox<>();
        JTextField qtyField = new JTextField(5);
        JButton transferBtn = new JButton("Transfer Stock from HQ");
        try {
            drinkBox.removeAllItems();
            for (Drinks d : drinkDAO.getAllDrinks()) drinkBox.addItem(d);
            branchBox.removeAllItems();
            for (Branch b : branchDAO.getAllBranches()) if (!b.getBranchName().equalsIgnoreCase("NAIROBI HQ")) branchBox.addItem(b);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
        transferPanel.add(new JLabel("Drink:"));
        transferPanel.add(drinkBox);
        transferPanel.add(new JLabel("To Branch:"));
        transferPanel.add(branchBox);
        transferPanel.add(new JLabel("Qty:"));
        transferPanel.add(qtyField);
        transferPanel.add(transferBtn);
        transferBtn.addActionListener(e -> {
            Drinks drink = (Drinks) drinkBox.getSelectedItem();
            Branch dest = (Branch) branchBox.getSelectedItem();
            int qty;
            try { qty = Integer.parseInt(qtyField.getText()); } catch (Exception ex) { JOptionPane.showMessageDialog(panel, "Invalid quantity"); return; }
            try {
                // Decrement HQ inventory
                Branch hq = branchDAO.getAllBranches().stream().filter(b -> b.getBranchName().equalsIgnoreCase("NAIROBI HQ")).findFirst().orElse(null);
                if (hq == null) throw new Exception("HQ not found");
                Inventory hqInv = inventoryDAO.getAllInventory().stream().filter(i -> i.getDrinkId() == drink.getDrinkId() && i.getBranchId() == hq.getBranchId()).findFirst().orElse(null);
                if (hqInv == null || hqInv.getQuantity() < qty) throw new Exception("Insufficient HQ stock");
                hqInv.setQuantity(hqInv.getQuantity() - qty);
                inventoryDAO.updateInventory(hqInv);
                // Increment branch inventory
                Inventory destInv = inventoryDAO.getAllInventory().stream().filter(i -> i.getDrinkId() == drink.getDrinkId() && i.getBranchId() == dest.getBranchId()).findFirst().orElse(null);
                if (destInv == null) {
                    destInv = new Inventory(drink.getDrinkId(), dest.getBranchId(), qty, 10);
                    inventoryDAO.addInventory(destInv);
                } else {
                    destInv.setQuantity(destInv.getQuantity() + qty);
                    inventoryDAO.updateInventory(destInv);
                }
                JOptionPane.showMessageDialog(panel, "Stock transferred successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error: " + ex.getMessage());
            }
        });
        // Add all to panel
        panel.add(new JScrollPane(customersArea));
        panel.add(new JScrollPane(ordersArea));
        panel.add(new JScrollPane(salesArea));
        panel.add(transferPanel);
        return panel;
    }
    
    // VISUAL OVERVIEW PANEL
    private JPanel createVisualPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        
        // Title
        JLabel chartTitle = new JLabel("Product Sales Overview", SwingConstants.CENTER);
        chartTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        chartTitle.setForeground(primaryColor);
        panel.add(chartTitle, BorderLayout.NORTH);
        
        // Create chart panel
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawModernChart(g);
            }
        };
        chartPanel.setBackground(cardColor);
        chartPanel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        panel.add(chartPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // HELPER METHODS
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void loadProductData() {
        try {
            DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
            model.setRowCount(0);
            productBox.removeAllItems();
            // Use drinks for names/prices, inventory for stock
            for (Drinks drink : drinkDAO.getAllDrinks()) {
                // Find inventory for HQ (branchId=1)
                Inventory inv = inventoryDAO.getAllInventory().stream()
                    .filter(i -> i.getDrinkId() == drink.getDrinkId() && i.getBranchId() == 1)
                    .findFirst().orElse(null);
                int stock = inv != null ? inv.getQuantity() : 0;
                String status = inv != null && stock < inv.getThreshold() ? "LOW STOCK" : "In Stock";
                model.addRow(new Object[]{
                    drink.getName(),
                    drink.getUnitPrice(),
                    stock,
                    status
                });
                productBox.addItem(drink.getName());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void updateLowStockAlerts() {
        // Clear existing alerts
        alertsPanel.removeAll();
        boolean hasAlerts = false;
        try {
            // Check each product
            for (Inventory inv : inventoryDAO.getAllInventory()) {
                Drinks drink = drinkDAO.getDrinkById(inv.getDrinkId());
                if (inv.getQuantity() < inv.getThreshold()) {
                    hasAlerts = true;
                    
                    // Create alert card
                    JPanel alertCard = new JPanel(new BorderLayout(10, 10));
                    alertCard.setBackground(cardColor);
                    alertCard.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 5, 0, 0, Color.RED),
                        new EmptyBorder(15, 15, 15, 15)
                    ));
                    alertCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
                    
                    // Product info
                    JPanel infoPanel = new JPanel(new GridLayout(2, 1));
                    infoPanel.setOpaque(false);
                    
                    JLabel nameLabel = new JLabel(drink != null ? drink.getName() : ("Drink#" + inv.getDrinkId()));
                    nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                    
                    JLabel stockLabel = new JLabel("Only " + inv.getQuantity() + " units remaining");
                    stockLabel.setForeground(Color.RED);
                    
                    infoPanel.add(nameLabel);
                    infoPanel.add(stockLabel);
                    alertCard.add(infoPanel, BorderLayout.CENTER);
                    
                    // Restock button
                    JButton restockButton = createStyledButton("Restock", primaryColor);
                    restockButton.addActionListener(e -> {
                        String input = JOptionPane.showInputDialog(
                            this, 
                            "Enter quantity to add:",
                            "Restock " + (drink != null ? drink.getName() : ("Drink#" + inv.getDrinkId())),
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        try {
                            int qty = Integer.parseInt(input);
                            if (qty > 0) {
                                inv.setQuantity(inv.getQuantity() + qty);
                                inventoryDAO.updateInventory(inv);
                                JOptionPane.showMessageDialog(this, 
                                    "Added " + qty + " units to " + (drink != null ? drink.getName() : ("Drink#" + inv.getDrinkId())));
                                updateLowStockAlerts();
                                loadProductData();
                                chartPanel.repaint();
                            }
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, 
                                "Please enter a valid number", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    
                    alertCard.add(restockButton, BorderLayout.EAST);
                    
                    // Add to panel with spacing
                    alertsPanel.add(alertCard);
                    alertsPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        if (!hasAlerts) {
            // No alerts message
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.setBackground(cardColor);
            messagePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel messageLabel = new JLabel("No low stock alerts at this time", SwingConstants.CENTER);
            messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
            
            messagePanel.add(messageLabel, BorderLayout.CENTER);
            alertsPanel.add(messagePanel);
        }
        
        alertsPanel.revalidate();
        alertsPanel.repaint();
    }
    
    private void drawModernChart(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        
        // Draw chart background
        g2.setColor(cardColor);
        g2.fillRect(0, 0, width, height);
        
        int padding = 50;
        int chartWidth = width - (padding * 2);
        int chartHeight = height - (padding * 2);
        int barSpace = 40;
        
        // Get product data from server
        int productCount = 0;
        try {
            productCount = inventoryDAO.getAllInventory().size();
        } catch (Exception e) { e.printStackTrace(); }
        if (productCount == 0) return;
        
        // Calculate bar width based on number of products
        int barWidth = Math.min(70, (chartWidth / productCount) - barSpace);
        
        // Find max stock for scaling
        int maxStock = 0;
        try {
            for (Inventory inv : inventoryDAO.getAllInventory()) {
                maxStock = Math.max(maxStock, inv.getQuantity());
            }
        } catch (Exception e) { e.printStackTrace(); }
        maxStock = Math.max(100, maxStock); // Minimum scale
        
        // Draw axes
        g2.setColor(accentColor);
        g2.setStroke(new BasicStroke(2));
        
        // X-axis
        g2.drawLine(padding, height - padding, width - padding, height - padding);
        
        // Y-axis
        g2.drawLine(padding, height - padding, padding, padding);
        
        // Draw grid lines and labels
        g2.setColor(new Color(230, 230, 230));
        g2.setStroke(new BasicStroke(1));
        g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Y-axis grid and labels
        int yStep = maxStock / 5;
        for (int i = 0; i <= 5; i++) {
            int y = height - padding - (i * chartHeight / 5);
            int value = i * yStep;
            
            // Grid line
            g2.drawLine(padding, y, width - padding, y);
            
            // Label
            g2.setColor(accentColor);
            g2.drawString(String.valueOf(value), padding - 30, y + 5);
            g2.setColor(new Color(230, 230, 230));
        }
        
        // Draw bars
        int x = padding + barSpace;
        try {
            for (Inventory inv : inventoryDAO.getAllInventory()) {
                Drinks drink = drinkDAO.getDrinkById(inv.getDrinkId());
                int stock = inv.getQuantity();
                
                // Calculate bar height (scaled)
                int barHeight = (int)(((double)stock / maxStock) * chartHeight);
                int barY = height - padding - barHeight;
                
                // Draw bar with drop shadow
                g2.setColor(new Color(210, 210, 210));
                g2.fillRect(x + 3, barY + 3, barWidth, barHeight);
                
                // Determine bar color - Red for low stock, otherwise Coca-Cola red
                if (stock < 20) {
                    g2.setColor(new Color(255, 100, 100));
                } else {
                    g2.setColor(primaryColor);
                }
                g2.fillRect(x, barY, barWidth, barHeight);
                
                // Draw value on top of bar
                g2.setColor(accentColor);
                g2.drawString(String.valueOf(stock), x + (barWidth/2) - 10, barY - 5);
                
                // Draw product name below x-axis
                g2.drawString(drink != null ? drink.getName() : ("Drink#" + inv.getDrinkId()), x, height - padding + 20);
                
                // Move to next bar position
                x += barWidth + barSpace;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Product name cannot be empty", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Add to drinks table
            Drinks drink = new Drinks(name, "CocaCola", price); // Use a default brand or add a field for brand
            drinkDAO.addDrink(drink);
            // Get the inserted drink's ID
            Drinks inserted = drinkDAO.getAllDrinks().stream().filter(d -> d.getName().equals(name)).findFirst().orElse(null);
            if (inserted != null) {
                // Add to inventory for HQ (branchId=1)
                inventoryDAO.addInventory(new Inventory(inserted.getDrinkId(), 1, stock, 10));
            }
            // Clear fields
            nameField.setText("");
            priceField.setText("");
            stockField.setText("");
            // Update UI
            loadProductData();
            updateLowStockAlerts();
            chartPanel.repaint();
            JOptionPane.showMessageDialog(this, 
                "Product added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for price and stock", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void updateProduct() {
        String selected = (String) productBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, 
                "Please select a product to update", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Product name cannot be empty", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Find the drink by name
            Drinks drink = drinkDAO.getAllDrinks().stream().filter(d -> d.getName().equals(selected)).findFirst().orElse(null);
            if (drink == null) {
                JOptionPane.showMessageDialog(this, 
                    "Product not found", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Update drink info
            drink.setName(name);
            drink.setUnitPrice(price);
            drinkDAO.updateDrink(drink);
            // Update inventory for HQ (branchId=1)
            Inventory inv = inventoryDAO.getAllInventory().stream()
                .filter(i -> i.getDrinkId() == drink.getDrinkId() && i.getBranchId() == 1)
                .findFirst().orElse(null);
            if (inv != null) {
                inv.setQuantity(stock);
                inventoryDAO.updateInventory(inv);
            }
            // Update UI
            loadProductData();
            updateLowStockAlerts();
            chartPanel.repaint();
            JOptionPane.showMessageDialog(this, 
                "Product updated successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter valid numbers for price and stock", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    // Refresh all dashboard data
    private void refreshDashboard() {
        // Refresh alerts
        if (alertsPanel != null) {
            alertsPanel.removeAll();
            JTextArea alertsArea = new JTextArea();
            alertsArea.setEditable(false);
            try {
                List<Inventory> lowStock = inventoryDAO.getAllInventory().stream()
                    .filter(i -> i.getQuantity() < i.getThreshold())
                    .toList();
                StringBuilder sb = new StringBuilder();
                for (Inventory inv : lowStock) {
                    Drinks drink = drinkDAO.getDrinkById(inv.getDrinkId());
                    sb.append("LOW STOCK: ")
                      .append(drink != null ? drink.getName() : "Drink#"+inv.getDrinkId())
                      .append(" (Qty: ").append(inv.getQuantity()).append(", Threshold: ").append(inv.getThreshold()).append(")\n");
                }
                alertsArea.setText(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
                alertsArea.setText("Error loading alerts: " + e.getMessage());
            }
            alertsPanel.add(new JScrollPane(alertsArea), BorderLayout.CENTER);
            alertsPanel.revalidate();
            alertsPanel.repaint();
        }
        // TODO: Add similar refresh logic for reports and inventory panels if needed
    }
    
    public static void main(String[] args) {
        // Set up and start dashboard
        try {
            // Try to use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore - will use default
        }
        
        // Launch dashboard
        SwingUtilities.invokeLater(() -> {
            new HeadquartersDashboard().setVisible(true);
        });
    }
} 