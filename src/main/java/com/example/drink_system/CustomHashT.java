package com.example.drink_system;
import java.util.ArrayList; //we are allowed to use the regular arrays for the hash but idk
class HNode<K, V>{
   //key and value are not built-in it is just convention
    K key;
    V value;
    HNode<K, V> next;
    HNode(K key, V value){
        this.key = key; //like index
        this.value = value; //attribute
        this.next = null; //the same as the custom linked list
    }
}


public class CustomHashT<K, V> {
    public HNode<K, V>[] table; //table is variable used to store array

    public int size; //size of array list
    public int capacity; //current capacity of array list

    //constructor for hash table
    public CustomHashT(int capacity) {
        table = new HNode[capacity]; //idk ab this one  //create an array of nodes
        this.capacity = capacity; //dynamic capacity
        this.size = 0;

//        //create empty chains (linked lists that are used for collision handling in a hash table
//        for (int i = 0; i < capacity; i++)
//        {
//            table.add(null);
//        }

    }


    public int size(){ return size; }

    //in java every object has its own hash code to ensure its uniqueness
   public int customHashCode ( K key){
        String newKey = key.toString(); //convert key to string
        int hash = 0;
        for (int i=0; i < newKey.length(); i++){
            hash +=(newKey.charAt(i)*(i+1)); //multiply ascii values with position
        }

        return hash%capacity;
   }
   //I wanna cry


    //add or update key value pair
    public void put(K key,V value){
        int i = customHashCode(key);
        HNode current = table [i];
        while (current != null){
            if (current.key.equals(key)){
                current.value = value;
                return;
            }
            current= current.next;
        }
        HNode newHNode = new HNode(key, value);
        newHNode.next = table[i];
        table[i] = newHNode;
        size++;
    }

    //returns for a key
    public V get(K key){
        int i = customHashCode(key);
        HNode<K, V> current = table[i];

        //traversing chain
        while (current!= null) {
            if (current.key.equals(key)){
                return current.value;
            }
            current = current.next;
        }
        return null; //if key is not found
    }

    public boolean remove(K key){
        int i = customHashCode(key);
        HNode<K, V> current = table [i];
        HNode<K, V> prev = null;

        while (current != null){
            if (current.key.equals(key)){ //search for key
                if (prev == null){ //if there is no node before the current
                    table[i] = current.next; //removes head
                }
                else {
                    prev.next = current.next; //removes from the chain
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;

    }

    //more of debug and test method at the moment
    public void display() {
        for (int i = 0; i < capacity; i++) {
            System.out.print("bucket " + i + ": "); //the hash tab;e is implemented as an array of buckets
            HNode<K, V> current = table[i];
            while (current != null) {
                System.out.print("[" + current.key + ": " + current.value + "] -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }

}
