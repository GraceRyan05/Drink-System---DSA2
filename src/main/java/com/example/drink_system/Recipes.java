package com.example.drink_system;

import javafx.collections.ObservableList;

import java.util.Objects;

public class Recipes {

    private String recipeName;

    private Drinks drinkDetails;

    private Ingredients ingredientDetails;

    public Recipes(String recipeName, Drinks drinkDetails, Ingredients ingredientDetails) {
        this.recipeName = recipeName;
        this.drinkDetails = drinkDetails;
        this.ingredientDetails = ingredientDetails;
    }

    public String getRecipeName() {
        return recipeName;
    }
    public Drinks getDrinkDetails() {
        return drinkDetails;
    }
    public Ingredients getIngredientDetails() {
        return ingredientDetails;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setDrinkDetails(Drinks drinkDetails) {
        this.drinkDetails = drinkDetails;
    }
    public void setIngredientDetails(Ingredients ingredientDetails) {
        this.ingredientDetails = ingredientDetails;
    }

    public String toString() {
        return "Recipe Name: " + recipeName + ", Drink Details: " + drinkDetails + ", Ingredient Details: " + ingredientDetails;
    }


    //Recipes equals method - generated
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipes recipes)) return false;
        return Objects.equals(recipeName, recipes.recipeName) && Objects.equals(drinkDetails, recipes.drinkDetails) && Objects.equals(ingredientDetails, recipes.ingredientDetails);
    }

    //Recipes hashcode method - generated
    @Override
    public int hashCode() {
        return Objects.hash(recipeName, drinkDetails, ingredientDetails);
    }
}
