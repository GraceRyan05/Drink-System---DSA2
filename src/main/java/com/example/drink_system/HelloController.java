package com.example.drink_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;

public class HelloController {


    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    public void initialize(){
        loadIngredients();
        loadDrinks();
    }


    //The lists of each
    private CustomLinkedList<Drinks> drinksList;
    private CustomLinkedList<Ingredients> ingredientsList;
    private CustomLinkedList<Recipes> recipesList;


    //Controller constructor
    public HelloController() {
        this.drinksList = new CustomLinkedList<>();
        this.ingredientsList = new CustomLinkedList<>();
        this.recipesList = new CustomLinkedList<>();
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
    public void addIngredient (ActionEvent event) throws IOException{
        String name = ingredientNameField.getText();
        String description = ingredientDescriptionField.getText();
        double alcContent = Double.parseDouble(ingredientAlcContentField.getText());

        Ingredients newIngredient = new Ingredients(name, description, alcContent);

        ingredientsCustomLinkedList.add(newIngredient);
        ingredientListView.getItems().add(newIngredient.toString());
        saveIngredients();

        ingredientNameField.clear();
        ingredientDescriptionField.clear();
        ingredientAlcContentField.clear();
    }


    public void deleteIngredient(ActionEvent event) {
        //I am not sure how you do the delete method

    }


    public void saveIngredients() throws IOException {
        File file = new File("src/main/resources/com/example/drink_system/ingredient.xml");
        var xstream = new XStream(new DomDriver());
        xstream.allowTypeHierarchy(Ingredients.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        ObjectOutputStream os = xstream.createObjectOutputStream(new FileWriter(file));
        os.writeObject(ingredientsCustomLinkedList);
        System.out.println ("ingredients added to the file"); //future debug
        os.close();
    }

    public void loadIngredients() {
        File file = new File("src/main/resources/com/example/drink_system/ingredient.xml");
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        //list of classes for serialisation
        xstream.allowTypeHierarchy(Ingredients.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        try {

            ObjectInputStream in = xstream.createObjectInputStream(new FileReader(file));
            //load the xml data into showsList
            ingredientsCustomLinkedList = (CustomLinkedList<Ingredients>) in.readObject();
            System.out.println("ingredients are loaded.");//debug
            for (int i = 0; i < ingredientsCustomLinkedList.size(); i++) { //populating listview with loaded ingredients
                ingredientListView.getItems().add(ingredientsCustomLinkedList.getAtIndex(i).toString());
            }
            in.close();
        } catch (Exception error) {
            error.printStackTrace();
            System.err.println("error loading from xml: " + error.getMessage()); //debug
        }
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
    public void addDrink (ActionEvent event) throws IOException{
        String name = drinkNameField.getText();
        String description = drinkDescriptionField.getText();
        String origin = drinkCountryOfOriginField.getText();
        String url = drinkURLImageField.getText();

        Drinks newDrink = new Drinks(name, description, origin, url);

        drinksCustomLinkedList.add(newDrink);
        drinkListView.getItems().add(newDrink.toString());
        saveDrinks();

        drinkNameField.clear();
        drinkDescriptionField.clear();
        drinkCountryOfOriginField.clear();
        drinkURLImageField.clear();
    }
    public void deleteDrink(ActionEvent event){

    }
    public void saveDrinks() throws IOException {
        File file = new File("src/main/resources/com/example/drink_system/drinks.xml");
        var xstream = new XStream(new DomDriver());
        xstream.allowTypeHierarchy(Drinks.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        ObjectOutputStream os = xstream.createObjectOutputStream(new FileWriter(file));
        os.writeObject(drinksCustomLinkedList);
        System.out.println ("drink added to the file"); //future debug
        os.close();
    }
    public void loadDrinks() {
        File file = new File("src/main/resources/com/example/drink_system/drinks.xml");
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        //list of classes for serialisation
        xstream.allowTypeHierarchy(Drinks.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        try {

            ObjectInputStream in = xstream.createObjectInputStream(new FileReader(file));
            //load the xml data into showsList
            drinksCustomLinkedList = (CustomLinkedList<Drinks>) in.readObject();
            System.out.println("drinks are loaded.");//debug
            for (int i = 0; i < drinksCustomLinkedList.size(); i++) { //populating listview with loaded ingredients
                drinkListView.getItems().add(drinksCustomLinkedList.getAtIndex(i).toString());
            }
            in.close();
        } catch (Exception error) {
            error.printStackTrace();
            System.err.println("error loading from xml: " + error.getMessage()); //debug
        }
    }

}