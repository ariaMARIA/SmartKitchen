
//greate


package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HomeScreen extends Application {

    private ObservableList<inventoryApp.Ingredient> ingredientList = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        loadIngredients(); // Load ingredients from file

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.CENTER);

        int row = 0, col = 0;

        for (inventoryApp.Ingredient ingredient : ingredientList) {
            VBox ingredientBox = new VBox(10);
            ingredientBox.setAlignment(Pos.CENTER);
            ingredientBox.setStyle("-fx-background-color: #fff5e6; -fx-border-radius: 15px; -fx-border-color: #e91e63; -fx-padding: 10px;");

            try {
                Image image = new Image(ingredient.getImagePath(), 120, 120, true, true);
                ImageView imageView = new ImageView(image);
                ingredientBox.getChildren().add(imageView);
            } catch (Exception e) {
                System.out.println("Error loading image: " + ingredient.getImagePath());
            }

            Text nameText = new Text(ingredient.getName());
            nameText.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            nameText.setTextAlignment(TextAlignment.CENTER);

            Text quantityText = new Text("Quantity: " + ingredient.getQuantity());
            quantityText.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
            quantityText.setTextAlignment(TextAlignment.CENTER);

            ingredientBox.getChildren().addAll(nameText, quantityText);
            gridPane.add(ingredientBox, col, row);

            col++;
            if (col == 3) { // 3 items per row
                col = 0;
                row++;
            }
        }

        // Scrollable section for the ingredient list
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Refresh Button
        Button refreshButton = new Button("🔄 Refresh");
        refreshButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white; -fx-font-weight: bold;");
        refreshButton.setOnAction(e -> {
            ingredientList.clear();
            loadIngredients();
            start(stage); // Restart UI to reflect updates
        });
        refreshButton.setStyle("-fx-padding: 10px; -fx-font-size: 14px; -fx-background-color: #FF4081;");
        refreshButton.setMaxWidth(200);
        refreshButton.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(15, refreshButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));

        // Title for the Ingredient List
        Text titleText = new Text("List of Ingredients");
        titleText.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");
        titleText.setTextAlignment(TextAlignment.CENTER);

        VBox layout = new VBox(15, titleText, scrollPane, hbox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #FCE4EC;");

        Scene scene = new Scene(layout, 600, 600);
        stage.setScene(scene);
        stage.setTitle("🏠 Home Screen - Ingredient Overview");
        stage.show();
    }

    private void loadIngredients() {
        try (BufferedReader reader = new BufferedReader(new FileReader("ingredients.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0];
                    String quantity = parts[1];
                    String expiryDate = parts[2];
                    int calories = Integer.parseInt(parts[3]);
                    String imagePath = parts[4];

                    inventoryApp.Ingredient ingredient = new inventoryApp.Ingredient(name, quantity, expiryDate, calories, imagePath);
                    ingredientList.add(ingredient);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading ingredients: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
