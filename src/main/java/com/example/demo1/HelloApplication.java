package com.example.demo1;

import com.example.dao.BaseDAO;
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
import java.util.Optional;

public class HelloApplication extends Application {
    private static Branch selectedBranch;
    private static User currentUser;
    private static String[] appArgs;
    
    @Override
    public void start(Stage stage) throws IOException {
        // Check for database host in arguments
        processArguments();
        
        // First, prompt for database connection if not already set
        if (BaseDAO.getDatabaseHost().equals("localhost")) {
            promptForDatabaseHost();
        }
        
        // If no branch is selected, prompt for branch selection
        if (selectedBranch == null) {
            promptForBranchSelection();
        }
        
        // Load the main ordering interface
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        
        HelloController controller = fxmlLoader.getController();
        if (selectedBranch != null) {
            controller.setBranch(selectedBranch);
            stage.setTitle("Ordering Interface - " + selectedBranch.getBranchName());
        } else {
            stage.setTitle("Ordering Interface");
        }
        
        if (currentUser != null) {
            controller.setUser(currentUser);
        }
        
        stage.setScene(scene);
        stage.show();
    }
    
    private void processArguments() {
        if (appArgs != null) {
            for (String arg : appArgs) {
                if (arg.startsWith("--dbhost=")) {
                    String host = arg.substring("--dbhost=".length());
                    if (!host.isEmpty()) {
                        BaseDAO.setDatabaseHost(host);
                        System.out.println("Database host set to: " + host);
                    }
                }
            }
        }
    }
    
    private void promptForDatabaseHost() {
        TextInputDialog dialog = new TextInputDialog(BaseDAO.getDatabaseHost());
        dialog.setTitle("Database Connection");
        dialog.setHeaderText("Enter Database Host");
        dialog.setContentText("Host:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(host -> {
            if (!host.isEmpty()) {
                BaseDAO.setDatabaseHost(host);
            }
        });
    }
    
    private void promptForBranchSelection() {
        // Create a simple dialog to select a branch
        // In a real application, this would load branches from the database
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Branch Selection");
        alert.setHeaderText("Please select a branch for this device");
        alert.setContentText("This device will represent a specific branch for the demonstration.");
        
        ButtonType branch1Button = new ButtonType("Branch 1 - Headquarters");
        ButtonType branch2Button = new ButtonType("Branch 2 - Downtown");
        ButtonType branch3Button = new ButtonType("Branch 3 - Westside");
        
        alert.getButtonTypes().setAll(branch1Button, branch2Button, branch3Button);
        
        Optional<ButtonType> result = alert.showAndWait();
        
        // Create a demo branch based on selection
        Branch branch;
        if (result.get() == branch1Button) {
            branch = new Branch(1, "Headquarters", "Main Street 123");
        } else if (result.get() == branch2Button) {
            branch = new Branch(2, "Downtown", "City Center 45");
        } else {
            branch = new Branch(3, "Westside", "West Avenue 67");
        }
        
        selectedBranch = branch;
    }
    
    public static void setBranch(Branch branch) {
        selectedBranch = branch;
    }
    
    public static void setUser(User user) {
        currentUser = user;
    }

    public static void main(String[] args) {
        appArgs = args;
        launch();
    }
}