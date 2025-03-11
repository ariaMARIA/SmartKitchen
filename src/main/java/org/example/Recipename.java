
/*
package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Recipename extends Application {

    private Button nextButton;
    private Recipe selectedRecipe;
    private inventoryApp app;

    public static class Recipe {
        private final String name;
        private final ObservableList<String> ingredients;
        private final ObservableList<String> preparationSteps;

        public Recipe(String name, ObservableList<String> ingredients, ObservableList<String> preparationSteps) {
            this.name = name;
            this.ingredients = ingredients;
            this.preparationSteps = preparationSteps;
        }

        public String getName() { return name; }
        public ObservableList<String> getIngredients() { return ingredients; }
        public ObservableList<String> getPreparationSteps() { return preparationSteps; }
    }

    @Override
    public void start(Stage primaryStage) {
        app = new inventoryApp();
        app.loadIngredients();

        Text titleLabel = new Text("🍽 Recipe Ideas");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");
        Recipe[] recipes = {
                new Recipe("🍽 Apple Pie",
                        FXCollections.observableArrayList("Apple", "Flour", "Sugar", "Butter", "Cinnamon"),
                        FXCollections.observableArrayList("1. Preheat the oven to 350°F.",
                                "2. Mix all ingredients.",
                                "3. Bake for 45 minutes.")
                ),
                new Recipe("🍽 Banana Bread",
                        FXCollections.observableArrayList("Banana", "Flour", "Eggs", "Baking Soda", "Sugar"),
                        FXCollections.observableArrayList("1. Preheat the oven to 350°F.",
                                "2. Mash the bananas and mix with the rest of the ingredients.",
                                "3. Bake for 60 minutes.")
                ),
                new Recipe("🍽 Chocolate Cake",
                        FXCollections.observableArrayList("Flour", "Cocoa Powder", "Sugar", "Eggs", "Butter", "Milk"),
                        FXCollections.observableArrayList("1. Preheat oven to 350°F.",
                                "2. Mix all dry and wet ingredients separately.",
                                "3. Combine and bake for 40 minutes.")
                ),
                new Recipe("🍽 Chicken Curry",
                        FXCollections.observableArrayList("Chicken", "Onion", "Garlic", "Tomato", "Spices"),
                        FXCollections.observableArrayList("1. Sauté onions and garlic.",
                                "2. Add chicken and spices.",
                                "3. Cook until tender and serve.")
                ),
                new Recipe("🍽 Pasta Carbonara",
                        FXCollections.observableArrayList("Pasta", "Eggs", "Bacon", "Parmesan", "Black Pepper"),
                        FXCollections.observableArrayList("1. Cook pasta and set aside.",
                                "2. Fry bacon until crispy.",
                                "3. Mix eggs, parmesan, and pasta, then serve.")
                ),
                new Recipe("🍽 Vegetable Stir Fry",
                        FXCollections.observableArrayList("Carrot", "Bell Pepper", "Broccoli", "Soy Sauce", "Garlic"),
                        FXCollections.observableArrayList("1. Chop vegetables.",
                                "2. Stir fry with soy sauce and garlic.",
                                "3. Serve hot.")
                )
        };

        // VBox to contain recipe cards
        VBox recipeCards = new VBox(10);
        recipeCards.setPadding(new Insets(15));

        for (Recipe recipe : recipes) {
            VBox recipeCard = new VBox(10);
            recipeCard.setPadding(new Insets(10));
            recipeCard.setStyle("-fx-background-color: #f3e5f5; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
            recipeCard.setPrefWidth(250);

            Text recipeText = new Text(recipe.getName());
            recipeText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            recipeCard.getChildren().add(recipeText);

            recipeCards.getChildren().add(recipeCard);

            recipeCard.setOnMouseClicked(event -> {
                selectedRecipe = recipe;
                nextButton.setDisable(false);

                // Reset all recipe cards to default
                for (var node : recipeCards.getChildren()) {
                    if (node instanceof VBox) {
                        node.setStyle("-fx-background-color: #f3e5f5; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
                    }
                }

                // Highlight selected recipe
                recipeCard.setStyle("-fx-background-color: #C8E6C9; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
            });
        }

        // Creating ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(recipeCards);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300); // Ensure it's large enough to fit multiple cards
        scrollPane.setStyle("-fx-background: transparent;");

        // Make sure the scroll content is refreshed when necessary
        recipeCards.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(0);  // Reset scroll to top when the content height changes
        });

        // Next button functionality
        nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        nextButton.setPadding(new Insets(10));
        nextButton.setDisable(true);
        nextButton.setOnAction(event -> showIngredientsTable(primaryStage));

        VBox mainLayout = new VBox(10, titleLabel, scrollPane, nextButton);
        mainLayout.setStyle("-fx-background-color: #FCE4EC;");
        mainLayout.setPadding(new Insets(15));

        Scene scene = new Scene(mainLayout, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Kitchen Inventory");
        primaryStage.show();
    }

    private void showIngredientsTable(Stage primaryStage) {
        Text titleLabel = new Text("🍽 Ingredients for " + selectedRecipe.getName());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");

        VBox ingredientList = new VBox(10);
        ingredientList.setPadding(new Insets(15));
        ingredientList.setStyle("-fx-background-color: #FCE4EC;");
        ingredientList.getChildren().add(titleLabel);

        for (String ingredient : selectedRecipe.getIngredients()) {
            HBox ingredientItem = new HBox(10);
            ingredientItem.setStyle("-fx-background-color: #f3e5f5; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
            ingredientItem.setPadding(new Insets(10));
            ingredientItem.setPrefWidth(250);

            String status = isIngredientAvailable(ingredient) ? "✔" : "❌";
            Text ingredientText = new Text("• " + ingredient + " " + status);
            ingredientText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            ingredientItem.getChildren().add(ingredientText);

            ingredientList.getChildren().add(ingredientItem);
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        backButton.setPadding(new Insets(10));
        backButton.setOnAction(event -> start(primaryStage));

        Button showPreparationButton = new Button("Next (Steps)");
        showPreparationButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        showPreparationButton.setPadding(new Insets(10));
        showPreparationButton.setOnAction(event -> showRecipePreparationStep(primaryStage));

        HBox buttonBox = new HBox(10, backButton, showPreparationButton);
        ingredientList.getChildren().add(buttonBox);

        Scene scene = new Scene(ingredientList, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ingredients for Recipe");
        primaryStage.show();
    }

    private void showRecipePreparationStep(Stage primaryStage) {
        Text titleLabel = new Text("🍽 Recipe Preparation for: " + selectedRecipe.getName());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");

        VBox preparationStepsList = new VBox(15);
        preparationStepsList.setPadding(new Insets(20));
        preparationStepsList.setStyle("-fx-background-color: #FCE4EC;");
        preparationStepsList.getChildren().add(titleLabel);

        for (String step : selectedRecipe.getPreparationSteps()) {
            Text stepText = new Text("• " + step);
            stepText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            preparationStepsList.getChildren().add(stepText);
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        backButton.setPadding(new Insets(10));
        backButton.setOnAction(event -> showIngredientsTable(primaryStage));

        preparationStepsList.getChildren().add(backButton);

        Scene scene = new Scene(preparationStepsList, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Recipe Preparation Steps");
        primaryStage.show();
    }

    private boolean isIngredientAvailable(String ingredientName) {
        return app.data.stream().anyMatch(ingredient -> ingredient.getName().equalsIgnoreCase(ingredientName));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/
//asol

package org.example;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Recipename extends Application {

    private Button nextButton;
    private Recipe selectedRecipe;
    private inventoryApp app;

    public static class Recipe {
        private final String name;
        private final ObservableList<String> ingredients;
        private final ObservableList<String> preparationSteps;

        public Recipe(String name, ObservableList<String> ingredients, ObservableList<String> preparationSteps) {
            this.name = name;
            this.ingredients = ingredients;
            this.preparationSteps = preparationSteps;
        }

        public String getName() { return name; }
        public ObservableList<String> getIngredients() { return ingredients; }
        public ObservableList<String> getPreparationSteps() { return preparationSteps; }
    }

    @Override
    public void start(Stage primaryStage) {
        app = new inventoryApp();
        app.loadIngredients();

        Text titleLabel = new Text("🍽 Recipe Ideas");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");

        Recipe[] recipes = {
                new Recipe("🍽 Apple Pie",
                        FXCollections.observableArrayList("Apple", "Flour", "Sugar", "Butter", "Cinnamon"),
                        FXCollections.observableArrayList("1. Preheat the oven to 350°F.",
                                "2. Mix all ingredients.",
                                "3. Bake for 45 minutes.")
                ),
                new Recipe("🍌 Banana Bread",
                        FXCollections.observableArrayList("Banana", "Flour", "Eggs", "Baking Soda", "Sugar"),
                        FXCollections.observableArrayList("1. Preheat the oven to 350°F.",
                                "2. Mash the bananas and mix with the rest of the ingredients.",
                                "3. Bake for 60 minutes.")
                ),
                new Recipe("🍫 Chocolate Cake",
                        FXCollections.observableArrayList("Flour", "Cocoa Powder", "Sugar", "Eggs", "Butter", "Milk"),
                        FXCollections.observableArrayList("1. Preheat oven to 350°F.",
                                "2. Mix all dry and wet ingredients separately.",
                                "3. Combine and bake for 40 minutes.")
                ),
                new Recipe("🍗 Chicken Curry",
                        FXCollections.observableArrayList("Chicken", "Onion", "Garlic", "Tomato", "Spices"),
                        FXCollections.observableArrayList("1. Sauté onions and garlic.",
                                "2. Add chicken and spices.",
                                "3. Cook until tender and serve.")
                ),
                new Recipe("🍝 Pasta Carbonara",
                        FXCollections.observableArrayList("Pasta", "Eggs", "Bacon", "Parmesan", "Black Pepper"),
                        FXCollections.observableArrayList("1. Cook pasta and set aside.",
                                "2. Fry bacon until crispy.",
                                "3. Mix eggs, parmesan, and pasta, then serve.")
                ),
                new Recipe("🥦 Vegetable Stir Fry",
                        FXCollections.observableArrayList("Carrot", "Bell Pepper", "Broccoli", "Soy Sauce", "Garlic"),
                        FXCollections.observableArrayList("1. Chop vegetables.",
                                "2. Stir fry with soy sauce and garlic.",
                                "3. Serve hot.")
                ),
                new Recipe("🍪 Chocolate Chip Cookies", FXCollections.observableArrayList("Flour", "Sugar", "Butter", "Chocolate Chips", "Eggs", "Baking Powder"),
                        FXCollections.observableArrayList("1. Preheat oven.", "2. Mix ingredients.", "3. Bake for 12 min.")),

                new Recipe("🍔 Beef Burger", FXCollections.observableArrayList("Beef Patty", "Bun", "Lettuce", "Tomato", "Cheese", "Onions", "Pickles"),
                        FXCollections.observableArrayList("1. Cook beef patty.", "2. Assemble burger.", "3. Serve with fries.")),

                new Recipe("🥔 Mashed Potatoes", FXCollections.observableArrayList("Potatoes", "Butter", "Milk", "Salt", "Pepper"),
                        FXCollections.observableArrayList("1. Boil potatoes.", "2. Mash with butter and milk.", "3. Serve warm.")),

                new Recipe("🍛 Butter Chicken", FXCollections.observableArrayList("Chicken", "Tomato", "Butter", "Cream", "Spices"),
                        FXCollections.observableArrayList("1. Cook chicken with spices.", "2. Add butter and cream.", "3. Simmer and serve.")),

                new Recipe("🍣 Sushi Rolls", FXCollections.observableArrayList("Rice", "Nori", "Salmon", "Cucumber", "Avocado"),
                        FXCollections.observableArrayList("1. Spread rice on nori.", "2. Add fillings and roll.", "3. Slice and serve.")),

                new Recipe("🥑 Avocado Toast", FXCollections.observableArrayList("Bread", "Avocado", "Salt", "Pepper", "Lemon Juice"),
                        FXCollections.observableArrayList("1. Toast bread.", "2. Mash avocado with seasoning.", "3. Spread on toast and serve.")),

                new Recipe("🍹 Lemonade", FXCollections.observableArrayList("Lemon", "Sugar", "Water", "Ice"),
                        FXCollections.observableArrayList("1. Squeeze lemons.", "2. Mix with sugar and water.", "3. Serve chilled.")),

                new Recipe("🍝 Lasagna", FXCollections.observableArrayList("Lasagna Noodles", "Tomato Sauce", "Ground Beef", "Cheese", "Ricotta"),
                        FXCollections.observableArrayList("1. Layer ingredients.", "2. Bake at 375°F for 45 min.", "3. Let rest and serve.")),

                new Recipe("🥜 Peanut Butter Sandwich", FXCollections.observableArrayList("Bread", "Peanut Butter", "Jam (optional)"),
                        FXCollections.observableArrayList("1. Spread peanut butter on bread.", "2. Add jam if desired.", "3. Serve.")),

                new Recipe("🍵 Green Tea", FXCollections.observableArrayList("Green Tea Leaves", "Hot Water", "Honey (optional)"),
                        FXCollections.observableArrayList("1. Boil water.", "2. Steep tea leaves for 3 min.", "3. Add honey and serve."))
        };

        // VBox to contain recipe cards
        VBox recipeCards = new VBox(10);
        recipeCards.setPadding(new Insets(15));

        for (Recipe recipe : recipes) {
            VBox recipeCard = new VBox(10);
            recipeCard.setPadding(new Insets(10));
            recipeCard.setStyle("-fx-background-color: #FCE4EC; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
            recipeCard.setPrefWidth(250);

            Text recipeText = new Text(recipe.getName());
            recipeText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            recipeCard.getChildren().add(recipeText);

            recipeCards.getChildren().add(recipeCard);

            recipeCard.setOnMouseClicked(event -> {
                selectedRecipe = recipe;
                nextButton.setDisable(false);

                // Reset all recipe cards to default
                for (var node : recipeCards.getChildren()) {
                    if (node instanceof VBox) {
                        node.setStyle("-fx-background-color: #FCE4EC; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
                    }
                }

                // Highlight selected recipe
                recipeCard.setStyle("-fx-background-color: #F8BBD0; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
            });
        }

        // Creating ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(recipeCards);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450); // Reduced space at the bottom
        scrollPane.setStyle("-fx-background: transparent;");

        recipeCards.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(0);  // Reset scroll to top when the content height changes
        });

        // Next button functionality
        nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        nextButton.setPadding(new Insets(10));
        nextButton.setDisable(true);
        nextButton.setOnAction(event -> showIngredientsTable(primaryStage));

        VBox mainLayout = new VBox(10, titleLabel, scrollPane, nextButton);
        mainLayout.setStyle("-fx-background-color: #FCE4EC;");
        mainLayout.setPadding(new Insets(15));

        Scene scene = new Scene(mainLayout, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Kitchen Inventory");
        primaryStage.show();
    }

    private void showIngredientsTable(Stage primaryStage) {
        Text titleLabel = new Text("🍽 Ingredients for " + selectedRecipe.getName());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");

        VBox ingredientList = new VBox(10);
        ingredientList.setPadding(new Insets(15));
        ingredientList.setStyle("-fx-background-color: #FCE4EC;");
        ingredientList.getChildren().add(titleLabel);

        for (String ingredient : selectedRecipe.getIngredients()) {
            HBox ingredientItem = new HBox(10);
            ingredientItem.setStyle("-fx-background-color:#F8BBD0 ; -fx-border-radius: 10px; -fx-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);");
            ingredientItem.setPadding(new Insets(10));
            ingredientItem.setPrefWidth(200);
            ingredientItem.setPrefHeight(450);
            ingredientItem.setAlignment(Pos.CENTER);  // Centering the text horizontally in the HBox

            String status = isIngredientAvailable(ingredient) ? "✔" : "❌";
            Text ingredientText = new Text("• " + ingredient + " " + status);
            ingredientText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            ingredientItem.getChildren().add(ingredientText);

            ingredientList.getChildren().add(ingredientItem);
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        backButton.setPadding(new Insets(10));
        backButton.setOnAction(event -> start(primaryStage));

        Button showPreparationButton = new Button("Next (Steps)");
        showPreparationButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        showPreparationButton.setPadding(new Insets(10));
        showPreparationButton.setOnAction(event -> showRecipePreparationStep(primaryStage));

        HBox buttonBox = new HBox(10, backButton, showPreparationButton);
        ingredientList.getChildren().add(buttonBox);

        Scene scene = new Scene(ingredientList, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ingredients for Recipe");
        primaryStage.show();
    }

    private void showRecipePreparationStep(Stage primaryStage) {
        Text titleLabel = new Text("🍽 Recipe Preparation for: " + selectedRecipe.getName());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #E91E63;");

        VBox preparationStepsList = new VBox(15);
        preparationStepsList.setPadding(new Insets(20));
        preparationStepsList.setStyle("-fx-background-color: #FCE4EC;");
        preparationStepsList.getChildren().add(titleLabel);

        for (String step : selectedRecipe.getPreparationSteps()) {
            Text stepText = new Text("• " + step);
            stepText.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
            preparationStepsList.getChildren().add(stepText);
        }

        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #E91E63; -fx-text-fill: white; -fx-border-radius: 5px; -fx-font-size: 14px;");
        backButton.setPadding(new Insets(10));
        backButton.setOnAction(event -> showIngredientsTable(primaryStage));

        preparationStepsList.getChildren().add(backButton);

        Scene scene = new Scene(preparationStepsList, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Recipe Preparation Steps");
        primaryStage.show();
    }

    private boolean isIngredientAvailable(String ingredientName) {
        return app.data.stream().anyMatch(ingredient -> ingredient.getName().equalsIgnoreCase(ingredientName));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
