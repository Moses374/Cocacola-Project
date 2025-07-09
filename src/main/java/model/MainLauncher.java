package model;

import com.example.dao.BaseDAO;
import com.example.dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.io.File;
import java.io.IOException;

public class MainLauncher extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton guestButton;
    private JLabel statusLabel;
    private JTextField dbHostField;
    
    // Colors for UI
    private Color primaryColor = new Color(237, 28, 36);    // Coca-Cola red
    private Color accentColor = new Color(30, 39, 46);      // Dark slate
    private Color backgroundColor = new Color(248, 249, 250); // Off white
    
    public MainLauncher() {
        setTitle("Drinks Distribution System");
        setSize(500, 450); // Increased height for DB config
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Drinks Distribution System");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        
        JLabel subtitleLabel = new JLabel("Optional login for admin features");
        subtitleLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Database configuration panel
        JPanel dbConfigPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dbConfigPanel.setOpaque(false);
        
        JLabel dbHostLabel = new JLabel("Database Host:");
        dbHostField = new JTextField(BaseDAO.getDatabaseHost(), 15);
        JButton dbConnectButton = new JButton("Connect");
        dbConnectButton.addActionListener(e -> updateDatabaseHost());
        
        dbConfigPanel.add(dbHostLabel);
        dbConfigPanel.add(dbHostField);
        dbConfigPanel.add(dbConnectButton);
        
        // Guest button panel (at the top for prominence)
        JPanel guestPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        guestPanel.setOpaque(false);
        guestButton = createStyledButton("Continue as Guest", new Color(0, 123, 255));
        guestButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        guestPanel.add(guestButton);
        
        // Add a separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        
        // Admin login section label
        JLabel adminSectionLabel = new JLabel("Admin Login (Optional)");
        adminSectionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        adminSectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setOpaque(false);
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        
        // Login button panel
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButtonPanel.setOpaque(false);
        loginButton = createStyledButton("Login as Admin", primaryColor);
        loginButtonPanel.add(loginButton);
        
        // Status label for error messages
        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create a panel for admin login section
        JPanel adminLoginPanel = new JPanel();
        adminLoginPanel.setLayout(new BoxLayout(adminLoginPanel, BoxLayout.Y_AXIS));
        adminLoginPanel.setOpaque(false);
        adminLoginPanel.add(adminSectionLabel);
        adminLoginPanel.add(Box.createVerticalStrut(10));
        adminLoginPanel.add(formPanel);
        adminLoginPanel.add(Box.createVerticalStrut(10));
        adminLoginPanel.add(loginButtonPanel);
        
        // Add action listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        
        guestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openGuestInterface();
            }
        });
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create center panel with DB config and guest button
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(dbConfigPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(guestPanel);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Add separator and admin section
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.setOpaque(false);
        southPanel.add(separator);
        southPanel.add(Box.createVerticalStrut(10));
        southPanel.add(adminLoginPanel);
        southPanel.add(Box.createVerticalStrut(10));
        southPanel.add(statusLabel);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Set main panel as content pane
        setContentPane(mainPanel);
        
        // Check if admin user exists, create if not
        ensureAdminUserExists();
    }
    
    private void updateDatabaseHost() {
        String newHost = dbHostField.getText().trim();
        if (!newHost.isEmpty()) {
            BaseDAO.setDatabaseHost(newHost);
            statusLabel.setForeground(new Color(0, 128, 0)); // Green color
            statusLabel.setText("Database host updated to: " + newHost);
            
            // Test the connection
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.getAllUsers();
                JOptionPane.showMessageDialog(this, 
                    "Successfully connected to database at " + newHost,
                    "Connection Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                statusLabel.setForeground(Color.RED);
                statusLabel.setText("Error connecting to database: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Database host cannot be empty");
        }
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            return;
        }
        
        try {
            UserDAO userDAO = new UserDAO();
            boolean isValid = userDAO.validateUser(username, password);
            
            if (isValid) {
                User user = userDAO.getUserByUsername(username);
                openUserInterface(user);
            } else {
                statusLabel.setText("Invalid username or password");
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            statusLabel.setText("Database error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void openUserInterface(User user) {
        SwingUtilities.invokeLater(() -> {
            dispose(); // Close login window
            
            if (user.getRole().equalsIgnoreCase("admin")) {
                // Open the main GUI with admin privileges
                MainGUI mainGUI = new MainGUI(user);
                mainGUI.setVisible(true);
            } else {
                // For other roles, open the regular GUI
                MainGUI mainGUI = new MainGUI(user);
                mainGUI.setVisible(true);
            }
        });
    }
    
    private void openGuestInterface() {
        SwingUtilities.invokeLater(() -> {
            dispose(); // Close login window
            
            // Create a guest user with limited privileges
            User guestUser = new User();
            guestUser.setUsername("Guest");
            guestUser.setRole("guest");
            
            // Open the main GUI as guest
            MainGUI mainGUI = new MainGUI(guestUser);
            mainGUI.setVisible(true);
        });
    }
    
    private void ensureAdminUserExists() {
        try {
            UserDAO userDAO = new UserDAO();
            User admin = userDAO.getUserByUsername("ADMIN1");
            
            if (admin == null) {
                // Admin doesn't exist, create it
                User newAdmin = new User("ADMIN1", "passadmin", "admin");
                userDAO.addUser(newAdmin);
            }
        } catch (SQLException ex) {
            System.err.println("Error checking/creating admin user: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Process command line arguments
        for (String arg : args) {
            if (arg.startsWith("--dbhost=")) {
                String host = arg.substring("--dbhost=".length());
                if (!host.isEmpty()) {
                    BaseDAO.setDatabaseHost(host);
                    System.out.println("Database host set to: " + host);
                }
            }
        }
        
        // Start application
        SwingUtilities.invokeLater(() -> {
            MainLauncher launcher = new MainLauncher();
            launcher.setVisible(true);
        });
    }
} 