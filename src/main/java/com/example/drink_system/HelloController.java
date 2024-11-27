package com.example.drink_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
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

    private CustomLinkedList<Ingredients> ingredientsCustomLinkedList = new CustomLinkedList<>();
    public void addIngredient (ActionEvent event){
        String name = ingredientNameField.getText();
        String description = ingredientDescriptionField.getText();
        double alcContent = Double.parseDouble(ingredientAlcContentField.getText());

        Ingredients newIngredient = new Ingredients(name, description, alcContent);

        ingredientsCustomLinkedList.add(newIngredient);
        ingredientListView.getItems().add(newIngredient.toString());

        ingredientNameField.clear();
        ingredientDescriptionField.clear();
        ingredientAlcContentField.clear();
    }


    public void deleteIngredient(ActionEvent event) {
        //I am not sure how you do the delete method
    }




    //Drinks Page
    @FXML
    private TextField drinkNameField;
    @FXML
    private TextField drinkDescriptionField;
    @FXML
    private TextField drinkCountryOfOriginField;
    @FXML
    private TextField drinkURLImageField; //we will need to implement hyperlink feature
    @FXML
    private Button drinkAddButton;
    @FXML
    private Button drinkDeleteButton;
    @FXML
    private ListView drinkListView;
    private CustomLinkedList<Drinks> drinksCustomLinkedList = new CustomLinkedList<>();
    public void addDrink (ActionEvent event) {
        String name = drinkNameField.getText();
        String description = drinkDescriptionField.getText();
        String origin = drinkCountryOfOriginField.getText();
        String url = drinkURLImageField.getText();

        Drinks newDrink = new Drinks(name, description, origin, url);

        drinksCustomLinkedList.add(newDrink);
        drinkListView.getItems().add(newDrink.toString());

        drinkNameField.clear();
        drinkDescriptionField.clear();
        drinkCountryOfOriginField.clear();
        drinkURLImageField.clear();
    }
    public void deleteDrink(ActionEvent event){

    }

}