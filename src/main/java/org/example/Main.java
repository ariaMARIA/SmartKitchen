// Main.java - Runs the program
package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Smart Kitchen Inventory");

        Label label = new Label("🍽️ Smart Kitchen Inventory System");
        Button addIngredientBtn = new Button("Add Ingredient");
        Button addRecipeBtn = new Button("Add Recipe");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, addIngredientBtn, addRecipeBtn);

        Scene scene = new Scene(layout, 400, 300);

        // Apply CSS
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
