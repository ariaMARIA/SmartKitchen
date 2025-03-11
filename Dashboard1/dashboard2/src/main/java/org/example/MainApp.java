package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class MainApp extends Application {

    private Button nextButton;
    private Button backButton;
    private Recipe selectedRecipe;
    private TextField weightField;
    private TextField heightFeetField;
    private TextField heightInchesField;
    private Text bmiResult;
    private String selectedSymptom = "";
    private static final String FILE_NAME = "symptoms_data.ser";


    private List<String> symptomsList = new ArrayList<>();  // Declare and initialize symptomsList
    private ObservableList<String> symptomObservableList = FXCollections.observableArrayList();
    private Map<String, List<String>> symptomsMap = new HashMap<>(); // for storing symptom with food suggestions

    // Method to filter symptoms based on the search query
    private List<String> filterSymptoms(String query) {
        List<String> filteredSymptoms = new ArrayList<>();
        for (String symptom : symptomsList) {
            if (symptom.toLowerCase().contains(query.toLowerCase())) {
                filteredSymptoms.add(symptom);
            }
        }
        return filteredSymptoms;
    }

    private void selectSymptom(String symptom) {
        selectedSymptom = symptom;
        nextButton.setDisable(false);  // Enable the next button once a symptom is selected
    }

    // Save symptoms and food suggestions to a file
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(symptomsMap);  // Serialize the symptomsMap object
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load symptoms and food suggestions from a file
    private void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            symptomsMap = (Map<String, List<String>>) ois.readObject();  // Deserialize the object
            if (symptomsMap == null) {
                symptomsMap = new HashMap<>();  // Initialize if the file is empty
            }
            symptomsList.addAll(symptomsMap.keySet());
            symptomObservableList.setAll(symptomsList);  // Update the observable list with symptoms
        } catch (IOException | ClassNotFoundException e) {
            symptomsMap = new HashMap<>();  // Initialize if file doesn't exist
        }
    }

    // Helper method to create a recipe card
    private VBox createRecipeCard(String recipeName) {
        VBox recipeCard = new VBox(10);
        recipeCard.setPadding(new Insets(15));
        recipeCard.setStyle("-fx-background-color: white; -fx-border-radius: 10px; -fx-border-color: #64B5F6; -fx-border-width: 2px;");
        recipeCard.setPrefWidth(500);

        Text recipeText = new Text(recipeName);
        recipeText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1976D2;");
        recipeCard.getChildren().add(recipeText);

        return recipeCard;
    }

    // Recipe class
    public static class Recipe {
        private final String name;
        private final int calories;
        private final String healthBenefits;
        private final String healthRating;
        private final String nutritionalContent;

        public Recipe(String name, int calories, String healthBenefits, String healthRating, String nutritionalContent) {
            this.name = name;
            this.calories = calories;
            this.healthBenefits = healthBenefits;
            this.healthRating = healthRating;
            this.nutritionalContent = nutritionalContent;
        }

        public String getName() {
            return name;
        }

        public int getCalories() {
            return calories;
        }

        public String getHealthBenefits() {
            return healthBenefits;
        }

        public String getHealthRating() {
            return healthRating;
        }

        public String getNutritionalContent() {
            return nutritionalContent;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        loadData();
        showMainMenu(primaryStage);
    }

    private void showMainMenu(Stage primaryStage) {
        // Title Label
        Text titleLabel = new Text("🩺 Smart Kitchen Inventory");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: #6A1B9A;");

        // Load the image for the background
        ImageView backgroundImageView = new ImageView(getClass().getResource("/ab.jpg").toExternalForm());

        backgroundImageView.setFitHeight(700);  // Set the height for the background image
        backgroundImageView.setFitWidth(1200);  // Set the width for the background image
        backgroundImageView.setPreserveRatio(false);  // Preserve aspect ratio

        // All Buttons
        Button ingredientsButton = new Button("Add Ingredients");
        Button bmiButton = new Button("BMI Calculation");
        Button Recipe = new Button("Recipe Suggestion");
        Button UseIngredients = new Button("Use Ingredients");
        Button symptomButton = new Button("Your Symptoms");
        Button recipeButton = new Button("Recipes Details");

        // Style for the buttons
        bmiButton.setStyle("-fx-background-color:  #FFFFFF; -fx-text-fill: black; -fx-font-size: 18px; -fx-padding: 10px; -fx-min-width: 200px;");
        recipeButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black; -fx-font-size: 18px; -fx-padding: 10px; -fx-min-width: 200px");
        ingredientsButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black; -fx-font-size: 18px; -fx-padding: 10px; -fx-min-width: 200px;");
        UseIngredients.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill:black; -fx-font-size: 18px; -fx-padding: 10px; -fx-min-width: 200px");
        Recipe.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill:black; -fx-font-size: 18px; -fx-padding: 10px; -fx-min-width: 200px");
        symptomButton.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: black; -fx-font-size: 18px; -fx-padding: 10px; -fx-min-width: 200px");

        // Button Actions
        bmiButton.setOnAction(event -> showBMICalculationPage(primaryStage));
        symptomButton.setOnAction(event -> showSymptomInputPage(primaryStage));
        recipeButton.setOnAction(event -> showRecipeSelectionPage(primaryStage));


        // Layout for buttons
        VBox buttonLayout = new VBox(20, titleLabel, bmiButton, symptomButton, recipeButton);
        buttonLayout.setAlignment(Pos.CENTER);

        // Transparent overlay to give a better contrast for buttons (optional)
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6);");  // Light transparent white

        // Add the background image and overlay into a stack pane
        StackPane mainLayout = new StackPane();
        mainLayout.getChildren().addAll(backgroundImageView, overlay, buttonLayout);  // Stack them

        // Scene setup
        Scene scene = new Scene(mainLayout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Kitchen Inventory - Main Menu");
        primaryStage.show();
    }

    // First page for BMI Calculation
    private void showBMICalculationPage(Stage primaryStage) {
        Text titleLabel = new Text("🩺 BMI Calculation");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #6A1B9A;");

        // BMI Inputs
        Label weightLabel = new Label("Enter your weight (kg):");
        weightField = new TextField();

        Label heightFeetLabel = new Label("Enter your height (feet):");
        heightFeetField = new TextField();

        Label heightInchesLabel = new Label("Enter your height (inches):");
        heightInchesField = new TextField();

        bmiResult = new Text();

        // Calculate BMI Button
        Button calculateBmiButton = new Button("Calculate BMI");
        calculateBmiButton.setStyle("-fx-background-color: #6A1B9A; -fx-text-fill: white;");
        calculateBmiButton.setOnAction(event -> calculateBMI());

        // Next Button (Disable until BMI is calculated)
        nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        nextButton.setDisable(true);
        nextButton.setOnAction(event -> showBMIResultPage(primaryStage));

        // Back Button
        backButton = new Button("🔙Back to Main");
        backButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        backButton.setOnAction(event -> showMainMenu(primaryStage));

        // Buttons Layout
        HBox buttonLayout = new HBox(10, backButton, nextButton);
        buttonLayout.setPadding(new Insets(10));

        // Main Layout
        VBox mainLayout = new VBox(10, titleLabel, weightLabel, weightField, heightFeetLabel, heightFeetField, heightInchesLabel, heightInchesField, calculateBmiButton, buttonLayout);
        mainLayout.setStyle("-fx-background-color: #FFDDEB  ; -fx-border-radius: 10px;");// #E3F2FD
        mainLayout.setPadding(new Insets(15));

        Scene scene = new Scene(mainLayout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("BMI Calculation");
        primaryStage.show();
    }

    // BMI Calculation
    private void calculateBMI() {
        try {
            double weight = Double.parseDouble(weightField.getText());
            int feet = Integer.parseInt(heightFeetField.getText());
            int inches = Integer.parseInt(heightInchesField.getText());

            double heightInMeters = (feet * 0.3048) + (inches * 0.0254);

            if (heightInMeters > 0) {
                double bmi = weight / (heightInMeters * heightInMeters);
                nextButton.setDisable(false);  // Enable next button after calculation
            } else {
                bmiResult.setText("Please enter a valid height.");
            }
        } catch (NumberFormatException e) {
            bmiResult.setText("Please enter valid weight and height.");
        }
    }

    // Health Rating Method
    private String getHealthRating(double bmi) {
        if (bmi < 18.5) return "Underweight (Need to gain more Weight)";
        else if (bmi >= 18.5 && bmi < 24.9) return "Normal (just maintain it)";
        else if (bmi >= 25 && bmi < 29.9) return "Overweight(Need to reduce some Weight)";
        else return "Obese(very bad & Health Risky)";
    }

    // Health Rating Method
    private String getHealthRecommend(double bmi) {
        if (bmi < 18.5) return "2,000 – 2,500 kcal(Sedentary ) \n                                              2300-2800kcal(Moderate Activity) \n                                              2500-3200kcal(Active Lifestyle)";
        else if (bmi >= 18.5 && bmi < 24.9) return "1,800 – 2,200 kcal(sedentary)\n                                             2,000 – 2,500 kcal(Moderate Activity)\n                                           2,300 – 2,800 kcal(Active Lifestyle)";
        else if (bmi >= 25 && bmi < 29.9) return "1,500 – 1,800 kcal(sedentary)\n                                           1,700 – 2,200kcal(Moderate Activity)\n                                             2,000 – 2,500 kcal(Active Lifestyle)";
        else return "1,200 – 1,600 kcal(Sedentary )\n                                             1,500 – 2,000 kcal(Moderate Activity)\n                                              1,800 – 2,300 kcal(Active Lifestyle)";
    }

    // Second Page to Show the BMI Result
    private void showBMIResultPage(Stage primaryStage) {
        double weight = Double.parseDouble(weightField.getText());
        int feet = Integer.parseInt(heightFeetField.getText());
        int inches = Integer.parseInt(heightInchesField.getText());

        double heightInMeters = (feet * 0.3048) + (inches * 0.0254);
        double bmi = weight / (heightInMeters * heightInMeters);

        Text titleLabel = new Text("BMI Result");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #6A1B9A;");

        Text resultLabel = new Text("Your BMI: " + String.format("%.2f", bmi) + "\nHealth Rating: " + getHealthRating(bmi) +"\nRecommended Daily Calorie Intake:" +getHealthRecommend(bmi));
        resultLabel.setStyle("-fx-font-size: 24px; -fx-fill: #4CAF50;");

        // Back Button (Go back to BMI Calculation)
        Button backToCalculationButton = new Button("🔙Back to Calculation");
        backToCalculationButton.setStyle("-fx-background-color: #FF4081; -fx-text-fill: white;");
        backToCalculationButton.setOnAction(event -> showBMICalculationPage(primaryStage));

        // Layout
        VBox resultPageLayout = new VBox(20, titleLabel, resultLabel, backToCalculationButton);
        resultPageLayout.setStyle("-fx-background-color:#FFDDEB;");//E3F2FD
        resultPageLayout.setAlignment(Pos.CENTER);

        Scene resultScene = new Scene(resultPageLayout, 700, 600);
        primaryStage.setScene(resultScene);
        primaryStage.setTitle("BMI Result");
        primaryStage.show();
    }


    private void showSymptomInputPage(Stage primaryStage) {
        // Title Label
        Text titleLabel = new Text("🩺 Your Symptoms");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: #9C27B0;");

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search Symptoms...");
        searchField.setStyle("-fx-font-size: 18px; -fx-padding: 10px; -fx-background-color: white; -fx-text-fill: black;");

        // New Symptom Add Button (Initially Hidden)
        Button addSymptomButton = new Button("Add New Symptom");
        addSymptomButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        addSymptomButton.setOnAction(e -> showAddNewSymptomPage(primaryStage));
        addSymptomButton.setVisible(false);

        // Table to display Symptoms
        TableView<String> symptomTable = new TableView<>();
        symptomTable.setItems(symptomObservableList);
        TableColumn<String, String> symptomColumn = new TableColumn<>("Symptoms");
        symptomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        symptomTable.getColumns().add(symptomColumn);

        // Show food suggestions after selecting a symptom
        nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        nextButton.setDisable(true);  // Disable initially
        nextButton.setOnAction(e -> showFoodSuggestionsPage(primaryStage, selectedSymptom));

        // Filter symptoms based on search query
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                symptomObservableList.setAll(symptomsList);
                addSymptomButton.setVisible(false);
            } else {
                symptomObservableList.setAll(filterSymptoms(newValue));
                addSymptomButton.setVisible(true);  // Show the add button if search returns no result
            }
        });

        // Enable the Next Button when a symptom is selected
        symptomTable.setOnMouseClicked(event -> {
            selectedSymptom = symptomTable.getSelectionModel().getSelectedItem();
            nextButton.setDisable(false);
        });

        // Back Button
        Button backButton = new Button("🔙Back to Main");
        backButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        backButton.setOnAction(event -> showMainMenu(primaryStage));

        // Symptoms layout (buttons placed in separate rows with more space)
        VBox symptomButtonLayout = new VBox(20, symptomTable, addSymptomButton, nextButton, backButton);
        symptomButtonLayout.setAlignment(Pos.CENTER);

        // Create a ScrollPane for symptoms to make it scrollable
        ScrollPane scrollPane = new ScrollPane(symptomButtonLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");

        // Main layout
        VBox mainLayout = new VBox(20, titleLabel, searchField, scrollPane);
        mainLayout.setStyle("-fx-background-color: #FFDDEB;");
        mainLayout.setPadding(new Insets(20));

        Scene scene = new Scene(mainLayout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Select Symptom");
        primaryStage.show();
    }

    private void showAddNewSymptomPage(Stage primaryStage) {
        Text titleLabel = new Text("🩺 Add New Symptom");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-fill: #9C27B0;");

        TextField newSymptomField = new TextField();
        newSymptomField.setPromptText("Enter new symptom...");
        newSymptomField.setStyle("-fx-font-size: 18px; -fx-padding: 10px; -fx-background-color: white; -fx-text-fill: black;");

        Button addSymptomButton = new Button("Add Symptom");
        addSymptomButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        addSymptomButton.setOnAction(e -> {
            String newSymptom = newSymptomField.getText().trim();
            if (!newSymptom.isEmpty() && !symptomsList.contains(newSymptom)) {
                addSymptom(newSymptom);
                showSymptomInputPage(primaryStage);  // Return to the symptom selection page
            }
        });

        Button backButton = new Button("🔙Back to Selection");
        backButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        backButton.setOnAction(event -> showSymptomInputPage(primaryStage));

        VBox mainLayout = new VBox(20, titleLabel, newSymptomField, addSymptomButton, backButton);
        mainLayout.setStyle("-fx-background-color: #FFDDEB;");
        mainLayout.setPadding(new Insets(20));

        Scene scene = new Scene(mainLayout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Add New Symptom");
        primaryStage.show();
    }

    private void addSymptom(String newSymptom) {
        if (!symptomsMap.containsKey(newSymptom)) {
            symptomsMap.put(newSymptom, new ArrayList<>()); // Initialize an empty list for food suggestions
            symptomsList.add(newSymptom);
            symptomObservableList.setAll(symptomsList); // Update the observable list
            saveData(); // Save the data persistently
        }
    }

    private void showFoodSuggestionsPage(Stage primaryStage, String symptom) {
        Text titleLabel = new Text("Food Suggestions for " + symptom);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #9C27B0;");

        VBox recipeCards = new VBox(15);
        recipeCards.setPadding(new Insets(20));

        if (symptomsMap.containsKey(symptom)) {
            List<String> foodSuggestions = symptomsMap.get(symptom);
            for (String food : foodSuggestions) {
                recipeCards.getChildren().add(createRecipeCard(food));
            }
        }

        TextField newFoodField = new TextField();
        newFoodField.setPromptText("Enter new food suggestion...");
        Button addFoodButton = new Button("Add Food Suggestion");
        addFoodButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        addFoodButton.setOnAction(e -> {
            String newFood = newFoodField.getText().trim();
            if (!newFood.isEmpty()) {
                addFoodSuggestion(symptom, newFood);
                newFoodField.clear(); // Clear input field
                showFoodSuggestionsPage(primaryStage, symptom); // Refresh the food suggestions page
            }
        });

        ScrollPane scrollPane = new ScrollPane(recipeCards);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Button backButton = new Button("🔙Back to Selection");
        backButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        backButton.setOnAction(event -> showSymptomInputPage(primaryStage));

        VBox mainLayout = new VBox(20, titleLabel, scrollPane, newFoodField, addFoodButton, backButton);
        mainLayout.setStyle("-fx-background-color:#FFDDEB;");
        mainLayout.setPadding(new Insets(15));
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Food Suggestions");
        primaryStage.show();
    }

    private void addFoodSuggestion(String symptom, String newFoodSuggestion) {
        if (symptomsMap.containsKey(symptom)) {
            symptomsMap.get(symptom).add(newFoodSuggestion);
            saveData(); // Save the updated food suggestions
        }
    }


    //receipe selection
    private void showRecipeSelectionPage(Stage primaryStage) {
        // Recipe Data
        Recipe[] recipes = {

                // *Vegetarian & Vegan Dishes*
                new Recipe("🥗 Greek Salad", 120, "Rich in vitamins and antioxidants", "Excellent", "High in Fiber, Low in Carbs"),
                new Recipe("🥑 Avocado Toast", 200, "Contains healthy fats & fiber", "Good", "High in Healthy Fats, Rich in Fiber"),
                new Recipe("🥕 Carrot Sticks", 50, "Rich in Vitamin A", "Excellent", "High in Vitamin A, Low in Calories"),
                new Recipe("🍆 Eggplant Parmesan", 300, "Rich in antioxidants", "Moderate", "High in Carbs, Medium in Fat"),
                new Recipe("🌽 Corn on the Cob", 120, "Good source of fiber", "Good", "High in Fiber, Low in Fat"),
                new Recipe("🥦 Broccoli Stir-Fry", 150, "High in fiber and vitamins", "Good", "High in Fiber, Low in Carbs"),
                new Recipe("🥒 Cucumber Salad", 100, "Cooling and hydrating", "Excellent", "High in Water, Low in Calories"),
                new Recipe("🍠 Roasted Sweet Potatoes", 160, "Rich in beta-carotene", "Excellent", "High in Fiber, Medium in Carbs"),
                new Recipe("🍄 Stuffed Mushrooms", 200, "Delicious and filling", "Good", "High in Fiber, Low in Calories"),
                new Recipe("🌱 Tofu Stir-Fry", 250, "Great protein source for vegetarians", "Good", "High in Protein, Low in Fat"),
                new Recipe("🥬 Saag Paneer", 300, "Indian spinach and cheese dish", "Good", "High in Calcium, Medium in Fat"),
                new Recipe("🥦 Kale Chips", 80, "Crunchy and nutritious", "Good", "High in Fiber, Low in Calories"),

                // *Meat & Poultry Dishes*
                new Recipe("🍔 Cheeseburger", 550, "Classic fast food", "Moderate", "High in Protein, High in Fat"),
                new Recipe("🌮 Chicken Tacos", 300, "Mexican favorite", "Good", "High in Protein, Medium in Carbs"),
                new Recipe("🍖 Lamb Chops", 480, "Rich in iron and flavor", "Excellent", "High in Protein, High in Fat"),
                new Recipe("🍗 Grilled Chicken", 220, "Great source of protein", "Good", "High in Protein, Low in Fat"),
                new Recipe("🥩 Beef Steak", 500, "High in iron and protein", "Excellent", "High in Protein, High in Fat"),
                new Recipe("🍗 Turkey Meatballs", 280, "Lean and tasty", "Good", "High in Protein, Low in Fat"),
                new Recipe("🥓 Bacon Strips", 400, "High in protein and fat", "Moderate", "High in Fat, Medium in Protein"),
                new Recipe("🍖 BBQ Ribs", 600, "Flavorful and high in protein", "Moderate", "High in Protein, High in Fat"),

                // *Seafood Dishes*
                new Recipe("🍣 Salmon Sushi", 300, "Japanese delicacy", "Excellent", "High in Omega-3, Medium in Carbs"),
                new Recipe("🦐 Garlic Butter Shrimp", 320, "Flavorful and healthy", "Good", "High in Protein, High in Healthy Fats"),
                new Recipe("🐟 Grilled Tuna Steak", 280, "Great source of lean protein", "Excellent", "High in Protein, Low in Fat"),
                new Recipe("🍲 Seafood Chowder", 450, "Creamy and hearty", "Moderate", "High in Protein, High in Fat"),
                new Recipe("🐙 Octopus Salad", 250, "Unique seafood dish", "Good", "High in Protein, Low in Fat"),

                // *Asian Dishes*
                new Recipe("🍜 Ramen", 450, "Japanese noodle soup", "Moderate", "High in Carbs, Medium in Protein"),
                new Recipe("🥡 Fried Rice", 400, "Classic Asian dish", "Good", "High in Carbs, Medium in Protein"),
                new Recipe("🥟 Dumplings", 350, "Delicious and filling", "Moderate", "High in Carbs, Medium in Protein"),
                new Recipe("🍢 Teriyaki Chicken", 320, "Sweet and savory", "Good", "High in Protein, Medium in Sugar"),
                new Recipe("🍛 Thai Green Curry", 400, "Spicy and flavorful", "Good", "High in Healthy Fats, Medium in Carbs"),
                new Recipe("🍱 Bento Box", 500, "Variety of flavors", "Good", "High in Protein, Low in Carbs"),
                new Recipe("🍣 Sushi", 250, "Japanese raw fish dish", "Excellent", "High in Omega-3, Low in Carbs"),

                // *Vegetables & Side Dishes*
                new Recipe("🥗 Caesar Salad", 250, "Classic side dish", "Good", "High in Healthy Fats, Low in Carbs"),
                new Recipe("🥑 Guacamole", 200, "Creamy and healthy", "Excellent", "High in Healthy Fats, Low in Carbs"),
                new Recipe("🥕 Roasted Brussels Sprouts", 120, "Crunchy and tasty", "Good", "High in Fiber, Low in Calories"),
                new Recipe("🍆 Grilled Eggplant", 150, "Smoky and nutritious", "Good", "High in Fiber, Low in Calories"),
                new Recipe("🍇 Grapes", 90, "Rich in antioxidants", "Good", "High in Antioxidants, Medium in Sugar"),
                new Recipe("🍋 Lemon Juice", 5, "Refreshing and tangy", "Excellent", "Zero Calories, Vitamin C Rich"),

                // *Fruits & Fresh Items*
                new Recipe("🍑 Peaches", 70, "Sweet and juicy", "Excellent", "High in Fiber, Low in Calories"),
                new Recipe("🍊 Oranges", 80, "Packed with Vitamin C", "Excellent", "High in Vitamin C, Low in Calories"),
                new Recipe("🍇 Grapes", 90, "Rich in antioxidants", "Good", "High in Antioxidants, Medium in Sugar"),
                new Recipe("🍎 Apples", 95, "Good source of fiber", "Excellent", "High in Fiber, Low in Calories"),
                new Recipe("🥝 Kiwi", 60, "Nutrient-dense fruit", "Excellent", "High in Fiber, Low in Calories"),
                new Recipe("🍍 Pineapple", 85, "Contains digestive enzymes", "Good", "High in Vitamin C, Low in Calories"),
                new Recipe("🍓 Strawberries", 50, "Rich in antioxidants", "Excellent", "High in Antioxidants, Low in Calories"),
                new Recipe("🍌 Banana", 105, "Great for energy", "Good", "High in Potassium, Medium in Carbs"),
                new Recipe("🍉 Watermelon", 50, "Refreshing summer fruit", "Excellent", "High in Water, Low in Calories"),
                new Recipe("🥭 Mango", 150, "Sweet and nutritious", "Good", "High in Vitamin C, Medium in Sugar"),

                // *Drinks & Beverages*
                new Recipe("🍵 Chamomile Tea", 0, "Soothing and relaxing", "Excellent", "Zero Calories, Herbal Benefits"),
                new Recipe("🥛 Coconut Water", 50, "Natural electrolyte drink", "Good", "High in Potassium, Low in Calories"),
                new Recipe("🍊 Orange Juice", 110, "High in Vitamin C", "Good", "High in Vitamin C, Medium in Sugar"),
                new Recipe("🥤 Iced Tea", 80, "Refreshing and cool", "Good", "Medium in Sugar, Low in Calories"),
                new Recipe("🍷 Red Wine", 125, "Contains antioxidants", "Moderate", "Medium in Alcohol, High in Antioxidants"),
                new Recipe("🍹 Mango Lassi", 180, "Rich in probiotics", "Good", "High in Probiotics, Medium in Sugar"),
                new Recipe("🧃 Apple Juice", 120, "Good for digestion", "Good", "High in Vitamin C, Medium in Sugar"),
                new Recipe("🥤 Lemonade", 120, "Good for hydration", "Good", "High in Vitamin C, Medium in Sugar"),
                new Recipe("🥛 Almond Milk", 80, "Dairy-free alternative", "Good", "High in Vitamin E, Low in Calories"),
                new Recipe("🍶 Soy Milk", 70, "Plant-based milk", "Good", "High in Protein, Low in Sugar"),

                // *Desserts & Sweets*
                new Recipe("🎂 Cheesecake", 350, "Rich and creamy", "Moderate", "High in Sugar, High in Fat"),
                new Recipe("🍮 Crème Brûlée", 300, "Classic French dessert", "Good", "High in Sugar, High in Fat"),
                new Recipe("🍯 Honey Toast", 400, "Sweet and buttery", "Moderate", "High in Sugar, High in Carbs"),
                new Recipe("🍧 Mango Sorbet", 150, "Fruity and refreshing", "Good", "High in Vitamin C, Low in Fat"),
                new Recipe("🥜 Peanut Butter Cookies", 280, "Nutty and delicious", "Moderate", "High in Protein, High in Fat"),
                new Recipe("🍩 Donut", 350, "High in sugar and fat", "Bad", "High in Sugar, High in Fat"),
                new Recipe("🍪 Chocolate Chip Cookies", 280, "Sweet snack", "Moderate", "High in Sugar, High in Fat"),
                new Recipe("🍫 Dark Chocolate", 180, "Contains antioxidants", "Good", "High in Antioxidants, High in Fat"),
                new Recipe("🍦 Vanilla Ice Cream", 250, "Classic sweet treat", "Moderate", "High in Sugar, High in Fat"),
                new Recipe("🍪 Brownies", 300, "Fudgy and delicious", "Moderate", "High in Sugar, High in Fat"),
                new Recipe("🍧 Frozen Yogurt", 150, "Refreshing dessert", "Good", "High in Protein, Low in Fat"),

                // *Bengali Dishes*
                new Recipe("🍛 Shorshe Ilish", 350, "Hilsa fish in mustard gravy", "Excellent", "High in Omega-3, Rich in Flavor"),
                new Recipe("🍚 Bhuna Khichuri", 280, "Bengali lentil and rice dish", "Good", "High in Protein, Medium in Carbs"),
                new Recipe("🥘 Chingri Malai Curry", 380, "Prawns cooked in coconut milk", "Excellent", "High in Protein, High in Healthy Fats"),
                new Recipe("🍛 Begun Bharta", 150, "Mashed eggplant with mustard oil", "Good", "High in Fiber, Low in Calories"),
                new Recipe("🍲 Aloo Posto", 220, "Potato with poppy seed paste", "Good", "High in Carbs, Medium in Healthy Fats"),
                new Recipe("🍛 Shorshe Bata Maach", 400, "Mustard sauce fish curry", "Excellent", "High in Protein, Rich in Omega-3"),
                new Recipe("🍚 Macher Jhol", 300, "Fish curry with spices", "Good", "High in Protein, Low in Carbs"),
                new Recipe("🍲 Panta Bhat", 150, "Fermented rice with mustard oil", "Good", "Low in Calories, High in Carbs"),
                new Recipe("🍛 Cholar Dal", 220, "Bengali style chickpea curry", "Good", "High in Protein, Medium in Carbs"),
                new Recipe("🍚 Aloo Bhorta", 180, "Mashed potatoes with mustard oil and spices", "Good", "High in Carbs, Medium in Fat"),

                // *Additional Bengali Dishes*
                new Recipe("🍛 Kachauli", 250, "Steamed dumplings made with flour and spices", "Moderate", "High in Carbs, Medium in Fat"),
                new Recipe("🍚 Pulao", 300, "Fragrant rice dish with spices", "Good", "High in Carbs, Low in Protein"),
                new Recipe("🍛 Korma", 400, "Mild curry with cream, spices, and meat", "Moderate", "High in Protein, High in Fat"),
                new Recipe("🍲 Dhokar Dalna", 270, "Bengali lentil cake curry", "Good", "High in Protein, Medium in Fat"),
                new Recipe("🍛 Aloo Macher Jhol", 350, "Fish curry with potatoes", "Good", "High in Protein, Medium in Carbs"),
                new Recipe("🍚 Rajma", 330, "Kidney beans curry", "Good", "High in Protein, High in Carbs"),
                new Recipe("🍛 Macher Matha Diye Dal", 200, "Lentils cooked with fish head", "Good", "High in Protein, Low in Carbs"),
                new Recipe("🍚 Chingri Bhorta", 240, "Mashed shrimp with mustard oil", "Good", "High in Protein, Medium in Fat"),
                new Recipe("🍛 Shorshe Pata", 280, "Mustard leaf cooked with fish or meat", "Good", "High in Fiber, Medium in Protein"),
                new Recipe("🍲 Paatla Bhaat", 200, "Thin rice soup with vegetables", "Good", "Low in Calories, Medium in Carbs"),

                // *More Indian Dishes*
                new Recipe("🍛 Butter Chicken", 550, "Chicken cooked in creamy tomato sauce", "Moderate", "High in Protein, High in Fat"),
                new Recipe("🍚 Paneer Tikka", 350, "Grilled paneer with spices", "Good", "High in Protein, Low in Carbs"),
                new Recipe("🍛 Dal Tadka", 250, "Yellow lentils cooked with tempering", "Good", "High in Protein, Low in Fat"),
                new Recipe("🍲 Prawn Masala", 300, "Spicy prawn curry", "Good", "High in Protein, Medium in Carbs"),
                new Recipe("🍚 Vegetable Biryani", 400, "Fragrant rice dish with mixed vegetables", "Good", "High in Carbs, Medium in Protein"),
                new Recipe("🍛 Tandoori Chicken", 450, "Chicken cooked in a clay oven with spices", "Good", "High in Protein, Low in Fat"),
                new Recipe("🍚 Pesarattu", 350, "Green gram pancakes from Andhra Pradesh", "Good", "High in Protein, Medium in Carbs"),
                new Recipe("🍛 Chana Masala", 320, "Spicy chickpea curry", "Good", "High in Protein, Medium in Carbs"),
                new Recipe("🍲 Masoor Dal", 220, "Red lentils cooked with spices", "Good", "High in Protein, Low in Carbs"),
                new Recipe("🍚 Baingan Bharta", 250, "Roasted eggplant curry", "Good", "High in Fiber, Medium in Fat"),

                // *More International Dishes*
                new Recipe("🍣 California Rolls", 150, "Sushi rolls with avocado, cucumber, and crab", "Good", "Low in Calories, Medium in Protein"),
                new Recipe("🥡 Chinese Fried Rice", 400, "Fried rice with vegetables and eggs", "Good", "High in Carbs, Medium in Protein"),
                new Recipe("🍜 Pho", 350, "Vietnamese noodle soup with herbs", "Good", "High in Carbs, Medium in Protein"),
                new Recipe("🍛 Paella", 500, "Spanish rice dish with seafood and meat", "Good", "High in Protein, High in Carbs"),
                new Recipe("🍱 Sushi Rolls", 200, "Variety of Japanese rolls with fish and rice", "Good", "Low in Calories, High in Omega-3"),
                new Recipe("🍤 Shrimp Scampi", 400, "Shrimp in garlic butter sauce with pasta", "Moderate", "High in Protein, Medium in Fat"),
                new Recipe("🍲 Risotto", 500, "Creamy rice dish from Italy", "Moderate", "High in Carbs, High in Fat"),
                new Recipe("🍗 Chicken Kiev", 450, "Chicken filled with herb butter", "Moderate", "High in Protein, High in Fat"),
                new Recipe("🍖 Roast Lamb", 600, "Oven-roasted lamb with herbs", "Good", "High in Protein, High in Fat"),
                new Recipe("🍜 Tom Yum Soup", 200, "Spicy Thai soup with shrimp", "Good", "Low in Calories, Medium in Protein"),

                // *Drinks & Beverages*
                new Recipe("🥤 Cold Brew Coffee", 10, "Smooth and strong cold coffee", "Excellent", "Zero Calories, Caffeine Rich"),
                new Recipe("🍹 Lemonade", 120, "Freshly squeezed lemon juice with sugar", "Good", "High in Vitamin C, Medium in Sugar"),
                new Recipe("🍵 Iced Matcha Latte", 120, "Iced green tea latte with matcha", "Good", "High in Antioxidants, Medium in Sugar"),
                new Recipe("🥛 Coconut Milkshake", 220, "Creamy coconut milk drink", "Moderate", "High in Healthy Fats, Medium in Sugar"),
                new Recipe("🍊 Fresh Orange Juice", 100, "Freshly squeezed orange juice", "Excellent", "High in Vitamin C, Low in Calories")

        };


        // Title
        Text recipeTitle = new Text("🍽 Select a Recipe");
        recipeTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #1565C0;");

        // *VBox to Hold Recipe List*
        VBox recipeCards = new VBox(15);
        recipeCards.setPadding(new Insets(20));

        for (Recipe recipe : recipes) {
            VBox recipeCard = new VBox(10);
            recipeCard.setPadding(new Insets(15));
            recipeCard.setStyle("-fx-background-color: white; -fx-border-radius: 10px; " +
                    "-fx-border-color: #64B5F6; -fx-border-width: 2px; " +
                    "-fx-effect: dropshadow(three-pass-box, #90CAF9, 5, 0, 2, 2);");
            recipeCard.setPrefWidth(500);

            Text recipeText = new Text(recipe.getName());
            recipeText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #1976D2;");

            recipeCard.getChildren().add(recipeText);
            recipeCards.getChildren().add(recipeCard);

            // Hover Effect
            recipeCard.setOnMouseEntered(e -> recipeCard.setStyle("-fx-background-color: #E3F2FD; -fx-border-radius: 10px; " +
                    "-fx-border-width: 2px; -fx-effect: dropshadow(three-pass-box, #42A5F5, 10, 0, 4, 4);"));
            recipeCard.setOnMouseExited(e -> recipeCard.setStyle("-fx-background-color: white; -fx-border-radius: 10px; " +
                    "-fx-border-color: #64B5F6; -fx-border-width: 2px; " +
                    "-fx-effect: dropshadow(three-pass-box, #90CAF9, 5, 0, 2, 2);"));

            // Click Selection
            recipeCard.setOnMouseClicked(event -> {
                selectedRecipe = recipe;
                nextButton.setDisable(false);
            });
        }

        // *Enable Scrolling for Recipe List*
        ScrollPane scrollPane = new ScrollPane(recipeCards);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");

        // *Next & Back Buttons*
        nextButton = new Button(" Next ");
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        nextButton.setDisable(true);
        nextButton.setOnAction(event -> {
            if (selectedRecipe != null) {
                showRecipeDetails(primaryStage);
            } else {
                System.out.println("Please select a recipe before proceeding.");
            }
        });

        Button backButton = new Button("🔙 Back to Main");
        backButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
        // backButton.setOnAction(event -> showBMICalculationPage(primaryStage));
        backButton.setOnAction(event -> showMainMenu(primaryStage));

        // *Keep Buttons Fixed at the Bottom*
        HBox buttonLayout = new HBox(10, backButton, nextButton);
        buttonLayout.setPadding(new Insets(10));
        buttonLayout.setAlignment(Pos.CENTER);

        // *Final Layout*
        VBox mainLayout = new VBox(10, recipeTitle, scrollPane, buttonLayout);
        mainLayout.setStyle("-fx-background-color: #FFDDEB; -fx-border-radius: 10px;");
        mainLayout.setPadding(new Insets(15));
        mainLayout.setAlignment(Pos.CENTER);

        // *Set Scene & Show Page*
        Scene scene = new Scene(mainLayout, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Kitchen Inventory - Recipe Selection");
        primaryStage.show();
    }


    private void showRecipeDetails(Stage primaryStage) {
        VBox recipeDetails = new VBox(12);
        recipeDetails.setPadding(new Insets(20));

        // ** Radish-Pink Gradient Background**
        recipeDetails.setStyle("-fx-background-color: linear-gradient(to bottom, #F06292, #EC407A); " +
                "-fx-border-radius: 15px; -fx-border-color: #D81B60; -fx-border-width: 3px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(255, 105, 180, 0.5), 10, 0, 5, 5);");

        if (selectedRecipe != null) {
            // *Elegant Title with Gradient Effect*
            Text recipeName = new Text("🍽 " + selectedRecipe.getName());
            recipeName.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; " +
                    "-fx-fill: #C2185B; -fx-effect: dropshadow(gaussian, #F8BBD0, 5, 0, 2, 2);");

            Text recipeCalories = new Text("🔥 Calories: " + selectedRecipe.getCalories());
            recipeCalories.setStyle("-fx-font-size: 16px; -fx-fill: #B71C1C;");

            Text recipeHealthBenefits = new Text("💪 Health Benefits: " + selectedRecipe.getHealthBenefits());
            recipeHealthBenefits.setStyle("-fx-font-size: 16px; -fx-fill: #AD1457;");

            Text recipeHealthRating = new Text("✅ Health Rating: " + selectedRecipe.getHealthRating());
            recipeHealthRating.setStyle("-fx-font-size: 16px; -fx-fill: #D81B60;");

            // *Health Recommendation*
            String healthRecommendation = getHealthRecommendation(selectedRecipe.getName());
            Text healthRecText = new Text("🩺 Recommended For: " + healthRecommendation);
            healthRecText.setStyle("-fx-font-size: 16px; -fx-fill: #004D40; -fx-font-weight: bold;");

            // *Allergy Warning - More Alerting*
            String allergyWarning = getAllergyWarning(selectedRecipe.getName());
            Text allergyText = new Text("⚠ Allergy Warning: " + allergyWarning);
            allergyText.setStyle("-fx-font-size: 16px; -fx-fill: #B71C1C; -fx-font-weight: bold;");

            Text recipeNutritionalContent = new Text("🥗 Nutritional Content: " + selectedRecipe.getNutritionalContent());
            recipeNutritionalContent.setStyle("-fx-font-size: 16px; -fx-fill: #1B5E20;");

            // *Stylish Rounded Back Button*
            Button backButton = new Button("🔙 Back to Receipe Menu");
            backButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white; " +
                    "-fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 25px; -fx-padding: 10px 20px;");
            backButton.setOnAction(event -> showRecipeSelectionPage(primaryStage));

            VBox contentLayout = new VBox(12, recipeName, recipeCalories, recipeHealthBenefits, recipeHealthRating,
                    healthRecText, allergyText, recipeNutritionalContent, backButton);
            contentLayout.setPadding(new Insets(20));

            Scene scene = new Scene(contentLayout, 500, 450);
            primaryStage.setScene(scene);
            primaryStage.setTitle(" Recipe Details ");
            primaryStage.show();
        }
    }

    // Health Recommendations Based on Recipe
    private String getHealthRecommendation(String recipeName) {
        switch (recipeName) {
            // *🥗 Vegetarian & Vegan Dishes*
            case "🥗 Greek Salad":
                return "💖 Heart Health & Antioxidant Boost";
            case "🥑 Avocado Toast":
                return "🧠 Brain Health & Healthy Fats";
            case "🥕 Carrot Sticks":
                return "👀 Eye Health & Vitamin A Boost";
            case "🍆 Eggplant Parmesan":
                return "🦠 Immunity Boost & Antioxidants";
            case "🌽 Corn on the Cob":
                return "💪 Digestive Health & High in Fiber";
            case "🥦 Broccoli Stir-Fry":
                return "🦠 Immunity Boost & Cancer Prevention";
            case "🥒 Cucumber Salad":
                return "💧 Hydration & Cooling Effect";
            case "🍠 Roasted Sweet Potatoes":
                return "💖 Heart Health & Vitamin A Rich";
            case "🍄 Stuffed Mushrooms":
                return "🦴 Bone Health & Rich in Antioxidants";
            case "🌱 Tofu Stir-Fry":
                return "🧠 Brain Health & Plant-Based Protein";
            case "🥬 Saag Paneer":
                return "🦴 Bone Health & Calcium Boost";
            case "🥦 Kale Chips":
                return "🦠 Immunity Boost & Detox Support";

            // *🍗 Meat & Poultry Dishes*
            case "🍔 Cheeseburger":
                return "⚠ High in Saturated Fat, Enjoy in Moderation";
            case "🌮 Chicken Tacos":
                return "💪 Lean Protein & Balanced Meal";
            case "🍖 Lamb Chops":
                return "🩸 Iron Boost & Muscle Strength";
            case "🍗 Grilled Chicken":
                return "💪 Protein for Muscle Building";
            case "🥩 Beef Steak":
                return "🩸 Iron Boost & Strength";
            case "🍗 Turkey Meatballs":
                return "💖 Heart Health & Low-Fat Protein";
            case "🥓 Bacon Strips":
                return "⚠ High in Fat, Consume in Moderation";
            case "🍖 BBQ Ribs":
                return "💪 Protein Source & Bone Strength";

            // *🍣 Seafood Dishes*
            case "🍣 Salmon Sushi":
                return "🧠 Brain Health & Omega-3 Benefits";
            case "🦐 Garlic Butter Shrimp":
                return "💖 Heart Health & Anti-Inflammatory";
            case "🐟 Grilled Tuna Steak":
                return "💪 High-Protein & Energy Booster";
            case "🍲 Seafood Chowder":
                return "🦴 Bone Strength & Rich in Minerals";
            case "🐙 Octopus Salad":
                return "🧠 Cognitive Function & Lean Protein";

            // *🍛 Asian Dishes*
            case "🍜 Ramen":
                return "⚠ High in Sodium, Consume Occasionally";
            case "🥡 Fried Rice":
                return "⚠ Moderate Carbs, Good Energy Source";
            case "🥟 Dumplings":
                return "💪 Balanced Protein & Carbs";
            case "🍢 Teriyaki Chicken":
                return "💖 Heart-Friendly If Prepared with Low Sugar";
            case "🍛 Thai Green Curry":
                return "🦠 Gut Health & Rich in Spices";
            case "🍱 Bento Box":
                return "💪 Balanced Meal for Energy";
            case "🍣 Sushi":
                return "🧠 Omega-3 & Brain Health Boost";

            // *🥦 Vegetables & Side Dishes*
            case "🥗 Caesar Salad":
                return "⚠ Can Be High in Calories If Dressing is Excessive";
            case "🥑 Guacamole":
                return "🧠 Brain Health & Heart-Friendly Fats";
            case "🥕 Roasted Brussels Sprouts":
                return "🦴 Bone Health & High in Fiber";
            case "🍆 Grilled Eggplant":
                return "🦠 Antioxidant-Rich & Good for Digestion";

            case "🍋 Lemon Juice":
                return "🦠 Immunity Boost & Vitamin C Rich";

            // *🍉 Fruits & Fresh Items*
            case "🍑 Peaches":
                return "🦠 Immunity & Skin Health";
            case "🍊 Oranges":
                return "🦠 Immune Booster & Vitamin C Rich";
            case "🍇 Grapes":
                return "💖 Heart Health & Anti-Aging Properties";
            case "🍎 Apples":
                return "🫀 Gut Health & High in Fiber";
            case "🥝 Kiwi":
                return "💖 Heart Health & High in Fiber";
            case "🍍 Pineapple":
                return "⚠ May Contain Citrus Allergens";
            case "🍓 Strawberries":
                return "🚫 Can Cause Allergic Reactions";
            case "🍌 Banana":
                return "✅ No Major Allergens";
            case "🍉 Watermelon":
                return "💧 Hydration & Rich in Antioxidants";
            case "🥭 Mango":
                return "⚠ May Contain Citrus Allergens";

            // *🍹 Drinks & Beverages*
            case "🍵 Chamomile Tea":
                return "🧘 Stress Relief & Improves Sleep";
            case "🥛 Coconut Water":
                return "💧 Natural Hydration & Electrolyte Balance";
            case "🍊 Orange Juice":
                return "🦠 Immunity Boost, But Watch the Sugar";
            case "🥤 Iced Tea":
                return "⚠ Can Be High in Sugar, Opt for Unsweetened";
            case "🍷 Red Wine":
                return "💖 Heart Health Benefits in Moderation";
            case "🍹 Mango Lassi":
                return "🚫 Contains Dairy";
            case "🧃 Apple Juice":
                return "✅ No Major Allergens";
            case "🥤 Lemonade":
                return "⚠ May Contain Citrus";
            case "🥛 Almond Milk":
                return "🦴 Bone Health & Vitamin E Boost";
            case "🍶 Soy Milk":
                return "⚠ Contains Soy";

            // *🍰 Desserts & Sweets*
            case "🎂 Cheesecake":
                return "⚠ High in Sugar & Fat, Enjoy Occasionally";
            case "🍮 Crème Brûlée":
                return "🦴 Calcium-Rich But High in Sugar";
            case "🍯 Honey Toast":
                return "⚠ High in Sugar, Best as an Occasional Treat";
            case "🍧 Mango Sorbet":
                return "🦠 Immunity Boost & Refreshing Dessert";
            case "🥜 Peanut Butter Cookies":
                return "💪 Good Protein & Energy Source, But Watch Portion Sizes";
            case "🍩 Donut":
                return "⚠ High in Sugar & Fat, Eat in Moderation";
            case "🍪 Chocolate Chip Cookies":
                return "🚫 Contains Gluten, Dairy & Eggs";
            case "🍫 Dark Chocolate":
                return "🚫 Contains Dairy & Nuts (May Contain Traces)";
            case "🍦 Vanilla Ice Cream":
                return "🚫 Contains Dairy";
            case "🍪 Brownies":
                return "🚫 Contains Eggs, Gluten & Dairy";
            case "🍧 Frozen Yogurt":
                return "🚫 Contains Dairy";

            // *Bengali Dishes*
            case "🍛 Shorshe Ilish":
                return "🧠 Omega-3 Benefits for Brain Health";
            case "🍚 Bhuna Khichuri":
                return "💖 Good for Heart Health";
            case "🥘 Chingri Malai Curry":
                return "🦠 Rich in Protein & Healthy Fats";
            case "🍛 Begun Bharta":
                return "💪 High in Antioxidants & Fiber";
            case "🍲 Aloo Posto":
                return "💖 High in Fiber";
            case "🍛 Shorshe Bata Maach":
                return "🧠 Omega-3 Benefits for Brain Health";
            case "🍚 Macher Jhol":
                return "🦠 Rich in Protein";
            case "🍲 Panta Bhat":
                return "💧 Good for Hydration";
            case "🍛 Cholar Dal":
                return "🦠 High in Fiber & Protein";
            case "🍚 Aloo Bhorta":
                return "💪 Rich in Vitamins & Minerals";
            case "🍛 Kachauli":
                return "💖 Healthy Carbs & Protein";
            case "🍚 Pulao":
                return "💪 Good Source of Carbohydrates";
            case "🍛 Korma":
                return "🧠 Contains Healthy Fats";
            case "🍲 Dhokar Dalna":
                return "💖 High in Fiber";
            case "🍛 Aloo Macher Jhol":
                return "🧠 High in Omega-3 Fatty Acids";
            case "🍚 Rajma":
                return "🦠 Good for Gut Health";
            case "🍛 Macher Matha Diye Dal":
                return "🧠 Rich in Omega-3";
            case "🍚 Chingri Bhorta":
                return "💪 Good for Muscle Recovery";
            case "🍛 Shorshe Pata":
                return "🦠 Boosts Immunity";
            case "🍲 Paatla Bhaat":
                return "💧 Good for Digestion";

            // *Indian Dishes*
            case "🍛 Butter Chicken":
                return "💪 High in Protein";
            case "🍚 Paneer Tikka":
                return "🧠 High in Calcium";
            case "🍛 Dal Tadka":
                return "🦠 Rich in Protein & Fiber";
            case "🍲 Prawn Masala":
                return "🧠 High in Omega-3";
            case "🍚 Vegetable Biryani":
                return "💖 Good for Heart Health";
            case "🍛 Tandoori Chicken":
                return "💪 High in Protein";
            case "🍚 Pesarattu":
                return "💖 Good for Heart Health";
            case "🍛 Chana Masala":
                return "🦠 Rich in Fiber & Protein";
            case "🍲 Masoor Dal":
                return "💪 Good for Muscle Recovery";
            case "🍚 Baingan Bharta":
                return "💖 High in Antioxidants & Fiber";

            // *International Dishes*
            case "🍣 California Rolls":
                return "💖 High in Omega-3 & Protein";
            case "🥡 Chinese Fried Rice":
                return "💪 Good for Energy";
            case "🍜 Pho":
                return "🦠 Boosts Immunity";
            case "🍛 Paella":
                return "💖 High in Protein & Omega-3";
            case "🍱 Sushi Rolls":
                return "💪 Rich in Protein & Healthy Fats";
            case "🍤 Shrimp Scampi":
                return "🦠 Boosts Immunity & Heart Health";
            case "🍲 Risotto":
                return "💖 Good for Digestive Health";
            case "🍗 Chicken Kiev":
                return "🧠 High in Protein & Healthy Fats";
            case "🍖 Roast Lamb":
                return "💪 High in Protein & Iron";
            case "🍜 Tom Yum Soup":
                return "🦠 Good for Immunity & Detox";

            // *Drinks & Beverages*
            case "🥤 Cold Brew Coffee":
                return "⚡ Boosts Energy & Metabolism";
            case "🍹 Lemonade":
                return "💧 Good for Hydration & Vitamin C";
            case "🍵 Iced Matcha Latte":
                return "🧠 Boosts Brain Function & Energy";
            case "🥛 Coconut Milkshake":
                return "💖 Rich in Healthy Fats";
            case "🍊 Fresh Orange Juice":
                return "💖 High in Vitamin C & Antioxidants";

            default:
                return "🩺 General Wellness";
        }
    }


    // Allergy Warnings Based on Recipe
    private String getAllergyWarning(String recipeName) {
        switch (recipeName) {
            // *🥗 Vegetarian & Vegan Dishes*
            case "🥗 Greek Salad":
                return "🚫 Contains Dairy (Feta Cheese)";
            case "🥑 Avocado Toast":
                return "🚫 Contains Gluten (Bread)";
            case "🥕 Carrot Sticks":
                return "✅ No Major Allergens";
            case "🍆 Eggplant Parmesan":
                return "🚫 Contains Dairy & Gluten";
            case "🌽 Corn on the Cob":
                return "✅ No Major Allergens";
            case "🥦 Broccoli Stir-Fry":
                return "✅ No Major Allergens";
            case "🥒 Cucumber Salad":
                return "✅ No Major Allergens";
            case "🍠 Roasted Sweet Potatoes":
                return "✅ No Major Allergens";
            case "🍄 Stuffed Mushrooms":
                return "⚠ May Contain Dairy (Cheese)";
            case "🌱 Tofu Stir-Fry":
                return "🚫 Contains Soy";
            case "🥬 Saag Paneer":
                return "🚫 Contains Dairy";

            // *🍗 Meat & Poultry Dishes*
            case "🍔 Cheeseburger":
                return "🚫 Contains Gluten & Dairy";
            case "🌮 Chicken Tacos":
                return "⚠ May Contain Gluten (Tortilla)";
            case "🍖 Lamb Chops":
                return "✅ No Major Allergens";
            case "🍗 Grilled Chicken":
                return "✅ No Major Allergens";
            case "🥩 Beef Steak":
                return "✅ No Major Allergens";
            case "🍗 Turkey Meatballs":
                return "🚫 Contains Gluten & Eggs";

            // *🍣 Seafood Dishes*
            case "🍣 Salmon Sushi":
                return "🚫 Contains Seafood & Soy (Soy Sauce)";
            case "🦐 Garlic Butter Shrimp":
                return "🚫 Contains Shellfish & Dairy";
            case "🐟 Grilled Tuna Steak":
                return "🚫 Contains Seafood";
            case "🍲 Seafood Chowder":
                return "🚫 Contains Seafood & Dairy";
            case "🐙 Octopus Salad":
                return "🚫 Contains Seafood";

            // *🍛 Asian Dishes*
            case "🍜 Ramen":
                return "🚫 Contains Gluten & Soy";
            case "🥡 Fried Rice":
                return "⚠ May Contain Soy & Eggs";
            case "🥟 Dumplings":
                return "🚫 Contains Gluten";
            case "🍢 Teriyaki Chicken":
                return "🚫 Contains Soy & Gluten";
            case "🍛 Thai Green Curry":
                return "🚫 Contains Coconut & Seafood";
            case "🍱 Bento Box":
                return "✅ No Major Allergens";
            case "🍣 Sushi":
                return "✅ No Major Allergens";

            // *🥦 Vegetables & Side Dishes*
            case "🥗 Caesar Salad":
                return "🚫 Contains Dairy & Fish (Anchovies)";
            case "🥑 Guacamole":
                return "✅ No Major Allergens";
            case "🥕 Roasted Brussels Sprouts":
                return "✅ No Major Allergens";
            case "🍆 Grilled Eggplant":
                return "✅ No Major Allergens";

            case "🍋 Lemon Juice":
                return "⚠ May Contain Citrus Allergens";

            // *🍉 Fruits & Fresh Items*
            case "🍑 Peaches":
                return "🦠 May Trigger Oral Allergy Syndrome";
            case "🍊 Oranges":
                return "⚠ May Contain Citrus Allergens";
            case "🍇 Grapes":
                return "✅ No Major Allergens";
            case "🍎 Apples":
                return "🫀 Gut Health & High in Fiber";
            case "🥝 Kiwi":
                return "⚠ Can Cause Allergic Reactions";
            case "🍍 Pineapple":
                return "⚠ May Contain Citrus Allergens";
            case "🍓 Strawberries":
                return "🚫 Can Cause Allergic Reactions";
            case "🍌 Banana":
                return "✅ No Major Allergens";
            case "🍉 Watermelon":
                return "✅ No Major Allergens";
            case "🥭 Mango":
                return "⚠ May Contain Citrus Allergens";


            // *🍹 Drinks & Beverages*
            case "🍵 Chamomile Tea":
                return "⚠ May Cause Allergic Reactions in Some People";
            case "🥛 Coconut Water":
                return "🚫 Contains Coconut";
            case "🍊 Orange Juice":
                return "⚠ May Contain Citrus Allergens";
            case "🥤 Iced Tea":
                return "✅ No Major Allergens";
            case "🍷 Red Wine":
                return "⚠ May Contain Sulfites";
            case "🍹 Mango Lassi":
                return "🚫 Contains Dairy";
            case "🧃 Apple Juice":
                return "✅ No Major Allergens";
            case "🥤 Lemonade":
                return "⚠ May Contain Citrus";
            case "🥛 Almond Milk":
                return "⚠ Contains Almond";
            case "🍶 Soy Milk":
                return "⚠ Contains Soy";

            // *🍰 Desserts & Sweets*
            case "🎂 Cheesecake":
                return "🚫 Contains Dairy & Gluten";
            case "🍮 Crème Brûlée":
                return "🚫 Contains Dairy & Eggs";
            case "🍯 Honey Toast":
                return "🚫 Contains Gluten & Dairy";
            case "🍧 Mango Sorbet":
                return "⚠ May Contain Citrus Allergens";
            case "🥜 Peanut Butter Cookies":
                return "🚫 Contains Peanuts & Gluten";
            case "🍩 Donut":
                return "🚫 Contains Gluten";
            case "🍪 Chocolate Chip Cookies":
                return "🚫 Contains Gluten, Dairy & Eggs";
            case "🍫 Dark Chocolate":
                return "🚫 Contains Dairy & Nuts (May Contain Traces)";
            case "🍦 Vanilla Ice Cream":
                return "🚫 Contains Dairy";
            case "🍪 Brownies":
                return "🚫 Contains Eggs, Gluten & Dairy";
            case "🍧 Frozen Yogurt":
                return "🚫 Contains Dairy";

            // *Bengali Dishes*
            case "🍛 Shorshe Ilish":
                return "🚫 Contains Fish";
            case "🍚 Bhuna Khichuri":
                return "🚫 Contains Gluten (Rice)";
            case "🥘 Chingri Malai Curry":
                return "🚫 Contains Shrimp & Coconut";
            case "🍛 Begun Bharta":
                return "✅ No Major Allergens";
            case "🍲 Aloo Posto":
                return "✅ No Major Allergens";
            case "🍛 Shorshe Bata Maach":
                return "🚫 Contains Fish & Mustard";
            case "🍚 Macher Jhol":
                return "🚫 Contains Fish";
            case "🍲 Panta Bhat":
                return "✅ No Major Allergens";
            case "🍛 Cholar Dal":
                return "✅ No Major Allergens";
            case "🍚 Aloo Bhorta":
                return "✅ No Major Allergens";
            case "🍛 Kachauli":
                return "✅ No Major Allergens";
            case "🍚 Pulao":
                return "🚫 Contains Gluten";
            case "🍛 Korma":
                return "🚫 Contains Dairy & Nuts";
            case "🍲 Dhokar Dalna":
                return "✅ No Major Allergens";
            case "🍛 Aloo Macher Jhol":
                return "🚫 Contains Fish";
            case "🍚 Rajma":
                return "✅ No Major Allergens";
            case "🍛 Macher Matha Diye Dal":
                return "🚫 Contains Fish";
            case "🍚 Chingri Bhorta":
                return "🚫 Contains Shrimp";
            case "🍛 Shorshe Pata":
                return "🚫 Contains Mustard";
            case "🍲 Paatla Bhaat":
                return "✅ No Major Allergens";

            // *Indian Dishes*
            case "🍛 Butter Chicken":
                return "🚫 Contains Dairy";
            case "🍚 Paneer Tikka":
                return "🚫 Contains Dairy";
            case "🍛 Dal Tadka":
                return "✅ No Major Allergens";
            case "🍲 Prawn Masala":
                return "🚫 Contains Shrimp";
            case "🍚 Vegetable Biryani":
                return "✅ No Major Allergens";
            case "🍛 Tandoori Chicken":
                return "✅ No Major Allergens";
            case "🍚 Pesarattu":
                return "✅ No Major Allergens";
            case "🍛 Chana Masala":
                return "✅ No Major Allergens";
            case "🍲 Masoor Dal":
                return "✅ No Major Allergens";
            case "🍚 Baingan Bharta":
                return "✅ No Major Allergens";

            // *International Dishes*
            case "🍣 California Rolls":
                return "🚫 Contains Seafood & Soy";
            case "🥡 Chinese Fried Rice":
                return "🚫 Contains Soy & Gluten";
            case "🍜 Pho":
                return "🚫 Contains Seafood & Gluten";
            case "🍛 Paella":
                return "🚫 Contains Shellfish";
            case "🍱 Sushi Rolls":
                return "🚫 Contains Seafood";
            case "🍤 Shrimp Scampi":
                return "🚫 Contains Shellfish";
            case "🍲 Risotto":
                return "🚫 Contains Dairy";
            case "🍗 Chicken Kiev":
                return "🚫 Contains Dairy & Gluten";
            case "🍖 Roast Lamb":
                return "✅ No Major Allergens";
            case "🍜 Tom Yum Soup":
                return "🚫 Contains Shellfish";

            // *Drinks & Beverages*
            case "🥤 Cold Brew Coffee":
                return "✅ No Major Allergens";
            case "🍹 Lemonade":
                return "✅ No Major Allergens";
            case "🍵 Iced Matcha Latte":
                return "🚫 Contains Dairy";
            case "🥛 Coconut Milkshake":
                return "🚫 Contains Coconut & Dairy";
            case "🍊 Fresh Orange Juice":
                return "✅ No Major Allergens";
            default:
                return "⚠ Unknown Ingredients";
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}