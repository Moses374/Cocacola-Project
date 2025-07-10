package com.example.demo1;

import com.example.dao.BaseDAO;
import com.example.dao.BranchDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Branch;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class HelloApplication extends Application {
    private static Branch selectedBranch;
    private static User currentUser;
    private static String[] appArgs;
    private static BranchDAO branchDAO;
    
    @Override
    public void start(Stage stage) throws IOException {
        // Check for database host in arguments
        processArguments();
        branchDAO = new BranchDAO();
        
        // First, prompt for branch selection
        if (!selectBranch()) {
            return; // Exit if no branch selected
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Drinks 4 U - " + selectedBranch.getBranchName());
        stage.setScene(scene);
        stage.show();
    }
    
    private void processArguments() {
        if (appArgs != null && appArgs.length > 0) {
            String dbHost = appArgs[0];
            System.setProperty("DB_HOST", dbHost);
        }
    }
    
    private boolean selectBranch() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Branch Selection");
        dialog.setHeaderText("Enter Branch ID");
        dialog.setContentText("Please enter your branch ID:");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int branchId = Integer.parseInt(result.get());
                try {
                    selectedBranch = branchDAO.getBranchById(branchId);
                    if (selectedBranch != null) {
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                // Invalid number format
            }
        }
        
        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid branch ID. Application will now exit.", ButtonType.OK);
        alert.showAndWait();
        return false;
    }
    
    public static void main(String[] args) {
        appArgs = args;
        launch(args);
    }
    
    public static Branch getSelectedBranch() {
        return selectedBranch;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}