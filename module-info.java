module com.example.browserprogram {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.example.browserprogram to javafx.fxml;
    exports com.example.browserprogram;
}