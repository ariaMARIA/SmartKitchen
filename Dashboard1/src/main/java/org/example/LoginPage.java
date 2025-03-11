package org.example;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundImage;

public class LoginPage extends Application {

    private Button nextButton;
    private Button backButton;
    private TextField usernameField;
    private PasswordField passwordField;

    private static final String CREDENTIALS_FILE = "user_credentials.txt"; // File to store credentials

    @Override
    public void start(Stage primaryStage) {
        showLoginPage(primaryStage);
    }

    private void showLoginPage(Stage primaryStage) {
        // Title Label
        Text titleLabel = new Text("Test the Best, Love the Rest!!");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #FFF0F5;");

        // Username and Password fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-font-size: 18px; -fx-padding: 15px; -fx-background-color: white ; -fx-text-fill: black;");
        usernameField.setMinWidth(150);  // Set the min width for the input field
        usernameField.setPrefWidth(250);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 18px; -fx-padding: 15px; -fx-background-color: white; -fx-text-fill: black;");
        passwordField.setMinWidth(150);  // Set the min width for the password field
        passwordField.setPrefWidth(250);

        // Log In Button
        Button loginButton = new Button("   Log In   ");
        loginButton.setStyle("-fx-background-color: #9B2242; -fx-text-fill: white; -fx-font-size: 18px;");
        loginButton.setOnAction(event -> loginAction(primaryStage));

        // Forgot Password and Sign Up Links with contrasting colors
        Hyperlink forgotPasswordLink = new Hyperlink(" Forgot Password? ");
        Hyperlink signUpLink = new Hyperlink("   Sign Up   ");

        forgotPasswordLink.setStyle("-fx-background-color: #FFF0F5; -fx-text-fill: #FF4081; -fx-font-size: 18px;");
        signUpLink.setStyle("-fx-background-color: #FF1493; -fx-text-fill: white; -fx-font-size: 18px;");
        signUpLink.setOnAction(event -> showSignUpPage(primaryStage));

        // Main Layout
        VBox mainLayout = new VBox(20, titleLabel, usernameField, passwordField, loginButton, forgotPasswordLink, signUpLink);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(50));

        // Set the background image
        String imageUrl = getClass().getResource("/aabb.png").toExternalForm(); // Update the image path
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imageUrl),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        // Use AnchorPane to place items over the image
        AnchorPane rootPane = new AnchorPane();
        rootPane.setBackground(new Background(backgroundImage));
        AnchorPane.setTopAnchor(mainLayout, 120.0);
        AnchorPane.setLeftAnchor(mainLayout, 50.0);
        AnchorPane.setRightAnchor(mainLayout, 50.0);
        rootPane.getChildren().add(mainLayout);

        // Create and show the scene
        Scene scene = new Scene(rootPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();
    }

    private void loginAction(Stage primaryStage) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Check if the credentials are valid
        if (isValidCredentials(username, password)) {
            showNextPage(primaryStage);
        } else {
            showError("Invalid username or password.");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        File file = new File(CREDENTIALS_FILE);

        // Check if file is empty
        if (file.length() == 0) {
            return false; // If file is empty, no credentials are found
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true; // Credentials match
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading credentials file.");
        }

        return false; // Return false if no matching credentials are found
    }

    private void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Login Failed");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void showNextPage(Stage primaryStage) {
        // Create a new scene for the next page
        Text nextPageText = new Text("Welcome to the next page!");
        nextPageText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #F4C2C2;");

        VBox nextPageLayout = new VBox(20, nextPageText);
        nextPageLayout.setAlignment(Pos.CENTER);
        Scene nextScene = new Scene(nextPageLayout, 800, 600);
        primaryStage.setScene(nextScene);
    }

    private void showSignUpPage(Stage primaryStage) {
        // Title Label
        Text titleLabel = new Text("Create Account");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-fill: #FFF0F5;");// #FF4081

        // Username and Password fields
        TextField signUpUsernameField = new TextField();
        signUpUsernameField.setPromptText("Username");
        signUpUsernameField.setStyle("-fx-font-size: 18px; -fx-padding: 10px; -fx-background-color: white; -fx-text-fill: black;");

        PasswordField signUpPasswordField = new PasswordField();
        signUpPasswordField.setPromptText("Password");
        signUpPasswordField.setStyle("-fx-font-size: 18px; -fx-padding: 10px; -fx-background-color: white; -fx-text-fill: black;");

        // Sign Up Button
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #FF4081; -fx-text-fill: white; -fx-font-size: 15px;");
        signUpButton.setOnAction(event -> signUpAction(signUpUsernameField, signUpPasswordField, primaryStage));

        // Main Layout
        VBox signUpLayout = new VBox(20, titleLabel, signUpUsernameField, signUpPasswordField, signUpButton);
        signUpLayout.setAlignment(Pos.CENTER);
        signUpLayout.setPadding(new Insets(50));

        // Set the background image (optional)
        String signUpImageUrl = getClass().getResource("/b.jpg").toExternalForm(); // Same image path
        BackgroundImage signUpBackgroundImage = new BackgroundImage(
                new Image(signUpImageUrl),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );
        signUpLayout.setBackground(new Background(signUpBackgroundImage));

        // Create and show the scene
        Scene signUpScene = new Scene(signUpLayout, 800, 600);
        primaryStage.setScene(signUpScene);
        primaryStage.setTitle("Sign Up Page");
        primaryStage.show();
    }

    private void signUpAction(TextField signUpUsernameField, PasswordField signUpPasswordField, Stage primaryStage) {
        String username = signUpUsernameField.getText();
        String password = signUpPasswordField.getText();

        // Check if account already exists
        if (isUsernameExists(username)) {
            showError("Account already exists.");
            return;
        }

        // Save the new credentials to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE, true))) {
            writer.write(username + "," + password);
            writer.newLine();
            showNextPage(primaryStage);
        } catch (IOException e) {
            System.out.println("Error saving credentials.");
        }
    }

    private boolean isUsernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;

            // Check if the username already exists in the file
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username)) {
                    return true; // Username already exists
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading credentials file.");
        }
        return false; // Username not found
    }

    public static void main(String[] args) {
        launch(args);
    }
}



//password save=file:user_credentials