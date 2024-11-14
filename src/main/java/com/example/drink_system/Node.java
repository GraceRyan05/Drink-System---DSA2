package com.example.drink_system;


//This class defines what a node looks like - needed for the CustomLinkedList class
public class Node<R> {
    public  Node<R> next = null; //next node reference
    private R contents; //the contents stored in the node

    public R getContents() { //getter for contents
        return contents;
    }

    public void setContents(R cont) { //setter for contents
        contents = cont;
    }

}
