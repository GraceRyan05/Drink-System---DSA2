package com.example.drink_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        loadRecipes();


        // Populate ListViews for creating recipes
        for (Ingredients ingredient : ingredientsCustomLinkedList) {
            ingredientsInRecipeListView.getItems().add(ingredient);
        }
        for (Drinks drink : drinksCustomLinkedList) {
            drinksInRecipeListView.getItems().add(drink);
        }

        ingredientsInRecipeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); //allows for multiple items to be selected to add to the recipe
        drinksInRecipeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
    private Button ingredientUpdateButton;
    @FXML
    private ListView ingredientListView;

    private CustomLinkedList<Ingredients> ingredientsCustomLinkedList = new CustomLinkedList<>();

    private int selectedIngredientIndex = -1;
    public void addIngredient (ActionEvent event) throws IOException{
        selectedIngredientIndex = ingredientListView.getSelectionModel().getSelectedIndex(); //in case of updated ingredient
        String name = ingredientNameField.getText();
        String description = ingredientDescriptionField.getText();
        double alcContent = Double.parseDouble(ingredientAlcContentField.getText());
        Ingredients newIngredient = new Ingredients(name, description, alcContent);
        if (selectedIngredientIndex >= 0) {
            // update existing ingredient
            ingredientsCustomLinkedList.setAtIndex(selectedIngredientIndex, newIngredient);
            ingredientListView.getItems().set(selectedIngredientIndex, newIngredient.toString());
            saveIngredients();
        } else {
            // add new ingredient
            ingredientsCustomLinkedList.add(newIngredient);
            ingredientListView.getItems().add(newIngredient.toString());
            saveIngredients();
        }
        selectedIngredientIndex = -1; //prob will delete later on
        ingredientNameField.clear();
        ingredientDescriptionField.clear();
        ingredientAlcContentField.clear();
        ingredientListView.getSelectionModel().clearSelection();
    }

    //this method put selected index`s info into text fields
    public void updateIngredient(ActionEvent event) {
        selectedIngredientIndex = ingredientListView.getSelectionModel().getSelectedIndex();
        if (selectedIngredientIndex >= 0) {
            // get the selected ingredient
            Ingredients selectedIngredient = ingredientsCustomLinkedList.getAtIndex(selectedIngredientIndex);

            // populate text fields with the selected ingredient's data
            ingredientNameField.setText(selectedIngredient.getIngredientName());
            ingredientDescriptionField.setText(selectedIngredient.getTextualDescription());
            ingredientAlcContentField.setText(String.valueOf(selectedIngredient.getAlcoholContent()));
        } else {
            System.out.println("No ingredient selected for update.");
        }
    }


    public void deleteIngredient(ActionEvent event) throws Exception {
        int selectedIndex = ingredientListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1 ){
            ingredientsCustomLinkedList.remove(selectedIndex);
            ingredientListView.getItems().remove(selectedIndex);
            saveIngredients();

        }
        ingredientListView.getSelectionModel().clearSelection();
    }


    public void saveIngredients() throws IOException {
        File file = new File("src/main/resources/com/example/drink_system/ingredient.xml");
        XStream xstream = new XStream(new DomDriver());
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
    private Button drinkUpdateButton;
    @FXML
    private ListView drinkListView;
    private CustomLinkedList<Drinks> drinksCustomLinkedList = new CustomLinkedList<>();

    private int selectedDrinkIndex = -1;
    public void addDrink (ActionEvent event) throws IOException{
        String name = drinkNameField.getText();
        String description = drinkDescriptionField.getText();
        String origin = drinkCountryOfOriginField.getText();
        String url = drinkURLImageField.getText();

        Drinks newDrink = new Drinks(name, description, origin, url);

        if (selectedDrinkIndex >= 0){
            drinksCustomLinkedList.setAtIndex(selectedDrinkIndex, newDrink);
            drinkListView.getItems().set(selectedDrinkIndex, newDrink);
            saveDrinks();
        }
        else{
            drinksCustomLinkedList.add(newDrink);
            drinkListView.getItems().add(newDrink.toString());
            saveDrinks();
        }

        drinkListView.getSelectionModel().clearSelection();
        drinkNameField.clear();
        drinkDescriptionField.clear();
        drinkCountryOfOriginField.clear();
        drinkURLImageField.clear();
    }



    public void updateDrink(ActionEvent event) {
        selectedDrinkIndex = drinkListView.getSelectionModel().getSelectedIndex();
        if (selectedDrinkIndex >= 0) {

            String drinkName = drinkNameField.getText();
            String countryOfOrigin = drinkCountryOfOriginField.getText();
            String description = drinkDescriptionField.getText();
            String image = drinkURLImageField.getText();

            Drinks updatedDrink = new Drinks(drinkName, countryOfOrigin, description, image);

            //update the selected drink with the updated data
            drinksCustomLinkedList.update(selectedDrinkIndex, updatedDrink);

            //show the changes in the list view
            drinkListView.getItems().set(selectedDrinkIndex, updatedDrink.toString());

            //save the updated drink
            //saveDrink();

            //clear selection and text fields
            selectedDrinkIndex = -1; //-delete later????
            drinkNameField.clear();
            drinkCountryOfOriginField.clear();
            drinkDescriptionField.clear();
            drinkURLImageField.clear();

        } else {
            System.out.println("No drink selected for update.");
        }
    }


    @FXML
    private ComboBox<String> deleteDrinkComboBox;
    public void deleteDrink(ActionEvent event) throws Exception {
        int selectedIndex = drinkListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1 ){
            drinksCustomLinkedList.remove(selectedIndex);
            drinkListView.getItems().remove(selectedIndex);
            saveDrinks();

        }
        drinkListView.getSelectionModel().clearSelection();
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








    //Recipes tab
    @FXML
    private TextField recipeNameTextField;
    @FXML
    private Button recipeAddButton;
    @FXML
    private Button recipeDeleteButton;
    @FXML
    private ListView ingredientsInRecipeListView;
    @FXML
    private ListView drinksInRecipeListView;

    @FXML
    private ListView recipeListView;

    private CustomLinkedList<Recipes> recipesCustomLinkedList = new CustomLinkedList<>();

    private int selectedRecipeIndex = -1;
    public void addRecipe (ActionEvent event) throws IOException{
        selectedRecipeIndex = recipeListView.getSelectionModel().getSelectedIndex(); //in case of updated ingredient

        String recipeName = recipeNameTextField.getText();
        Drinks drinksInRecipe = (Drinks) drinksInRecipeListView.getSelectionModel().getSelectedItem();
        Ingredients ingredientsInRecipe = (Ingredients) ingredientsInRecipeListView.getSelectionModel().getSelectedItem();


        Recipes newRecipe = new Recipes(recipeName, drinksInRecipe, ingredientsInRecipe);
        if (selectedRecipeIndex >= 0) {
            // update existing recipe
            recipesCustomLinkedList.setAtIndex(selectedRecipeIndex, newRecipe);
            recipeListView.getItems().set(selectedRecipeIndex, newRecipe.toString());
            saveRecipe();
        } else {
            // add new recipe
            recipesCustomLinkedList.add(newRecipe);
            recipeListView.getItems().add(newRecipe.toString());
            saveRecipe();
        }
        selectedRecipeIndex = -1; //-delete later????
        recipeNameTextField.clear();
        ingredientsInRecipeListView.getSelectionModel().clearSelection();
        drinksInRecipeListView.getSelectionModel().clearSelection();
        ingredientListView.getSelectionModel().clearSelection();
    }

    //To put selected index's information into text fields
    public void updateRecipe(ActionEvent event) throws IOException {
        selectedRecipeIndex = recipeListView.getSelectionModel().getSelectedIndex();
        if (selectedRecipeIndex >= 0) {


            String recipeName = recipeNameTextField.getText();
            Drinks drinksInRecipe = (Drinks) drinksInRecipeListView.getSelectionModel().getSelectedItem();
            Ingredients ingredientsInRecipe = (Ingredients) ingredientsInRecipeListView.getSelectionModel().getSelectedItem();

            Recipes updatedRecipe = new Recipes(recipeName, drinksInRecipe, ingredientsInRecipe);

            //update the selected recipe with the updated data
            recipesCustomLinkedList.update(selectedRecipeIndex, updatedRecipe);

            //show the changes in the list view
            recipeListView.getItems().set(selectedRecipeIndex, updatedRecipe.toString());

            //save the updated recipe
            saveRecipe();

            //clear selection and text fields
            selectedRecipeIndex = -1; //-delete later????
            recipeNameTextField.clear();
            ingredientsInRecipeListView.getSelectionModel().clearSelection();
            drinksInRecipeListView.getSelectionModel().clearSelection();
            ingredientListView.getSelectionModel().clearSelection();


        } else {
            System.out.println("No recipe selected for update.");
        }
    }

    public void deleteRecipe(ActionEvent event) throws Exception {
        int selectedIndex = recipeListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1 ){
            recipesCustomLinkedList.remove(selectedIndex);
            recipeListView.getItems().remove(selectedIndex);
            saveRecipe();

        }
        recipeListView.getSelectionModel().clearSelection();
    }


    public void saveRecipe() throws IOException {
        File file = new File("src/main/resources/com/example/drink_system/recipes.xml");
        XStream xstream = new XStream(new DomDriver());
        xstream.allowTypeHierarchy(Recipes.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        ObjectOutputStream os = xstream.createObjectOutputStream(new FileWriter(file));
        os.writeObject(recipesCustomLinkedList);
        System.out.println ("recipes added to the file"); //---future debug
        os.close();
    }

    public void loadRecipes() {
        File file = new File("src/main/resources/com/example/drink_system/recipes.xml");
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        //list of classes for serialisation
        xstream.allowTypeHierarchy(Recipes.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        try {
            ObjectInputStream in = xstream.createObjectInputStream(new FileReader(file));
            //load the xml data into recipeList
            recipesCustomLinkedList = (CustomLinkedList<Recipes>) in.readObject();
            System.out.println("recipes are loaded.");//debug
            for (int i = 0; i < recipesCustomLinkedList.size(); i++) { //populating listview with loaded recipes
                recipeListView.getItems().add(recipesCustomLinkedList.getAtIndex(i).toString());
            }
            in.close();
        } catch (Exception error) {
            error.printStackTrace();
            System.err.println("error loading from xml: " + error.getMessage()); //debug
        }
    }


    //method to reset entire system
    @FXML
    private void resetDrinkSystem(ActionEvent event) {

    }



}