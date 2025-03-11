

package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class UsedIngredientApp extends Application {

    private final ObservableList<UsedIngredient> usedData = FXCollections.observableArrayList();
    private final File usedFile = new File("used_ingredients.txt");

    @Override
    public void start(Stage primaryStage) {
        loadUsedData();

        // Title Label
        Label titleLabel = new Label("🍳 Used Ingredients");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");

        // Table setup
        TableView<UsedIngredient> usedTable = new TableView<>();
        usedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<UsedIngredient, ImageView> imageCol = new TableColumn<>("Image");
        imageCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getImage()));

        TableColumn<UsedIngredient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<UsedIngredient, String> quantityCol = new TableColumn<>("Quantity Used");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityUsed"));

        usedTable.getColumns().addAll(imageCol, nameCol, quantityCol);
        usedTable.setItems(usedData);

        // Delete Button
        Button deleteButton = new Button("🗑 Delete Ingredient");
        deleteButton.setOnAction(e -> {
            UsedIngredient selected = usedTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                usedData.remove(selected); // Remove from the table
                saveUsedData(); // Update file after deletion
            } else {
                showAlert("Warning", "Please select an ingredient to delete.");
            }
        });

        // Layout setup
        HBox buttonBox = new HBox(10, deleteButton);
        buttonBox.setPadding(new Insets(10));

        VBox vbox = new VBox(10, titleLabel, usedTable, buttonBox);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: #f3e5f5;");

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Used Ingredients");
        primaryStage.show();
    }


    public void addUsedIngredient(UsedIngredient ingredient) {
        loadUsedData();  // Reload previous used ingredients before adding new one
        usedData.add(ingredient);  // Add new ingredient
        saveUsedData();  // Save to file
    }


    void saveUsedData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usedFile))) {
            for (UsedIngredient ingredient : usedData) {
                writer.write(ingredient.getImageUrl() + "," + ingredient.getName() + "," + ingredient.getQuantityUsed() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void loadUsedData() {
        if (!usedFile.exists()) return;

        usedData.clear();  // Clear old data before loading to prevent duplicates

        try (BufferedReader reader = new BufferedReader(new FileReader(usedFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    usedData.add(new UsedIngredient(parts[0], parts[1], parts[2]));  // Store quantity as string
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static class UsedIngredient {
        private String imageUrl;
        private String name;
        private String quantityUsed;  // Store as string (e.g., "2kg", "500g")

        public UsedIngredient(String imageUrl, String name, String quantityUsed) {
            this.imageUrl = imageUrl;
            this.name = name;
            this.quantityUsed = quantityUsed;
        }

        public String getImageUrl() { return imageUrl; }
        public String getName() { return name; }
        public String getQuantityUsed() { return quantityUsed; }

        public ImageView getImage() {
            return new ImageView(new Image(imageUrl, 50, 50, true, true));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}