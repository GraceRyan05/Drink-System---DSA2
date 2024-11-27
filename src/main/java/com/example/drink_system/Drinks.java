package com.example.drink_system;

public class Drinks {
    private String drinkName;
    private String countryOfOrigin;
    private String textualDescription;

    private String imageURL; //Peter asks for URL in the description and not an actual pic so I hope we are gonna be fine with that

    public Drinks(String drinkName, String countryOfOrigin, String textualDescription, String imageURL) {
        this.drinkName = drinkName;
        this.countryOfOrigin = countryOfOrigin;
        this.textualDescription = textualDescription;
        this.imageURL = imageURL;
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

    public String getImageURL(){
        return imageURL;
    }
    public void setImageURL(){
        this.imageURL = imageURL;
    }
    public String toString() {
        return "Drink Name: " + drinkName + ", Country of Origin: " + countryOfOrigin + ", Textual Description: " + textualDescription + ", Image URL: " +imageURL;
    }

}
