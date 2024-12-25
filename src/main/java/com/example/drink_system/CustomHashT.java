package com.example.drink_system;
import java.util.ArrayList;
class HNode<K, V>{
    K key;
    V value;
    HNode<K, V> next;
    HNode(K key, V value){
        this.key = key;
        this.value = value;
        this.next = null;
    }
}


public class CustomHashT<K, V> {
    private ArrayList<HNode<K, V>> table; //table is used to store array

    private int size; //size of array list
    private int capacity; //current capacity of array list



    //constructor
    public CustomHashT() {
        table = new ArrayList<>();
        capacity = 20;
        size = 0;

        //create empty chains (linked lists that are used for collision handling in a hash table
        for (int i = 0; i < capacity; i++)
        {
            table.add(null);
        }

    }

    public int size(){ return size; }
//    private int hashCode (K key){
//        //to do
//        return Objects.
//    }
}
