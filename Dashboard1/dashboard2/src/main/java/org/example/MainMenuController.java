package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<String> symptomTable;
    @FXML
    private Button nextButton;

    private String selectedSymptom = "";

    // Method to handle the search field action
    @FXML
    private void onSearchSymptoms(ActionEvent event) {
        String query = searchField.getText().trim();
        // filter and update the table with symptoms based on query
        filterSymptoms(query);
    }

    // Filter symptoms based on search query
    private void filterSymptoms(String query) {
        // Implement your filtering logic based on query
    }

    // Method to handle when a symptom is selected from the table
    @FXML
    private void onSymptomSelected(ActionEvent event) {
        // Enable the next button when a symptom is selected
        selectedSymptom = symptomTable.getSelectionModel().getSelectedItem();
        nextButton.setDisable(false);  // Enable next button once a symptom is selected
    }

    // Method to handle next button click
    @FXML
    private void onNextButtonClick(ActionEvent event) {
        // Show food suggestions page with the selected symptom
    }

    // Method to show main menu with buttons (you can update this to handle button clicks and update the UI)
    public void showMainMenu(Stage primaryStage) {
        // Example: you can update the UI or show a welcome screen
    }
}
