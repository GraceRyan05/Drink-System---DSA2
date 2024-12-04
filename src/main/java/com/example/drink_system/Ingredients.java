package com.example.drink_system;

public class Ingredients {
    private String ingredientName;
    private String textualDescription;
    private double alcoholContent;




    public Ingredients(String ingredientName, String textualDescription, double alcoholContent) {
        this.ingredientName = ingredientName;
        this.textualDescription = textualDescription;
        this.alcoholContent = alcoholContent;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getTextualDescription() {
        return textualDescription;
    }

    public double getAlcoholContent() {
        return alcoholContent;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setTextualDescription(String textualDescription) {
        this.textualDescription = textualDescription;
    }

    public void setAlcoholContent(double alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public String toString() {
        return "Ingredient Name: " + ingredientName + ", Description: " + textualDescription + ", Alcohol Content (ABV): " + alcoholContent;
    }

}
