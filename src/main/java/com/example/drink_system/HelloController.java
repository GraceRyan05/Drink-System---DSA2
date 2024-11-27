package com.example.drink_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    //ingredients tab
    @FXML
    private TextField ingredientNameField;
    @FXML
    private TextField ingredientDescriptionField;
    @FXML
    private TextField ingredientAlcContentField;
    @FXML
    private Button ingredientAddButton;
    @FXML
    private Button ingredientDeleteButton;
    @FXML
    private ListView ingredientListView;

    //Drinks Page
    @FXML
    private TextField drinkNameField;
    @FXML
    private TextField drinkDescriptionField;
    @FXML
    private TextField drinkCountryOfOriginField;
    @FXML
    private TextField drinkURLImageField;
    @FXML
    private Button drinkAddButton;
    @FXML
    private Button drinkDeleteButton;
    @FXML
    private ListView drinkListView;

}