import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
        
        // Set as content pane
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        
        // Load data from server
        loadProductData();
        updateLowStockAlerts();
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
        
        productBox.addActionListener(e -> {
            String selected = (String) productBox.getSelectedItem();
            if (selected != null && HeadquartersServer.stock.containsKey(selected)) {
                Product p = HeadquartersServer.stock.get(selected);
                nameField.setText(p.getName());
                priceField.setText(String.valueOf(p.getPrice()));
                stockField.setText(String.valueOf(p.getStock()));
            }
        });
        
        panel.add(formPanel, BorderLayout.SOUTH);
        return panel;
    }
    
    // LOW STOCK ALERTS PANEL
    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        
        // Title
        JLabel alertsTitle = new JLabel("Low Stock Alerts", SwingConstants.CENTER);
        alertsTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        alertsTitle.setForeground(primaryColor);
        panel.add(alertsTitle, BorderLayout.NORTH);
        
        // Alerts container (will be populated dynamically)
        alertsPanel = new JPanel();
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
        alertsPanel.setBackground(backgroundColor);
        JScrollPane scrollPane = new JScrollPane(alertsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JButton refreshButton = createStyledButton("Refresh Alerts", primaryColor);
        refreshButton.addActionListener(e -> updateLowStockAlerts());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // SALES REPORTS PANEL
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(backgroundColor);
        
        // Simple report view
        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setEditable(false);
        reportArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Generate the report content using server data
        StringBuilder report = new StringBuilder();
        report.append("COCA-COLA SALES REPORT\n");
        report.append("====================\n\n");
        report.append(String.format("%-20s %-10s %-10s %-10s\n", 
            "PRODUCT", "PRICE", "STOCK", "VALUE"));
        report.append("\n");
        
        double totalValue = 0;
        for (Map.Entry<String, Product> entry : HeadquartersServer.stock.entrySet()) {
            Product p = entry.getValue();
            double value = p.getPrice() * p.getStock();
            totalValue += value;
            
            report.append(String.format("%-20s %.2f      %-10d %.2f\n",
                p.getName(), p.getPrice(), p.getStock(), value));
        }
        
        report.append("\n====================\n");
        report.append(String.format("Total Inventory Value: KES %.2f", totalValue));
        
        reportArea.setText(report.toString());
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Export button
        JButton exportButton = createStyledButton("Export Report", primaryColor);
        exportButton.addActionListener(e -> {
            try {
                FileWriter writer = new FileWriter("coca_cola_report.txt");
                writer.write(reportArea.getText());
                writer.close();
                JOptionPane.showMessageDialog(this, 
                    "Report saved to coca_cola_report.txt",
                    "Export Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error saving report: " + ex.getMessage(),
                    "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(exportButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
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
        // Clear existing data
        DefaultTableModel model = (DefaultTableModel) stockTable.getModel();
        model.setRowCount(0);
        productBox.removeAllItems();
        
        // Load from server
        for (Map.Entry<String, Product> entry : HeadquartersServer.stock.entrySet()) {
            Product p = entry.getValue();
            String status = p.getStock() < 20 ? "LOW STOCK" : "In Stock";
            
            model.addRow(new Object[]{
                p.getName(),
                p.getPrice(),
                p.getStock(),
                status
            });
            
            productBox.addItem(p.getName());
        }
    }
    
    private void updateLowStockAlerts() {
        // Clear existing alerts
        alertsPanel.removeAll();
        
        boolean hasAlerts = false;
        
        // Check each product
        for (Map.Entry<String, Product> entry : HeadquartersServer.stock.entrySet()) {
            Product p = entry.getValue();
            
            if (p.getStock() < 20) {
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
                
                JLabel nameLabel = new JLabel(p.getName());
                nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                
                JLabel stockLabel = new JLabel("Only " + p.getStock() + " units remaining");
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
                        "Restock " + p.getName(),
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    try {
                        int qty = Integer.parseInt(input);
                        if (qty > 0) {
                            p.setStock(p.getStock() + qty);
                            JOptionPane.showMessageDialog(this, 
                                "Added " + qty + " units to " + p.getName());
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
        int productCount = HeadquartersServer.stock.size();
        if (productCount == 0) return;
        
        // Calculate bar width based on number of products
        int barWidth = Math.min(70, (chartWidth / productCount) - barSpace);
        
        // Find max stock for scaling
        int maxStock = 0;
        for (Product p : HeadquartersServer.stock.values()) {
            maxStock = Math.max(maxStock, p.getStock());
        }
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
        for (Map.Entry<String, Product> entry : HeadquartersServer.stock.entrySet()) {
            Product p = entry.getValue();
            int stock = p.getStock();
            
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
            g2.drawString(p.getName(), x, height - padding + 20);
            
            // Move to next bar position
            x += barWidth + barSpace;
        }
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
            
            if (HeadquartersServer.stock.containsKey(name)) {
                JOptionPane.showMessageDialog(this,
                    "Product already exists", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Add to stock
            HeadquartersServer.stock.put(name, new Product(name, price, stock));
            
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
        }
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
            
            // Get product from server
            Product product = HeadquartersServer.stock.get(selected);
            
            // Update values
            product.setName(name);
            product.setPrice(price);
            product.setStock(stock);
            
            // If name changed, update hashmap key
            if (!name.equals(selected)) {
                HeadquartersServer.stock.remove(selected);
                HeadquartersServer.stock.put(name, product);
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
        }
    }
    
    public static void main(String[] args) {
        // Set up and start dashboard
        try {
            // Try to use system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignore - will use default
        }
        
        // Load stock data if needed
        HeadquartersServer.loadStock();
        
        // Launch dashboard
        SwingUtilities.invokeLater(() -> {
            new HeadquartersDashboard().setVisible(true);
        });
    }
} 