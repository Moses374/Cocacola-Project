package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;

/**
 * MainGUI serves as the central navigation hub for the application
 * It provides access to different parts of the application based on user role
 */
public class MainGUI extends JFrame {
    private User currentUser;
    
    // UI components
    private JPanel mainPanel;
    private JPanel buttonPanel;
    
    // Colors for UI
    private Color primaryColor = new Color(237, 28, 36);    // Coca-Cola red
    private Color accentColor = new Color(30, 39, 46);      // Dark slate
    private Color backgroundColor = new Color(248, 249, 250); // Off white
    
    /**
     * Constructor for MainGUI
     * @param user The currently logged in user
     */
    public MainGUI(User user) {
        this.currentUser = user;
        
        // Set up the frame
        setTitle("Drinks Distribution System - Main Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Add header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Add button panel
        buttonPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        buttonPanel.setBackground(backgroundColor);
        mainPanel.add(new JScrollPane(buttonPanel), BorderLayout.CENTER);
        
        // Add navigation buttons based on user role
        addNavigationButtons();
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Creates the header panel with logo and user info
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(false);
        
        // Logo and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🥤");
        logoLabel.setFont(new Font("Dialog", Font.PLAIN, 28));
        titlePanel.add(logoLabel);
        
        JLabel titleLabel = new JLabel("Drinks Distribution System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titlePanel.add(titleLabel);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userPanel.add(userLabel);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        userPanel.add(logoutButton);
        
        header.add(userPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Adds navigation buttons based on user role
     */
    private void addNavigationButtons() {
        // Common buttons for all users
        addNavigationButton("Ordering Interface", "Access the drink ordering system", e -> openOrderingInterface());
        addNavigationButton("Branch Information", "View branch details and locations", e -> openBranchInfo());
        
        // Admin-specific buttons
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            addNavigationButton("Inventory Management", "View and manage inventory across branches", e -> openInventoryManagement());
            addNavigationButton("Headquarters Dashboard", "Access the headquarters dashboard", e -> openHeadquartersDashboard());
            addNavigationButton("User Management", "Manage system users and permissions", e -> openUserManagement());
            addNavigationButton("Order History", "View and manage order history", e -> openOrderHistory());
        }
    }
    
    /**
     * Helper method to create and add a navigation button
     */
    private void addNavigationButton(String title, String description, ActionListener action) {
        JPanel buttonCard = new JPanel();
        buttonCard.setLayout(new BoxLayout(buttonCard, BoxLayout.Y_AXIS));
        buttonCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        buttonCard.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton button = new JButton("Open");
        button.addActionListener(action);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        buttonCard.add(titleLabel);
        buttonCard.add(Box.createVerticalStrut(5));
        buttonCard.add(descLabel);
        buttonCard.add(Box.createVerticalStrut(10));
        buttonCard.add(button);
        
        buttonPanel.add(buttonCard);
    }
    
    /**
     * Opens the ordering interface
     */
    private void openOrderingInterface() {
        try {
            // Execute the batch file we created
            ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "run_app.bat");
            builder.directory(new File(System.getProperty("user.dir")));
            Process process = builder.start();
            
            JOptionPane.showMessageDialog(this,
                "Launching ordering interface...",
                "Launching Application", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error opening ordering interface: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the inventory management interface
     */
    private void openInventoryManagement() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this,
                "You don't have permission to access Inventory Management.",
                "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            HeadquartersDashboard dashboard = new HeadquartersDashboard();
            dashboard.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error opening Inventory Management: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the branch information interface
     */
    private void openBranchInfo() {
        try {
            // Display branch information from the database
            JFrame branchFrame = new JFrame("Branch Information");
            branchFrame.setSize(600, 400);
            branchFrame.setLocationRelativeTo(this);
            
            // Create a panel to display branch information
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Create a table model for branch data
            String[] columns = {"ID", "Name", "Location"};
            Object[][] data = {
                {2, "NAKURU", "Nakuru"},
                {3, "MOMBASA", "Mombasa"},
                {4, "KISUMU", "Kisumu"},
                {5, "NAIROBI", "Nairobi HQ"}
            };
            
            JTable table = new JTable(data, columns);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            branchFrame.add(panel);
            branchFrame.setVisible(true);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error displaying branch information: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the headquarters dashboard
     */
    private void openHeadquartersDashboard() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this,
                "You don't have permission to access the Headquarters Dashboard.",
                "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            HeadquartersDashboard dashboard = new HeadquartersDashboard();
            dashboard.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error opening Headquarters Dashboard: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the user management interface
     */
    private void openUserManagement() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this,
                "You don't have permission to access User Management.",
                "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create and show AdminDashboard
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error opening User Management: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opens the order history interface
     */
    private void openOrderHistory() {
        if (!currentUser.getRole().equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this,
                "You don't have permission to access Order History.",
                "Access Denied", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Create a simple order history viewer
            JFrame orderHistoryFrame = new JFrame("Order History");
            orderHistoryFrame.setSize(700, 500);
            orderHistoryFrame.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Sample order data
            String[] columns = {"Order ID", "Customer", "Branch", "Date", "Total (KES)"};
            Object[][] data = {
                {1001, "Ayman Khubran", "NAIROBI", "2023-05-15", 2500.00},
                {1002, "Halima Musa", "MOMBASA", "2023-05-16", 1800.50},
                {1003, "Brian Otieno", "KISUMU", "2023-05-17", 3200.75},
                {1004, "Grace Wanjiru", "NAKURU", "2023-05-18", 1250.25}
            };
            
            JTable table = new JTable(data, columns);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);
            
            // Add a refresh button
            JButton refreshButton = new JButton("Refresh");
            panel.add(refreshButton, BorderLayout.SOUTH);
            
            orderHistoryFrame.add(panel);
            orderHistoryFrame.setVisible(true);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error displaying order history: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Logs out the current user and returns to the login screen
     */
    private void logout() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            // Launch the JavaFX application again
            try {
                MainLauncher.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error restarting application: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            User testUser = new User();
            testUser.setUsername("Test User");
            testUser.setRole("admin");
            
            MainGUI gui = new MainGUI(testUser);
            gui.setVisible(true);
        });
    }
} 