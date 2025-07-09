module com.example.demo1 {
    // Essential modules only
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.base;
    requires java.desktop;
    requires java.sql;

    // Exports and opens
    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
    exports model;
    opens model to javafx.fxml;
}