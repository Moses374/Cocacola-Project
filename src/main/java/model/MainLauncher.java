package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainLauncher extends Application {
    @Override
    public void start(Stage primaryStage) {
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
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 