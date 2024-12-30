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
    ListView ingredientsInSystemList;
    @FXML
    ListView drinksInSystemList;
    @FXML
    ListView recipesInSystemList;

    @FXML
    public void initialize(){
        loadIngredients();
        loadDrinks();
        loadRecipes();


        // Populate ListViews for creating recipes & Populate ListViews for the homePage
        for (Ingredients ingredient : ingredientsCustomLinkedList) {
            ingredientsInRecipeListView.getItems().add(ingredient);
            ingredientsInSystemList.getItems().add(ingredient);
        }
        for (Drinks drink : drinksCustomLinkedList) {
            drinksInRecipeListView.getItems().add(drink);
            drinksInSystemList.getItems().add(drink);
        }
        for (Recipes recipe : recipesCustomLinkedList) {
            recipesInSystemList.getItems().add(recipe);
        }




        ingredientsInRecipeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); //allows for multiple items to be selected to add to the recipe
        drinksInRecipeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        ingredientFilterBy.getItems().addAll("Name", "Description");
        drinkFilterBy.getItems().addAll("Name", "Description");
        recipeFilterBy.getItems().addAll("Name");

    }


    //The lists of each
    private CustomLinkedList<Drinks> drinksList;
    private CustomLinkedList<Ingredients> ingredientsList;
    private CustomLinkedList<Recipes> recipesList;

    //define the hash table instances
    private CustomHashT<String, Drinks> drinksHashTable;
    private CustomHashT<String, Ingredients> ingredientsHashTable;
    private CustomHashT< String, Recipes> recipesHashTable;
    //Controller constructor
    public HelloController() {
        this.drinksList = new CustomLinkedList<>();
        this.ingredientsList = new CustomLinkedList<>();
        this.recipesList = new CustomLinkedList<>();

        drinksHashTable = new CustomHashT<>(20);
        ingredientsHashTable = new CustomHashT<>(20); //I hope its enough
        recipesHashTable = new CustomHashT<>(20); //also guessing 20 capacity
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

        //generating the UNIQUE  hash key
        String baseKey = name; //start with name
        int hashKey = ingredientsHashTable.customHashCode(baseKey); //generate hash code
        //WE WANT FREAKING UNIQUE HASH KEY
        int count = 0;
        String uniqueKey = baseKey + "_" + hashKey + "_" + count; //combine everything together to ensure that we have UNIQUE key
        while (ingredientsHashTable.get(uniqueKey) != null) { //CHECKING IF THE KEY IS NOT UNIQUE
            count++;
            uniqueKey = baseKey + "_" + hashKey + "_" + count; //MAKING SURE THAT WE HAVE REALLY UNIQUE KEY
        }

        if (selectedIngredientIndex >= 0) {
            // update existing ingredient
            ingredientsCustomLinkedList.setAtIndex(selectedIngredientIndex, newIngredient);
            ingredientListView.getItems().set(selectedIngredientIndex, newIngredient.toString());
            ingredientsHashTable.put(uniqueKey, newIngredient);
            saveIngredients();
        } else {
            // add new ingredient
            ingredientsCustomLinkedList.add(newIngredient);
            ingredientListView.getItems().add(newIngredient.toString());
            ingredientsHashTable.put(uniqueKey, newIngredient);
            saveIngredients();
        }

        selectedIngredientIndex = -1; //prob will delete later on
        ingredientNameField.clear();
        ingredientDescriptionField.clear();
        ingredientAlcContentField.clear();
        ingredientListView.getSelectionModel().clearSelection();
    }

    //this method put selected index`s info into text fields
    public void updateIngredient(ActionEvent event) throws IOException {
        selectedIngredientIndex = ingredientListView.getSelectionModel().getSelectedIndex();
        if (selectedIngredientIndex >= 0) {

            String ingredientName = ingredientNameField.getText();
            String ingredientDescription = ingredientDescriptionField.getText();
            double ingredientAlcoholContent = Double.parseDouble(ingredientAlcContentField.getText());

            Ingredients updatedIngredient = new Ingredients(ingredientName, ingredientDescription, ingredientAlcoholContent);

            //update the selected ingredient with the updated data
            ingredientsCustomLinkedList.update(selectedIngredientIndex, updatedIngredient);

            //show the changes in the list view
            ingredientListView.getItems().set(selectedIngredientIndex, updatedIngredient.toString());

            ingredientsHashTable.put(ingredientName, updatedIngredient);

            //save the updated ingredient
            saveIngredients();

            //clear selection and text fields
            selectedIngredientIndex = -1;
            ingredientNameField.clear();
            ingredientDescriptionField.clear();
            ingredientAlcContentField.clear();
        }
            else {
                System.out.println("No ingredient selected for update.");
            }

        }


    public void deleteIngredient(ActionEvent event) throws Exception {
        int selectedIndex = ingredientListView.getSelectionModel().getSelectedIndex();
        if(selectedIndex != -1 ){
            ingredientsCustomLinkedList.remove(selectedIndex);
            ingredientListView.getItems().remove(selectedIndex);
            ingredientsHashTable.remove(ingredientsCustomLinkedList.getAtIndex(selectedIndex).getIngredientName()); //simply gets the name of ingredient
            saveIngredients();
        }
        ingredientListView.getSelectionModel().clearSelection();
    }

    @FXML
    public TextField ingredientSeacrh;
    @FXML
    public ListView ingredientSearchResult;
    @FXML
    public Button ingredientSearchButton;

    public void searchIngredientByName(String name) {
        boolean found = false;
        for (int i = 0; i < ingredientsHashTable.capacity; i++) { //directly access the table array
            HNode<String, Ingredients> node = ingredientsHashTable.table[i]; //start of bucket chain
            //traverse the chain in each bucket
            while (node != null) {
                Ingredients ingredient = node.value; //access value
                if (ingredient.getIngredientName().contains(name)) {
                    sortIngredientsAlphabeticallyMethodCall();
                    ingredientSearchResult.getItems().add(ingredient.toString());
                    found = true;
                }
                node = node.next; //move to the next node
            }
        }
        if (!found) {
            ingredientSearchResult.getItems().add("no ingredients with such name");
        }



    }


    public void searchIngredientByDescription(String keyWords) {
        boolean found = false;
        //iterate through all buckets
        for (int i = 0; i < ingredientsHashTable.capacity; i++) { //directly access the table array
            HNode<String, Ingredients> node = ingredientsHashTable.table[i]; //start of bucket chain
            //traverse the chain in each bucket
            while (node != null) {
                Ingredients ingredient = node.value; //access value
                if (ingredient.getTextualDescription().contains(keyWords)) {
                    sortIngredientsAlphabeticallyMethodCall();
                    ingredientSearchResult.getItems().add(ingredient.toString());
                    found = true;
                }
                node = node.next; //move to the next node
            }
        }
        if (!found) {
            ingredientSearchResult.getItems().add("no ingredients found with description containing: " + keyWords);
        }
    }


    @FXML
    public ComboBox<String> ingredientFilterBy;
    public void switchSearch(ActionEvent event){
        //clear previous results
        ingredientSearchResult.getItems().clear();
        String input = ingredientSeacrh.getText(); //input text

        //check if input is empty
        if (input == null || input.trim().isEmpty()) {
            ingredientSearchResult.getItems().add("Please enter a search term.");
            return;
        }
        if (ingredientFilterBy.getValue().equals("Name")){
            searchIngredientByName(input);
        }
        else if (ingredientFilterBy.getValue().equals("Description")){
            searchIngredientByDescription(input);
        }
    }

    public void sortIngredientsAlphabeticallyMethodCall() {
        int size = ingredientsCustomLinkedList.size();

        //selection sort algorithm
        for (int i = 0; i < size -1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                Ingredients currentIngredient = ingredientsCustomLinkedList.getAtIndex(j);
                Ingredients minIngredient = ingredientsCustomLinkedList.getAtIndex(min);

                //compare the ingredient names alphabetically
                if (currentIngredient.getIngredientName().compareTo(minIngredient.getIngredientName()) < 0) {
                    min = j; //a smaller name was found - update the min value
                }
            }
            //swap the found minimum element with the first unsorted element
            if (min != i) {
                Ingredients swap = ingredientsCustomLinkedList.getAtIndex(i);
                ingredientsCustomLinkedList.setAtIndex(i, ingredientsCustomLinkedList.getAtIndex(min));
                ingredientsCustomLinkedList.setAtIndex(min, swap);
            }
        }

    }


    @FXML
    Button sortIngredientsButton;

    public void sortIngredientsAlphabetically(ActionEvent event) {
        int size = ingredientsCustomLinkedList.size();

        //selection sort algorithm
        for (int i = 0; i < size -1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                Ingredients currentIngredient = ingredientsCustomLinkedList.getAtIndex(j);
                Ingredients minIngredient = ingredientsCustomLinkedList.getAtIndex(min);

                //compare the ingredient names alphabetically
                if (currentIngredient.getIngredientName().compareTo(minIngredient.getIngredientName()) < 0) {
                    min = j; //a smaller name was found - update the min value
                }
            }
            //swap the found minimum element with the first unsorted element
            if (min != i) {
                Ingredients swap = ingredientsCustomLinkedList.getAtIndex(i);
                ingredientsCustomLinkedList.setAtIndex(i, ingredientsCustomLinkedList.getAtIndex(min));
                ingredientsCustomLinkedList.setAtIndex(min, swap);
            }
        }
        //after the sort update the listview
        ingredientListView.getItems().clear();

        for (int i = 0; i < ingredientsCustomLinkedList.size(); i++) {
            ingredientListView.getItems().add(ingredientsCustomLinkedList.getAtIndex(i).toString());
          //Ignore this - just for testing if it works
            //  ingredientSearchResult.getItems().add(ingredientsCustomLinkedList.getAtIndex(i).toString());
        }
    }


    @FXML
    Button sortIngredientsButtonABV;

    public void sortIngredientsByABV(ActionEvent event) {
        int size = ingredientsCustomLinkedList.size();

        //selection sort algorithm
        for (int i = 0; i < size -1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                Ingredients currentIngredient = ingredientsCustomLinkedList.getAtIndex(j);
                Ingredients minIngredient = ingredientsCustomLinkedList.getAtIndex(min);

                //compare the ingredient names alphabetically
                if (currentIngredient.getAlcoholContent() < minIngredient.getAlcoholContent()) {
                    min = j; //a smaller name was found - update the min value
                }
            }
            //swap the found minimum element with the first unsorted element
            if (min != i) {
                Ingredients swap = ingredientsCustomLinkedList.getAtIndex(i);
                ingredientsCustomLinkedList.setAtIndex(i, ingredientsCustomLinkedList.getAtIndex(min));
                ingredientsCustomLinkedList.setAtIndex(min, swap);
            }
        }
        //after the sort update the listview
        ingredientListView.getItems().clear();

        for (int i = 0; i < ingredientsCustomLinkedList.size(); i++) {
            ingredientListView.getItems().add(ingredientsCustomLinkedList.getAtIndex(i).toString());
            //Ignore this - just for testing if it works
            //  ingredientSearchResult.getItems().add(ingredientsCustomLinkedList.getAtIndex(i).toString());
        }
    }

    public void saveIngredients() throws IOException {
        File file = new File("src/main/resources/com/example/drink_system/ingredient.xml");
        XStream xstream = new XStream(new DomDriver());
        xstream.allowTypeHierarchy(Ingredients.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        ObjectOutputStream os = xstream.createObjectOutputStream(new FileWriter(file));
        os.writeObject(ingredientsCustomLinkedList);
        os.writeObject(ingredientsHashTable);
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
            //load the xml data into ingredientsList
            ingredientsCustomLinkedList = (CustomLinkedList<Ingredients>) in.readObject();
           ingredientsHashTable = new CustomHashT<>(10);//clear and reinitialize existing hash table
            System.out.println("ingredients are loaded.");//debug
            for (int i = 0; i < ingredientsCustomLinkedList.size(); i++) { //populating listview with loaded ingredients
                Ingredients ingredient = ingredientsCustomLinkedList.getAtIndex(i);
                ingredientListView.getItems().add(ingredientsCustomLinkedList.getAtIndex(i).toString());
                int hashKey = ingredientsHashTable.customHashCode(ingredient.getIngredientName());
                String uniqueKey = ingredient.getIngredientName() + "_" + hashKey + "_" + i; //unique key ;-;
                ingredientsHashTable.put(uniqueKey, ingredient);
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
        selectedDrinkIndex = drinkListView.getSelectionModel().getSelectedIndex(); //in case of updated drink

        String name = drinkNameField.getText();
        String description = drinkDescriptionField.getText();
        String origin = drinkCountryOfOriginField.getText();
        String url = drinkURLImageField.getText();

        Drinks newDrink = new Drinks(name, description, origin, url);

        //generating the unique hash key
        String baseKey = name; //start with name
        int hashKey = drinksHashTable.customHashCode(baseKey); //generate hash code

        //looking for unique hash key
        int count = 0;
        String uniqueKey = baseKey + "_" + hashKey + "_" + count; //combine everything together to ensure we have a unique key

        while (drinksHashTable.get(uniqueKey) != null) { //checking if the key is not unique
            count++;
            uniqueKey = baseKey + "_" + hashKey + "_" + count; //making sure the key is properly unique
        }

        if (selectedDrinkIndex >= 0){
            //update existing drink
            drinksCustomLinkedList.setAtIndex(selectedDrinkIndex, newDrink);
            drinkListView.getItems().set(selectedDrinkIndex, newDrink);

            drinksHashTable.put(uniqueKey, newDrink);

            saveDrinks();
        }
        else{
            drinksCustomLinkedList.add(newDrink);
            drinkListView.getItems().add(newDrink.toString());

            drinksHashTable.put(uniqueKey, newDrink);
            saveDrinks();
        }

        drinkListView.getSelectionModel().clearSelection();
        drinkNameField.clear();
        drinkDescriptionField.clear();
        drinkCountryOfOriginField.clear();
        drinkURLImageField.clear();
    }



    //method to update the drinks
    public void updateDrink(ActionEvent event) throws IOException {
        selectedDrinkIndex = drinkListView.getSelectionModel().getSelectedIndex();

        if (selectedDrinkIndex >= 0 ) {
            String drinkName = drinkNameField.getText();
            String countryOfOrigin = drinkCountryOfOriginField.getText();
            String description = drinkDescriptionField.getText();
            String image = drinkURLImageField.getText();

            Drinks updatedDrink = new Drinks(drinkName, countryOfOrigin, description, image);

            //update the selected drink with the updated data
            drinksCustomLinkedList.update(selectedDrinkIndex, updatedDrink);

            //show the changes in the list view
            drinkListView.getItems().set(selectedDrinkIndex, updatedDrink.toString());

            drinksHashTable.put(drinkName, updatedDrink);

            //save the updated drink
            saveDrinks();

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

            drinksHashTable.remove(drinksCustomLinkedList.getAtIndex(selectedIndex).getDrinkName()); //simply get the name of the drink
            saveDrinks();

        }
        drinkListView.getSelectionModel().clearSelection();
    }

    @FXML
    public TextField drinkSearch;
    @FXML
    public ListView drinkSearchResult;
    @FXML
    public Button drinkSearchButton;

    public void searchDrinkByName (String name) {
        boolean found = false;
        for (int i = 0; i < drinksHashTable.capacity; i++) { //directly access the table array
            HNode<String, Drinks> node = drinksHashTable.table[i]; //start of bucket chain
            //traverse the chain in each bucket
            while (node != null) {
                Drinks drink = node.value; //access value
                if (drink.getDrinkName().contains(name)) {
                    sortDrinksAlphabeticallyMethodCall();
                    drinkSearchResult.getItems().add(drink.toString());
                    found = true;
                }
                node = node.next; //move to the next node
            }
        }
        if (!found) {
            drinkSearchResult.getItems().add("No drinks with such name");
        }
    }

    public void searchDrinksByDescription(String keyWords) {
        boolean found = false;
        //iterate through all buckets
        for ( int i = 0; i < drinksHashTable.capacity; i++) { //directly access the table array
            HNode<String, Drinks> node = drinksHashTable.table[i];
            //start of bucket chain
            //traverse the chain in each bucket
            while(node != null) {
                Drinks drink = node.value; //access value;
                if (drink.getTextualDescription().contains(keyWords)) {
                    sortDrinksAlphabeticallyMethodCall();
                    drinkSearchResult.getItems().add(drink.toString());
                    found = true;
                }
                node = node.next; //move to the next node
            }
        }
        if (!found) {
            drinkSearchResult.getItems().add("No drinks found with description containing: " + keyWords);
        }
    }

    @FXML
    public ComboBox<String> drinkFilterBy;
    public void switchSearchDrink(ActionEvent event) {
        //clear previous results
        drinkSearchResult.getItems().clear();
        String input = drinkSearch.getText(); //input text

        //check if input is empty
        if (input == null || input.trim().isEmpty()) {
            drinkSearchResult.getItems().add("Please enter a search term.");
            return;
        }
        if (drinkFilterBy.getValue().equals("Name")) {
            searchDrinkByName(input);
        }
        else if (drinkFilterBy.getValue().equals("Description")) {
            searchDrinksByDescription(input);
        }
    }

    public void sortDrinksAlphabeticallyMethodCall() {
        int size = drinksCustomLinkedList.size();

        //selection sort algorithm
        for (int i = 0; i < size -1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                Drinks currentDrink = drinksCustomLinkedList.getAtIndex(j);
                Drinks minDrink = drinksCustomLinkedList.getAtIndex(min);

                //compare the drink names alphabetically
                if (currentDrink.getDrinkName().compareTo(minDrink.getDrinkName()) < 0) {
                    min = j; //a smaller name was found - update the min value
                }
            }
            //swap the found minimum element with the first unsorted element
            if (min != i) {
                Drinks swap = drinksCustomLinkedList.getAtIndex(i);
                drinksCustomLinkedList.setAtIndex(i, drinksCustomLinkedList.getAtIndex(min));
                drinksCustomLinkedList.setAtIndex(min, swap);
            }
        }
    }

    @FXML
    Button sortDrinksButton;

    public void sortDrinksAlphabetically(ActionEvent event) {
        int size = drinksCustomLinkedList.size();

        //selection sort algorithm
        for (int i = 0; i < size -1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                Drinks currentDrink = drinksCustomLinkedList.getAtIndex(j);
                Drinks minDrink = drinksCustomLinkedList.getAtIndex(min);

                //compare the drink names alphabetically
                if (currentDrink.getDrinkName().compareTo(minDrink.getDrinkName()) < 0) {
                    min = j; //a smaller name was found - update the min value
                }
            }
            //swap the found minimum element with the first unsorted element
            if (min != i) {
                Drinks swap = drinksCustomLinkedList.getAtIndex(i);
                drinksCustomLinkedList.setAtIndex(i, drinksCustomLinkedList.getAtIndex(min));
                drinksCustomLinkedList.setAtIndex(min, swap);
            }
        }
        //after the sort update the listview
        drinkListView.getItems().clear();

        for (int i = 0; i < drinksCustomLinkedList.size(); i++) {
            drinkListView.getItems().add(drinksCustomLinkedList.getAtIndex(i).toString());
        }
    }



    public void saveDrinks() throws IOException {
        File file = new File("src/main/resources/com/example/drink_system/drinks.xml");
        var xstream = new XStream(new DomDriver());
        xstream.allowTypeHierarchy(Drinks.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        ObjectOutputStream os = xstream.createObjectOutputStream(new FileWriter(file));
        os.writeObject(drinksCustomLinkedList);
        os.writeObject(drinksHashTable);
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
            drinksHashTable = new CustomHashT<>(10); //clear and reinitialize existing hash table
            System.out.println("drinks are loaded.");//debug
            for (int i = 0; i < drinksCustomLinkedList.size(); i++) { //populating listview with loaded ingredients
                Drinks drink = drinksCustomLinkedList.getAtIndex(i);

                drinkListView.getItems().add(drinksCustomLinkedList.getAtIndex(i).toString());

                int hashKey = drinksHashTable.customHashCode(drink.getDrinkName());
                String uniqueKey = drink.getDrinkName() + "_" + hashKey + "_" + i; //unique key
                drinksHashTable.put(uniqueKey, drink);
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

        //generating the unique hash key
        String baseKey = recipeName; //start with the recipe name
        int hashKey = recipesHashTable.customHashCode(baseKey); //generate hash code
        //we want unique hash key
        int count = 0;
        String uniqueKey = baseKey + "_" + hashKey + "_" + count; //combine everything together to ensure we have unique key
        while (recipesHashTable.get(uniqueKey) != null) { //checking if the key is not unique
            count++;
            uniqueKey = baseKey + "_" + hashKey + "_" + count; //making sure we really have unique key

        }
        if (selectedRecipeIndex >= 0) {
            // update existing recipe
            recipesCustomLinkedList.setAtIndex(selectedRecipeIndex, newRecipe);
            recipeListView.getItems().set(selectedRecipeIndex, newRecipe.toString());
            recipesHashTable.put(uniqueKey, newRecipe);
            saveRecipe();
        } else {
            // add new recipe
            recipesCustomLinkedList.add(newRecipe);
            recipeListView.getItems().add(newRecipe.toString());
            recipesHashTable.put(uniqueKey, newRecipe);
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

    @FXML
    public TextField recipeSearch;
    @FXML
    public ListView recipeSearchResult;
    @FXML
    public Button recipeSearchButton;

    public boolean searchingRecipeByName(String recipeName) {
        boolean found = false;
        for (int i = 0; i < recipesHashTable.capacity; i++) { //directly access the table array
            HNode<String, Recipes> node = recipesHashTable.table[i]; //start of bucket chain
            //traverse the chain in each bucket
            while (node != null) {
                Recipes recipe = node.value; //access value
                if (recipe.getRecipeName().contains(recipeName)) {
                    recipeSearchResult.getItems().add(recipe.toString());
                    found = true;
                }
                node = node.next; //move to the next node
            }
        }
        if (!found) {
            recipeSearchResult.getItems().add("No recipes with such name");
        }
        return found;
    }

    @FXML
    public ComboBox<String> recipeFilterBy;
    public void switchSearchRecipe(ActionEvent event) {
        //clear previous results
        recipeSearchResult.getItems().clear();
        String input = recipeSearch.getText(); //input text

        //check if input is empty
        if ( input == null || input.trim().isEmpty()) {
            recipeSearchResult.getItems().add("Please enter a search term.");
            return;
        }
        if (recipeFilterBy.getValue().equals("Name")) {
            searchingRecipeByName(input);
        }
    }


    @FXML
    Button sortRecipesButton;

    public void sortRecipesAlphabetically(ActionEvent event) {
        int size = recipesCustomLinkedList.size();

        //selection sort algorithm
        for (int i = 0; i < size -1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                Recipes currentRecipe = recipesCustomLinkedList.getAtIndex(j);
                Recipes minRecipe = recipesCustomLinkedList.getAtIndex(min);

                //compare the recipe names alphabetically
                if (currentRecipe.getRecipeName().compareTo(minRecipe.getRecipeName()) < 0) {
                    min = j; //a smaller name was found - update the min value
                }
            }
            //swap the found minimum element with the first unsorted element
            if (min != i) {
                Recipes swap = recipesCustomLinkedList.getAtIndex(i);
                recipesCustomLinkedList.setAtIndex(i, recipesCustomLinkedList.getAtIndex(min));
                recipesCustomLinkedList.setAtIndex(min, swap);
            }
        }
        //after the sort update the listview
        recipeListView.getItems().clear();

        for (int i = 0; i < recipesCustomLinkedList.size(); i++) {
            recipeListView.getItems().add(recipesCustomLinkedList.getAtIndex(i).toString());

        }
    }



    public void saveRecipe() throws IOException {
        File file = new File("src/main/resources/com/example/drink_system/recipes.xml");
        XStream xstream = new XStream(new DomDriver());
        xstream.allowTypeHierarchy(Recipes.class);
        xstream.allowTypeHierarchy(CustomLinkedList.class);
        ObjectOutputStream os = xstream.createObjectOutputStream(new FileWriter(file));
        os.writeObject(recipesCustomLinkedList);
        os.writeObject(recipesHashTable);
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
            recipesHashTable = new CustomHashT<>(10); //clear and reinitialize existing hash table
            System.out.println("recipes are loaded.");//debug
            for (int i = 0; i < recipesCustomLinkedList.size(); i++) { //populating listview with loaded recipes
                Recipes recipe = recipesCustomLinkedList.getAtIndex(i);
                recipeListView.getItems().add(recipesCustomLinkedList.getAtIndex(i).toString());

                int hashKey = recipesHashTable.customHashCode(recipe.getRecipeName());
                String uniqueKey = recipe.getRecipeName() + "_" + hashKey + "_" + i; //unique key
                recipesHashTable.put(uniqueKey, recipe);
            }

            in.close();
        } catch (Exception error) {
            error.printStackTrace();
            System.err.println("error loading from xml: " + error.getMessage()); //debug
        }
    }


    @FXML
    private Button resetWholeDrinkSystem;
    //method to reset entire system
    @FXML
    private void resetDrinkSystem(ActionEvent event) {
        ingredientListView.getItems().clear();
        drinkListView.getItems().clear();
        recipeListView.getItems().clear();

        ingredientsList.clear();
        drinksList.clear();
        recipesList.clear();
    }



}