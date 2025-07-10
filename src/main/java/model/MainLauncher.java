package model;

import com.example.dao.UserDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.swing.*;
import java.sql.SQLException;

public class MainLauncher extends Application {
    private UserDAO userDAO;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            userDAO = new UserDAO();
            showLoginScreen(primaryStage);
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showLoginScreen(Stage primaryStage) {
        // Create the login form
        primaryStage.setTitle("Drinks Distribution System - Login");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Text sceneTitle = new Text("Welcome to Drinks Distribution System");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);
        
        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);
        
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);
        
        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);
        
        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
        
        Button loginBtn = new Button("Sign in");
        Button guestBtn = new Button("Continue as Guest");
        
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(guestBtn, loginBtn);
        grid.add(hbBtn, 1, 4);
        
        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);
        
        loginBtn.setOnAction(e -> {
            String username = userTextField.getText();
            String password = pwBox.getText();
            
            try {
                if (userDAO.validateUser(username, password)) {
                    User user = userDAO.getUserByUsername(username);
                    if (user != null) {
                        // Close JavaFX window
                        primaryStage.close();
                        
                        // Launch Swing MainGUI
                        SwingUtilities.invokeLater(() -> {
                            MainGUI gui = new MainGUI(user);
                            gui.setVisible(true);
                        });
                    }
                } else {
                    actionTarget.setText("Invalid username or password");
                }
            } catch (SQLException ex) {
                actionTarget.setText("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        guestBtn.setOnAction(e -> {
            // Load the ordering interface directly
            try {
                // Load the main view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo1/hello-view.fxml"));
                Parent root = loader.load();
                
                // Set up the scene
                Scene scene = new Scene(root);
                
                // Configure the stage
                primaryStage.setTitle("Drinks 4 U - Beverage Ordering System");
                primaryStage.setScene(scene);
                primaryStage.setMinWidth(1200);
                primaryStage.setMinHeight(800);
                primaryStage.show();
                
                System.out.println("Application started successfully");
            } catch (Exception ex) {
                System.err.println("Error starting application: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
} 