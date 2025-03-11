package org.example;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OnlineShopping extends Application {

    private ObservableList<String> platformList = FXCollections.observableArrayList();
    private ObservableList<inventoryApp.Ingredient> ingredientList = FXCollections.observableArrayList(); // Initialize as an empty list

    // Flags to simulate whether the apps are installed or not
    private boolean isFoodpandaInstalled = false;
    private boolean isUberEatsInstalled = false;
    private boolean isBigBasketInstalled = false;
    private boolean isMeatBoxInstalled = false;
    private boolean isGrocerInstalled = false;

    public OnlineShopping() {
        // Add shopping platforms to the list (app names only)
        platformList.addAll("Foodpanda", "UberEats", "Grocer", "MeatBox", "BigBasket");
    }

    @Override
    public void start(Stage primaryStage) {
        showShoppingPlatforms(primaryStage); // Show the shopping platforms immediately
    }

    // Show List of Shopping Platforms immediately when the app starts
    public void showShoppingPlatforms(Stage primaryStage) {
        // ListView to show online platforms as "playlist"
        ListView<String> listView = new ListView<>(platformList);

        // Style the ListView to make it look like a playlist
        listView.setStyle("-fx-font-size: 18px; -fx-selection-bar: #4CAF50; -fx-background-color: #FCE4EC;");

        // When the user clicks on an item, show platform options
        listView.setOnMouseClicked(event -> {
            if (listView.getSelectionModel().getSelectedItem() != null) {
                String selectedPlatform = listView.getSelectionModel().getSelectedItem();
                if (selectedPlatform.equals("Foodpanda")) {
                    showPlatformOptions(primaryStage, "Foodpanda", isFoodpandaInstalled);
                } else if (selectedPlatform.equals("UberEats")) {
                    showPlatformOptions(primaryStage, "UberEats", isUberEatsInstalled);
                } else if (selectedPlatform.equals("Grocer")) {
                    showPlatformOptions(primaryStage, "Grocer", isGrocerInstalled);
                } else if (selectedPlatform.equals("MeatBox")) {
                    showPlatformOptions(primaryStage, "MeatBox", isMeatBoxInstalled);
                } else if (selectedPlatform.equals("BigBasket")) {
                    showPlatformOptions(primaryStage, "BigBasket", isBigBasketInstalled);
                }
            }
        });

        // Layout for shopping platform selection
        VBox layout = new VBox(10, new Label("Choose a platform to place an order:"), listView);
        layout.setStyle("-fx-padding: 20px;");
        Scene shoppingScene = new Scene(layout, 350, 400);

        // Set the scene for the shopping platform selection
        primaryStage.setScene(shoppingScene);
        primaryStage.setTitle("Choose Shopping Platform");
        primaryStage.show();
    }

    // Display options if the user selects any platform
    private void showPlatformOptions(Stage primaryStage, String platformName, boolean isInstalled) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-padding: 20px;");

        // Add a Back button to return to the platform list
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        backButton.setOnAction(e -> showShoppingPlatforms(primaryStage));

        // Modify the button text based on app installation status
        Button platformButton = new Button("Shopping with " + platformName);
        platformButton.setStyle("-fx-font-size: 16px; -fx-background-color: #FF1744; -fx-text-fill: white;");
        platformButton.setOnAction(e -> {
            if (isInstalled) {
                openApp(platformName); // Open the app if installed
            } else {
                downloadApp(platformName); // Redirect to download if not installed
            }
        });

        layout.getChildren().addAll(backButton, platformButton);

        // Scene for platform-specific options
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
    }

    // Simulate download by opening the app store search URL
    private void downloadApp(String platformName) {
        String url = "";
        switch (platformName) {
            case "Foodpanda":
                url = "https://play.google.com/store/search?q=foodpanda&c=apps&hl=en"; // Google Play Store search URL for Foodpanda
                break;
            case "UberEats":
                url = "https://play.google.com/store/search?q=ubereats&c=apps&hl=en"; // Google Play Store search URL for UberEats
                break;
            case "Grocer":
                url = "https://play.google.com/store/search?q=grocer&c=apps&hl=en"; // Google Play Store search URL for Grocer
                break;
            case "MeatBox":
                url = "https://play.google.com/store/search?q=meatbox&c=apps&hl=en"; // Google Play Store search URL for MeatBox
                break;
            case "BigBasket":
                url = "https://play.google.com/store/search?q=bigbasket&c=apps&hl=en"; // Google Play Store search URL for BigBasket
                break;
        }
        openAppStore(url);  // Redirect to the Play Store search page
    }

    // Open the app store search URL
    private void openAppStore(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url)); // Open in the default browser
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    // Open the app if it is installed (simulating this)
    private void openApp(String platformName) {
        // Simulate opening the app
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Opening " + platformName);
        alert.setHeaderText("Opening the " + platformName + " app");
        alert.setContentText("You will now be taken directly to " + platformName + " app.");
        alert.showAndWait();

        // This is where the code would trigger the opening of the app (for Android, not JavaFX desktop).
        // For simulation purposes, we show a message and simulate a redirect.
        String url = "";
        switch (platformName) {
            case "Foodpanda":
                url = "foodpanda://"; // Simulating opening the app directly (this would be platform-specific in mobile apps).
                break;
            case "UberEats":
                url = "ubereats://";
                break;
            // Add other platforms as needed.
        }
        openAppStore(url);  // Open the app or store URL
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application immediately
    }
}
