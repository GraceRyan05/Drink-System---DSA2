package com.example.drink_system.tests;

import com.example.drink_system.CustomHashT;

public class TestHashTable {
    public static void main(String[] args) {
        CustomHashT<String, String> hashTable = new CustomHashT<>(10);
        //add drinks
        hashTable.put("mojito", "mint, lime, rum, sugar, soda");
        hashTable.put("margarita", "tequila, triple sec, lime juice");
        hashTable.put("bloody mary", "vodka, tom juice, spices");
        //return a drink
        System.out.println("mojito recipe: " + hashTable.get("mojito"));
        //remove a drink
        hashTable.remove("margarita");

        //display the hash table
        hashTable.display();
        //check if a drink exists
        System.out.println("contains margarita? " + hashTable.remove("margarita"));
    }
}
