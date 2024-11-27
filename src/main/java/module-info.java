module com.example.drink_system {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.drink_system to javafx.fxml;
    exports com.example.drink_system;


}