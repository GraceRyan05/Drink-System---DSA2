package com.example.drink_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws Exception {

        //Loads the fxml file - stores the GUI information
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        //The scene - represents the visual contents of the stage
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Drink Selection System"); //add a title for the stage
        stage.show(); //shows the stage - opens the window of the GUI and all contents
    }
}