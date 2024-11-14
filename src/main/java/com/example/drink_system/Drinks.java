package com.example.drink_system;

public class Drinks {
    private String drinkName;
    private String countryOfOrigin;
    private String textualDescription;

    public Drinks(String drinkName, String countryOfOrigin, String textualDescription) {
        this.drinkName = drinkName;
        this.countryOfOrigin = countryOfOrigin;
        this.textualDescription = textualDescription;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getTextualDescription() {
        return textualDescription;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setTextualDescription(String textualDescription) {
        this.textualDescription = textualDescription;
    }

    public String toString() {
        return "Drink Name: " + drinkName + ", Country of Origin: " + countryOfOrigin + " Textual Description: " + textualDescription;
    }

}
