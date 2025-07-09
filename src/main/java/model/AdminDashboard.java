package model;

import com.example.dao.UserDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboard extends HeadquartersDashboard {
    private JPanel userPanel;
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JButton addUserButton;
    private JButton editUserButton;
    private JButton deleteUserButton;
    private JTabbedPane tabbedPane;
    
    public AdminDashboard() {
        super();
        setTitle("Admin Dashboard - Drinks Distribution System");
        
        // Get the tabbed pane from the content pane
        for (Component c : getContentPane().getComponents()) {
            if (c instanceof JPanel) {
                JPanel panel = (JPanel) c;
                for (Component inner : panel.getComponents()) {
                    if (inner instanceof JTabbedPane) {
                        tabbedPane = (JTabbedPane) inner;
                        break;
                    }
                }
                if (tabbedPane != null) break;
            }
        }
        
        // Add User Management tab
        initializeUserManagementTab();
        
        // Add the tab to the tabbed pane
        if (tabbedPane != null) {
            tabbedPane.addTab("User Management", userPanel);
        } else {
            System.err.println("Error: Could not find tabbedPane in parent class");
        }
    }
    
    @Override
    protected void refreshDashboard() {
        super.refreshDashboard();
        refreshUserTable();
    }
    
    private void initializeUserManagementTab() {
        userPanel = new JPanel(new BorderLayout(10, 10));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table model with columns
        userTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        userTableModel.addColumn("ID");
        userTableModel.addColumn("Username");
        userTableModel.addColumn("Role");
        
        // Create table
        userTable = new JTable(userTableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        userPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        addUserButton = new JButton("Add User");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");
        
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        
        userPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        addUserButton.addActionListener(e -> showAddUserDialog());
        editUserButton.addActionListener(e -> showEditUserDialog());
        deleteUserButton.addActionListener(e -> deleteSelectedUser());
        
        // Load users
        refreshUserTable();
    }
    
    private void refreshUserTable() {
        userTableModel.setRowCount(0); // Clear table
        
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllUsers();
            
            for (User user : users) {
                userTableModel.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getRole()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading users: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Add User", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"admin", "user"});
        
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "Username and password cannot be empty",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                UserDAO userDAO = new UserDAO();
                User newUser = new User(username, password, role);
                userDAO.addUser(newUser);
                
                dialog.dispose();
                refreshUserTable();
                
                JOptionPane.showMessageDialog(this,
                    "User added successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Error adding user: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.pack();
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to edit",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) userTable.getValueAt(selectedRow, 0);
        
        try {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                    "User not found",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog dialog = new JDialog(this, "Edit User", true);
            dialog.setLayout(new BorderLayout());
            
            JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JTextField usernameField = new JTextField(user.getUsername());
            JPasswordField passwordField = new JPasswordField(user.getPassword());
            JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"admin", "user"});
            roleComboBox.setSelectedItem(user.getRole());
            
            formPanel.add(new JLabel("Username:"));
            formPanel.add(usernameField);
            formPanel.add(new JLabel("Password:"));
            formPanel.add(passwordField);
            formPanel.add(new JLabel("Role:"));
            formPanel.add(roleComboBox);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton saveButton = new JButton("Save");
            JButton cancelButton = new JButton("Cancel");
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            saveButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                        "Username and password cannot be empty",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setRole(role);
                    
                    userDAO.updateUser(user);
                    
                    dialog.dispose();
                    refreshUserTable();
                    
                    JOptionPane.showMessageDialog(this,
                        "User updated successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(dialog,
                        "Error updating user: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });
            
            cancelButton.addActionListener(e -> dialog.dispose());
            
            dialog.pack();
            dialog.setSize(400, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error loading user: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to delete",
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) userTable.getValueAt(selectedRow, 0);
        String username = (String) userTable.getValueAt(selectedRow, 1);
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete user: " + username + "?",
            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                UserDAO userDAO = new UserDAO();
                boolean success = userDAO.deleteUser(userId);
                
                if (success) {
                    refreshUserTable();
                    JOptionPane.showMessageDialog(this,
                        "User deleted successfully",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete user",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting user: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
} 