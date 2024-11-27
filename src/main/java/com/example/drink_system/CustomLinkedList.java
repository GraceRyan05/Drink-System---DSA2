package com.example.drink_system;


import java.util.Iterator; //used to create an object to go through a collection of items
import java.util.NoSuchElementException;

//This class is used to represent the linked list itself. This class is used to manage the nodes from the node class
public class CustomLinkedList<G> implements Iterable<G>{ //need to implement iterable here otherwise will get errors trying to use a for each loop - "not applicable here" (Now has the ability to go through each item in the list)
    //The G stands for generic type - the list can hold any data type (eg. String, int, etc.)

    private Node<G> head = null; //head of the list
    //First item in the list - starts as null as the list is initially empty


    //method to add a new node to the head of the list
    public void add(G data) {
        Node<G> newNode = new Node<>(); //create a new node

        newNode.setContents(data); //sets the node's content to the provided data
        newNode.next = head; //link the new node to the current head of the list
        head = newNode; //update head to the new node -- adds it to the front of the list
    }

    //method to delete a node from the list
    public boolean delete(G data) {
        if (head == null) { //checks if list is empty
            return false;
        }

        if (head.getContents().equals(data)) { //checks if the first node has the data to delete
            //if it does, it removes the node by updating the head
            head = head.next; //remove the head node
            return true; //converts the value to true
        }
        //search for the node to be deleted, keep track of the previous node
        Node<G> current = head;
        Node<G> previous = null;

        //if the data isn't at the head, it goes through the list until it finds the node to delete
        while (current != null && !current.getContents().equals(data)) {
            previous = current; //move to the previous node
            current = current.next; //move to the next noe
        }
        //if data was not present in the list
        if (current == null) {
            return false;
        }
        //unlink the node from the linked list
        previous.next = current.next;
        return true; //data successfully deleted
    }

    //search for an element in the list
    public boolean search(G data) {
        Node<G> current = head; //start from the head
        //loops through each node to see if its contents match the data
        while (current != null) {
            //if it finds a match it returns true
            if (current.getContents().equals(data)) { //check if data matches
                return true; //data found
            }
            current = current.next; //move to the next node
        }
        return false; //data not found
    }

    //get size of a list
    public int size() {
        int count = 0;
        Node<G> current = head; // starting from the head
        while (current != null) { // traverse until the end
            count++;
            current = current.next;
        }
        System.out.println("Size of CustomList: " + count); // debugging
        return count;
    }

    //get an element at a certain index in the list
    public G getAtIndex(int index) {
        Node<G> current = head; //starts at the head
        int countIndex = 0; //uses a counter to move to the necessary index
        //when at the index, it returns the node's data
        while (current != null) {
            if (countIndex == index) {
                return current.getContents();
            }
            countIndex++;
            current = current.next;
        }
        return null; //index is out of bounds
    }

    //clear the entire list
    public void clear() {
        head = null;
    }

    //method to display the linked list
    public void display() {
        Node<G> current = head; //start from the head
        //prints the data at each node
        while (current != null) {
            System.out.println(current.getContents() + " -> "); //print data
            current = current.next; //move to the next node
        }
        System.out.println("End of list");
    }

    //This method is used to find specific information from the LinkedList
    public boolean contains(G data) {
        Node<G> current = head; //start at the head
        while (current != null) {
            if (current.getContents().equals(data)) { //check if matches the inputted data
                return true;
            }
            current = current.next; //move to the next node

        }
        return false; //if nothing found will just return false
    }


    //This class allows the list to be looped through easily
    public class CustomIterator implements Iterator<G> {
        Node<G> current = head; //start at the head of the list

        public boolean hasNext() {
            return current != null;
        } //checks if there is another node after the current one

        //moves to the next node in the list and returns its data
        //if there is no node, it throws a noSuchElementException
        public G next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            G data = current.getContents();
            current = current.next;
            return data;
        }
    }


    //Needed as the linkedList implements iterator - returns an instance of CustomIterator, which makes the list usable in a loop
    @Override
    public Iterator<G> iterator() {
        return new CustomIterator();
    }

}






