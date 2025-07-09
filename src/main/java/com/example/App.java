package com.example;

import model.MainLauncher;
import javax.swing.SwingUtilities;

/**
 * Main Application Entry Point
 */
public class App {
    // Flag to track if the app should launch directly to the ordering interface
    private static boolean launchOrderingInterface = false;
    
    /**
     * Launch the HelloApplication (ordering interface) directly
     */
    public static void launchOrderingInterface() {
        try {
            // Use reflection to avoid direct dependency on JavaFX classes
            // This allows the App class to be loaded without JavaFX
            Class<?> appClass = Class.forName("com.example.demo1.HelloApplication");
            java.lang.reflect.Method mainMethod = appClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to launch ordering interface: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        // Check if we should launch directly to ordering interface
        for (String arg : args) {
            if (arg.equals("--ordering")) {
                launchOrderingInterface = true;
                break;
            }
        }
        
        if (launchOrderingInterface) {
            launchOrderingInterface();
        } else {
            // Launch the main launcher window
            SwingUtilities.invokeLater(() -> {
                MainLauncher launcher = new MainLauncher();
                launcher.setVisible(true);
            });
        }
    }
} 