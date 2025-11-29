package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChoosManuallyScene {
    private TextField inputField, textField;
    private VBox fieldsVBox, finalVBox;
    private Button checkButton, backButton, startButton, saveButton;
    private Label errorLabel;
    private ScrollPane scrollPane;
    static Scene scene;
    static int[] numbersList = new int[0];
    Text text;
    GameTowPlayer gameScene = new GameTowPlayer();
    GameTheComputer computer = new GameTheComputer();

    public int inputFieldValue() {
        return Integer.parseInt(inputField.getText());
    }

    public void initializechoosManually(Stage primaryStage) {
        errorLabel = new Label();
        primaryStage.setTitle("Choose Manually Scene");

        Text text = new Text("Please Enter The Number Of Coins (Even Number)");
        text.setStyle("-fx-border-color: #000000; -fx-border-width: 2px; -fx-font-size: 18px; -fx-font-weight: bold;");

        finalVBox = new VBox(30);
        fieldsVBox = new VBox();
        fieldsVBox.setAlignment(Pos.CENTER);
        fieldsVBox.setSpacing(5);

        inputField = new TextField();
        inputField.setPromptText("Please enter an integer (even number)");

        checkButton = new Button("Click to complete");
        setButtonStyle(checkButton);
        checkButton.setOnAction(e -> checkCoins());

        startButton = new Button("Start The Game");
        setButtonStyle(startButton);
        startButton.setVisible(false); // Initially hidden
        startButton.setOnAction(e -> {
            int inputValue = inputFieldValue();
            if (inputValue % 2 != 0) {
                showError("Please enter an even number.");
            } else if (OpeneScene.towPlayer.isSelected()) {
                gameScene.initializeGameScene(primaryStage, numbersList);
                primaryStage.setScene(gameScene.getGameScene());
            } else if (OpeneScene.Computer.isSelected()) {
                computer.initializeGameScene(primaryStage, numbersList);
                primaryStage.setScene(computer.getScene());
            } else {
                showError("Please select a game mode.");
            }
        });

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(text, inputField, checkButton, fieldsVBox);
        layout.setPadding(new Insets(20));

        backButton = new Button("Back And Start Again");
        setButtonStyle(backButton);
        backButton.setOnAction(e -> {
            fieldsVBox.getChildren().clear();
            primaryStage.setScene(OpeneScene.mainScene);
        });

        saveButton = new Button("Save Values");
        setButtonStyle(saveButton);
        saveButton.setVisible(false);
        saveButton.setOnAction(e -> saveValues());

        VBox buttonsVBox = new VBox(20);
        buttonsVBox.setAlignment(Pos.CENTER);
        buttonsVBox.getChildren().addAll(saveButton, startButton, backButton);

        scrollPane = new ScrollPane();
        scrollPane.setContent(fieldsVBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVisible(false);

        finalVBox.getChildren().addAll(layout, scrollPane, buttonsVBox);
        finalVBox.setStyle("-fx-background-color: lightblue;");
        scene = new Scene(finalVBox, 500, 500);
    }

    private void checkCoins() {
        try {
            inputFieldValue();
            fieldsVBox.getChildren().clear();

            if (inputFieldValue() % 2 != 0) {
                showError("Please enter an even number.");
            } else {
                numbersList = new int[inputFieldValue()];

                for (int i = 0; i < inputFieldValue(); i++) {
                    textField = new TextField();
                    textField.setPromptText("Coin " + (i + 1));
                    fieldsVBox.getChildren().add(textField);
                }
                saveButton.setVisible(true);
                scrollPane.setVisible(true);
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid number.");
        }
    }

    private void saveValues() {
        boolean allFieldsValid = true;

        for (int i = 0; i < fieldsVBox.getChildren().size(); i++) {
            if (fieldsVBox.getChildren().get(i) instanceof TextField) {
                TextField textField = (TextField) fieldsVBox.getChildren().get(i);
                String textValue = textField.getText();

                if (textValue.isEmpty()) {
                    showError("Please fill in all coin values.");
                    allFieldsValid = false;
                    break;
                }

                try {
                    int value = Integer.parseInt(textValue);
                    numbersList[i] = value;
                } catch (NumberFormatException e) {
                    showError("Please enter valid numbers for all coins.");
                    allFieldsValid = false;
                    break;
                }
            }
        }

        if (allFieldsValid) {
            System.out.println("Saved coin values: " + java.util.Arrays.toString(numbersList));
            showError(""); // Clear any previous error message
            startButton.setVisible(true); // Show the "Start The Game" button
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        if (!fieldsVBox.getChildren().contains(errorLabel)) {
            fieldsVBox.getChildren().add(errorLabel);
        }
    }

    public Scene getScene() {
        return scene;
    }

    private void setButtonStyle(Button button) {
        button.setMinWidth(Button.USE_PREF_SIZE);
        button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
    }
}
