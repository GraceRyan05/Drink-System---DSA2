module com.example.drink_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires xstream;


    opens com.example.drink_system to xstream, javafx.fxml;
    exports com.example.drink_system;

}