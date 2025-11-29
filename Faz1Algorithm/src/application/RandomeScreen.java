package application;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import java.util.Arrays;
import java.util.Random;

public class RandomeScreen {
	TextField numberOfCoinsTextField, minRangeField, maxRangeField;
	Text text;
	VBox vBox;

	static Scene randomScene;
	Button backButton, startButton;
	private int[] numbersList = new int[0];
	GameTowPlayer gameScene = new GameTowPlayer();
	GameTheComputer computer = new GameTheComputer();

//...................................................................................................
	public Scene getScene() {
		return randomScene;
	}
//...................................................................................................

	public int inputFieldValue() {
		try {
			String input = numberOfCoinsTextField.getText().trim();
			if (input.isEmpty()) {
				showError("Please enter a value.");
				return -1;
			}
			int numberOfCoins = Integer.parseInt(input);
			if (numberOfCoins <= 0) {
				showError("The number of coins must be greater than zero.");
				return -1;
			}
			return numberOfCoins;
		} catch (NumberFormatException e) {
			showError("Please enter a valid integer.");
			return -1;
		}
	}

//...................................................................................................

	public void initializeRandomScen(Stage primaryStage) {
		vBox = new VBox(30);

		text = new Text("Enter the number of coins and range to generate:");
		text.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");

		numberOfCoinsTextField = new TextField();
		numberOfCoinsTextField.setPromptText("Enter the number of coins");

		minRangeField = new TextField();
		minRangeField.setPromptText("Enter minimum range (e.g., 50)");

		maxRangeField = new TextField();
		maxRangeField.setPromptText("Enter maximum range (e.g., 100)");

		VBox box = new VBox(30);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(numberOfCoinsTextField, minRangeField, maxRangeField);

		Button saveButton = new Button("Save Values");
		setButtonStyle(saveButton);
		// .........................................................
		backButton = new Button("Back and again");
		setButtonStyle(backButton);

		saveButton.setOnAction(e -> saveValues());
		// .........................................................
		startButton = new Button("Start The Game");
		startButton.setVisible(false);
		setButtonStyle(startButton);

		// .........................................................

		startButton.setOnAction(e -> {
			if (numberOfCoinsTextField.getText().isEmpty() || minRangeField.getText().isEmpty()
					|| maxRangeField.getText().isEmpty()) {
				showError("Please enter all data");
				return;
			}

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
		// .........................................................

		backButton.setOnAction(e -> {
			box.getChildren().clear();
			vBox.getChildren().clear();
			primaryStage.setScene(OpeneScene.mainScene);
		});
		// .........................................................

		vBox.getChildren().addAll(text, box, saveButton, startButton, backButton);
		vBox.setAlignment(Pos.CENTER);
		vBox.setStyle("-fx-background-color: lightblue;");
		randomScene = new Scene(vBox, 700, 700);
		primaryStage.setScene(randomScene);
	}
//...................................................................................................

	private void saveValues() {
		try {
			int numberOfCoins = Integer.parseInt(numberOfCoinsTextField.getText().trim());
			int minRange = Integer.parseInt(minRangeField.getText().trim());
			int maxRange = Integer.parseInt(maxRangeField.getText().trim());

			if (minRange >= maxRange) {
				showError("The minimum range must be less than the maximum range.");
				return;
			}
			if (numberOfCoins <= 0) {
				showError("The number of coins must be greater than zero.");
				return;
			}

			//  random numbers
			numbersList = new int[numberOfCoins];
			Random random = new Random();
			for (int i = 0; i < numberOfCoins; i++) {
				numbersList[i] = random.nextInt(maxRange - minRange + 1) + minRange;
			}
			System.out.println("Random coins: " + Arrays.toString(numbersList));
			startButton.setVisible(true);
		} catch (NumberFormatException e) {
			showError("Please enter valid numbers.");
		}
	}
//...................................................................................................

	private void showError(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(" Eroor");
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}
//...................................................................................................

	private void setButtonStyle(Button button) {
		button.setMinWidth(Button.USE_PREF_SIZE);
		button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
		button.setPrefWidth(150);
	}
}
