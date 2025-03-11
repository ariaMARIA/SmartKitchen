
package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class inventoryApp extends Application {

    private Label titleLabel;
    public ObservableList<Ingredient> data = FXCollections.observableArrayList();
    private UsedIngredientApp usedIngredientApp;  // reference to UsedIngredientApp

    public inventoryApp() {
        usedIngredientApp = new UsedIngredientApp(); // Initialize UsedIngredientApp
    }

    public Iterable<? extends Ingredient> getInventoryData() {
        return data;
    }

    // Ingredient class with details and image
    public static class Ingredient {
        private String name;
        private String quantity;
        private String expiryDate;
        private int calories;
        private String imagePath;

        public Ingredient(String name, String quantity, String expiryDate, int calories, String imagePath) {
            this.name = name;
            this.quantity = quantity;
            this.expiryDate = expiryDate;
            this.calories = calories;
            this.imagePath = imagePath;
        }

        public String getName() { return name; }
        public String getQuantity() { return quantity; }
        public void setQuantity(String quantity) { this.quantity = quantity; }
        public String getExpiryDate() { return expiryDate; }
        public int getCalories() { return calories; }
        public String getImagePath() { return imagePath; }

        public ImageView getImage() {
            return new ImageView(new Image(imagePath)); // Use JavaFX ImageView here
        }
    }

    @Override
    public void start(Stage primaryStage) {
        titleLabel = new Label("🍽 Smart Kitchen Inventory");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");
        loadIngredients();

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search ingredients...");
        searchField.setStyle("-fx-padding: 8px; -fx-border-radius: 5px; -fx-background-radius: 5px;");

        // FilteredList for dynamic filtering based on search input
        FilteredList<Ingredient> filteredData = new FilteredList<>(data, b -> true);

        // Real-time search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ingredient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;  // If the search field is empty, show all ingredients
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return ingredient.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Table Setup
        TableView<Ingredient> table = new TableView<>();
        table.setStyle("-fx-background-color: #ffffff; -fx-border-color: #E91E63;");

        TableColumn<Ingredient, ImageView> imageCol = new TableColumn<>("Image");
        imageCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getImage()));

        TableColumn<Ingredient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Ingredient, String> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Ingredient, String> expiryCol = new TableColumn<>("Expiry Date");
        expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        TableColumn<Ingredient, Integer> calorieCol = new TableColumn<>("Calories");
        calorieCol.setCellValueFactory(new PropertyValueFactory<>("calories"));

        table.getColumns().addAll(imageCol, nameCol, quantityCol, expiryCol, calorieCol);
        table.setItems(filteredData);  // Use the filtered data for the table

        Button addButton = new Button("➕ Add Ingredient");
        addButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white;");
        addButton.setOnAction(e -> showAddIngredientForm(primaryStage));

        Button useButton = new Button("🍳 Use Ingredient");
        useButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        useButton.setOnAction(e -> {
            Ingredient selectedIngredient = table.getSelectionModel().getSelectedItem();

            if (selectedIngredient != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Use Ingredient");
                dialog.setHeaderText("How much of " + selectedIngredient.getName() + " would you like to use?");
                dialog.setContentText("Enter the quantity:");

                dialog.showAndWait().ifPresent(input -> {
                    try {
                        int amountToUse = Integer.parseInt(input);
                        String currentQuantityStr = selectedIngredient.getQuantity();
                        String unit = currentQuantityStr.replaceAll("[^a-zA-Z]", "");
                        int currentQuantity = Integer.parseInt(currentQuantityStr.replaceAll("[^0-9]", ""));

                        if (amountToUse > currentQuantity) {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "You cannot use more than the available quantity!", ButtonType.OK);
                            alert.show();
                        } else if (amountToUse > 0) {
                            int newQuantity = currentQuantity - amountToUse;
                            selectedIngredient.setQuantity(newQuantity + unit);
                            table.refresh();
                            saveIngredients();

                            // Add the used ingredient to UsedIngredientApp (Persistent storage)
                            usedIngredientApp.addUsedIngredient(new UsedIngredientApp.UsedIngredient(selectedIngredient.getImagePath(), selectedIngredient.getName(), input));

                            // Now you can also save the new state of used ingredients to the used ingredients file
                            usedIngredientApp.saveUsedData();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid amount to use.", ButtonType.OK);
                            alert.show();
                        }

                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid quantity entered. Please enter a valid number.", ButtonType.OK);
                        alert.show();
                    }
                });
            }
        });

        Button deleteButton = new Button("🗑 Delete Ingredient");
        deleteButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Ingredient selectedIngredient = table.getSelectionModel().getSelectedItem();

            if (selectedIngredient != null) {
                data.remove(selectedIngredient);
                table.refresh();
                saveIngredients();
            }
        });

        HBox buttonBox = new HBox(10, addButton, useButton, deleteButton);
        buttonBox.setPadding(new Insets(10));

        VBox vbox = new VBox(10, titleLabel, searchField, table, buttonBox);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: #FCE4EC;");

        Scene scene = new Scene(vbox, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Kitchen Inventory");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> saveIngredients());
    }

    private void showAddIngredientForm(Stage primaryStage) {
        // Form layout for adding ingredient
        VBox formLayout = new VBox(10);
        formLayout.setPadding(new Insets(15));
        formLayout.setStyle("-fx-background-color: #FCE4EC;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Ingredient Name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter Quantity");

        TextField expiryField = new TextField();
        expiryField.setPromptText("Enter Expiry Date");

        TextField calorieField = new TextField();
        calorieField.setPromptText("Enter Calories");

        Button imageButton = new Button("Select Image");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        final String[] imagePath = {""};

        imageButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                imagePath[0] = selectedFile.toURI().toString();
            }
        });

        Button addIngredientButton = new Button("Add Ingredient");
        addIngredientButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white;");
        addIngredientButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String quantity = quantityField.getText().trim();
            String expiryDate = expiryField.getText().trim();
            String calorieStr = calorieField.getText().trim();

            if (!name.isEmpty() && !quantity.isEmpty() && !expiryDate.isEmpty() && !calorieStr.isEmpty() && !imagePath[0].isEmpty()) {
                int calories = Integer.parseInt(calorieStr);
                Ingredient newIngredient = new Ingredient(name, quantity, expiryDate, calories, imagePath[0]);
                data.add(newIngredient);
                saveIngredients();

                // Return to the Smart Kitchen Inventory screen after adding the ingredient
                showMainInventoryScreen(primaryStage);
            }
        });

        // Back button to go back to the main inventory screen
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        backButton.setOnAction(e -> {
            // Return to the main inventory screen
            showMainInventoryScreen(primaryStage);
        });

        // Add all buttons and fields to the layout
        formLayout.getChildren().addAll(nameField, quantityField, expiryField, calorieField, imageButton, addIngredientButton, backButton);

        // Create a scene for the add ingredient form
        Scene formScene = new Scene(formLayout, 400, 300);
        primaryStage.setScene(formScene);
    }

    // Show the main inventory screen with the table of ingredients
    private void showMainInventoryScreen(Stage primaryStage) {
        // Create the table view for ingredients
        TableView<Ingredient> table = new TableView<>();
        table.setStyle("-fx-background-color: #ffffff; -fx-border-color: #E91E63;");

        TableColumn<Ingredient, ImageView> imageCol = new TableColumn<>("Image");
        imageCol.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getImage()));

        TableColumn<Ingredient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Ingredient, String> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Ingredient, String> expiryCol = new TableColumn<>("Expiry Date");
        expiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));

        TableColumn<Ingredient, Integer> calorieCol = new TableColumn<>("Calories");
        calorieCol.setCellValueFactory(new PropertyValueFactory<>("calories"));

        table.getColumns().addAll(imageCol, nameCol, quantityCol, expiryCol, calorieCol);
        table.setItems(data);

        // Buttons for adding, using, and deleting ingredients
        Button addButton = new Button("➕ Add Ingredient");
        addButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white;");
        addButton.setOnAction(e -> showAddIngredientForm(primaryStage));

        Button useButton = new Button("🍳 Use Ingredient");
        useButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        useButton.setOnAction(e -> {
            Ingredient selectedIngredient = table.getSelectionModel().getSelectedItem();

            if (selectedIngredient != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Use Ingredient");
                dialog.setHeaderText("How much of " + selectedIngredient.getName() + " would you like to use?");
                dialog.setContentText("Enter the quantity:");

                dialog.showAndWait().ifPresent(input -> {
                    try {
                        int amountToUse = Integer.parseInt(input);
                        String currentQuantityStr = selectedIngredient.getQuantity();
                        String unit = currentQuantityStr.replaceAll("[^a-zA-Z]", "");
                        int currentQuantity = Integer.parseInt(currentQuantityStr.replaceAll("[^0-9]", ""));

                        if (amountToUse > currentQuantity) {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "You cannot use more than the available quantity!", ButtonType.OK);
                            alert.show();
                        } else if (amountToUse > 0) {
                            int newQuantity = currentQuantity - amountToUse;
                            selectedIngredient.setQuantity(newQuantity + unit);
                            table.refresh();
                            saveIngredients();

                            // Add the used ingredient to UsedIngredientApp (Persistent storage)
                            usedIngredientApp.addUsedIngredient(new UsedIngredientApp.UsedIngredient(selectedIngredient.getImagePath(), selectedIngredient.getName(), input));

                            // Now you can also save the new state of used ingredients to the used ingredients file
                            usedIngredientApp.saveUsedData();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid amount to use.", ButtonType.OK);
                            alert.show();
                        }

                    } catch (NumberFormatException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid quantity entered. Please enter a valid number.", ButtonType.OK);
                        alert.show();
                    }
                });
            }
        });

        Button deleteButton = new Button("🗑 Delete Ingredient");
        deleteButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Ingredient selectedIngredient = table.getSelectionModel().getSelectedItem();

            if (selectedIngredient != null) {
                data.remove(selectedIngredient);
                table.refresh();
                saveIngredients();
            }
        });

        HBox buttonBox = new HBox(10, addButton, useButton, deleteButton);
        buttonBox.setPadding(new Insets(10));

        VBox vbox = new VBox(10, titleLabel, table, buttonBox);
        vbox.setPadding(new Insets(15));
        vbox.setStyle("-fx-background-color: #FCE4EC;");

        Scene scene = new Scene(vbox, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Kitchen Inventory");
        primaryStage.show();
    }

    private void saveIngredients() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ingredients.txt"))) {
            for (Ingredient ingredient : data) {
                writer.write(ingredient.getName() + "," + ingredient.getQuantity() + "," + ingredient.getExpiryDate() + "," + ingredient.getCalories() + "," + ingredient.getImagePath());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void loadIngredients() {
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

                    Ingredient ingredient = new Ingredient(name, quantity, expiryDate, calories, imagePath);
                    data.add(ingredient);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
