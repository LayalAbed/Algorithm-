package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameTowPlayer {
	private int[] numbers;
	private int[] player1Numbers = new int[0];
	private int[] player2Numbers = new int[0];
	private int player1Score = 0;
	private int player2Score = 0;
	private boolean player1Turn = true;
	private String player1Name = "Player 1";
	private String player2Name = "Player 2";
	private Label player1ScoreLabel;
	private Label player2ScoreLabel;
	private Label currentPlayerLabel;
	private HBox numbersBox;
	private HBox scoresBox;
	private VBox gameScreenBox;
	private RadioButton player1RadioButton, player2RadioButton;
	private ToggleGroup toggleGroup;
	private Color player1Color = Color.BLUE;
	private Color player2Color = Color.RED;
	private ColorPicker colorPicker1, colorPicker2;
	static Scene gameScene;
//...................................................................................................

	public void initializeGameScene(Stage primaryStage, int[] numbersArray) {
		numbers = numbersArray;
		Text text = new Text("Please Enter The Name And Choose The Color");
		text.setStyle("-fx-font-size: 18px; -fx-text-fill: #000000;");

		// Text fields for player names and color pickers
		TextField player1NameField = new TextField();
		player1NameField.setPromptText("Enter Player 1 Name");
		player1NameField.setPrefWidth(150);

		TextField player2NameField = new TextField();
		player2NameField.setPromptText("Enter Player 2 Name");
		player2NameField.setPrefWidth(150);

		colorPicker1 = new ColorPicker(player1Color);
		colorPicker1.setOnAction(e -> player1Color = colorPicker1.getValue());

		colorPicker2 = new ColorPicker(player2Color);
		colorPicker2.setOnAction(e -> player2Color = colorPicker2.getValue());

		HBox player1Box = new HBox(10, player1NameField, colorPicker1);
		player1Box.setAlignment(Pos.CENTER);

		HBox player2Box = new HBox(10, player2NameField, colorPicker2);
		player2Box.setAlignment(Pos.CENTER);

		// Choosing which player starts
		toggleGroup = new ToggleGroup();
		player1RadioButton = new RadioButton("Player 1 starts");
		player1RadioButton.setToggleGroup(toggleGroup);
		player1RadioButton.setSelected(true);

		player2RadioButton = new RadioButton("Player 2 starts");
		player2RadioButton.setToggleGroup(toggleGroup);

		Button startButton = new Button("Start");
		setButtonStyle(startButton);
		// ...................................................................................................

		startButton.setOnAction(e -> {
			if (player1NameField.getText().isEmpty()) {
				player1Name = "Player 1";
			} else {
				player1Name = player1NameField.getText();
			}

			if (player2NameField.getText().isEmpty()) {
				player2Name = "Player 2";
			} else {
				player2Name = player2NameField.getText();
			}

			if (player2RadioButton.isSelected()) {
				player1Turn = false;
			}
			showGameScreen(primaryStage);
		});
		// ...................................................................................................

		VBox playerSelectionBox = new VBox(30, text, player1Box, player2Box, player1RadioButton, player2RadioButton,
				startButton);
		playerSelectionBox.setStyle("-fx-background-color: lightblue;");
		playerSelectionBox.setAlignment(Pos.CENTER);
		// ...................................................................................................

		// Game screen
		player1ScoreLabel = new Label(player1Name + " Score: " + player1Score);
		player2ScoreLabel = new Label(player2Name + " Score: " + player2Score);

		currentPlayerLabel = new Label("Current Player: " + player1Name);
		currentPlayerLabel.setFont(javafx.scene.text.Font.font("Arial", 24));
		currentPlayerLabel.setStyle("-fx-font-size: 24px;");

		numbersBox = new HBox(30);
		numbersBox.setAlignment(Pos.CENTER);
		updateNumbersDisplay();

		Button chooseStartButton = new Button("Choose Start");
		setButtonStyle(chooseStartButton);
		chooseStartButton.setOnAction(e -> chooseNumber(true));
		// ...................................................................................................

		Button chooseEndButton = new Button("Choose End");
		setButtonStyle(chooseEndButton);
		chooseEndButton.setOnAction(e -> chooseNumber(false));
		// ...................................................................................................

		HBox buttonBox = new HBox(30, chooseStartButton, chooseEndButton);
		buttonBox.setAlignment(Pos.CENTER);

		Button backButton = new Button("Back");
		setButtonStyle(backButton);
		backButton.setOnAction(e -> {
			player1Score = 0;
			player2Score = 0;
			player1Numbers = new int[0];
			player2Numbers = new int[0];
			primaryStage.setScene(OpeneScene.getScene());
		});
		// ...................................................................................................

		scoresBox = new HBox(40);
		scoresBox.setAlignment(Pos.CENTER);

		VBox player1ScoreBox = new VBox(30, new Label(player1Name), player1ScoreLabel);
		player1ScoreBox.setAlignment(Pos.CENTER);
		player1ScoreBox.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");

		VBox player2ScoreBox = new VBox(30, new Label(player2Name), player2ScoreLabel);
		player2ScoreBox.setAlignment(Pos.CENTER);
		player2ScoreBox.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");

		scoresBox.getChildren().addAll(player1ScoreBox, player2ScoreBox);

		gameScreenBox = new VBox(30, numbersBox, scoresBox, currentPlayerLabel, buttonBox, backButton);
		gameScreenBox.setAlignment(Pos.CENTER);
		gameScreenBox.setStyle("-fx-background-color: lightblue;");

		gameScene = new Scene(playerSelectionBox, 500, 500);
		primaryStage.setTitle("Number Selection Game");
		primaryStage.setScene(gameScene);
	}

//...................................................................................................

	private void showGameScreen(Stage primaryStage) {
		if (player1RadioButton.isSelected()) {
			player1Turn = true;
		} else {
			player1Turn = false;
		}

		if (player1Turn) {
			currentPlayerLabel.setText("Current Player: " + player1Name);
			currentPlayerLabel.setTextFill(player1Color);
		} else {
			currentPlayerLabel.setText("Current Player: " + player2Name);
			currentPlayerLabel.setTextFill(player2Color);
		}

		player1ScoreLabel.setText(player1Name + "\nScore: " + player1Score);
		player2ScoreLabel.setText(player2Name + "\nScore: " + player2Score);

		updateNumbersDisplay();
		gameScene.setRoot(gameScreenBox);
		primaryStage.setScene(gameScene);
	}
//...................................................................................................

	private void updateNumbersDisplay() {
		numbersBox.getChildren().clear();
		for (int number : numbers) {
			javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(30);
			circle.setFill(Color.WHITE);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(2);

			Label label = new Label(String.valueOf(number));
			label.setStyle("-fx-font-size: 18; -fx-text-fill: black;");
			StackPane stackPane = new StackPane();
			stackPane.getChildren().addAll(circle, label);
			numbersBox.getChildren().add(stackPane);
		}

		// Display chosen numbers for each player under their score label
		HBox player1ChosenNumbersBox = new HBox(10);
		player1ChosenNumbersBox.setAlignment(Pos.CENTER);
		for (int number : player1Numbers) {
			javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(20);
			circle.setFill(player1Color);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(2);

			Label label = new Label(String.valueOf(number));
			label.setStyle("-fx-font-size: 14; -fx-text-fill: white;");

			StackPane stackPane = new StackPane();
			stackPane.getChildren().addAll(circle, label);
			player1ChosenNumbersBox.getChildren().add(stackPane);
		}
		player1ScoreLabel.setGraphic(player1ChosenNumbersBox);

		HBox player2ChosenNumbersBox = new HBox(10);
		player2ChosenNumbersBox.setAlignment(Pos.CENTER);
		for (int number : player2Numbers) {
			javafx.scene.shape.Circle circle = new javafx.scene.shape.Circle(20);
			circle.setFill(player2Color);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(2);

			Label label = new Label(String.valueOf(number));
			label.setStyle("-fx-font-size: 14; -fx-text-fill: white;");

			StackPane stackPane = new StackPane();
			stackPane.getChildren().addAll(circle, label);
			player2ChosenNumbersBox.getChildren().add(stackPane);
		}
		player2ScoreLabel.setGraphic(player2ChosenNumbersBox);
	}
//...................................................................................................

	private void chooseNumber(boolean isStart) {
		if (numbers.length == 0) {
			declareWinner();
			return; // No numbers left to choose
		}

		int chosenNumber;
		if (isStart) {
			chosenNumber = numbers[0];
			numbers = removeElementFromStart(numbers);
		} else {
			chosenNumber = numbers[numbers.length - 1];
			numbers = removeElementFromEnd(numbers);
		}

		if (player1Turn) {
			player1Numbers = addElementToArray(player1Numbers, chosenNumber);
			player1Score += chosenNumber;
			player1ScoreLabel.setText(player1Name + "\n Score: " + player1Score);
		} else {
			player2Numbers = addElementToArray(player2Numbers, chosenNumber);
			player2Score += chosenNumber;
			player2ScoreLabel.setText(player2Name + "\n Score: " + player2Score);

		}

		player1Turn = !player1Turn;

		if (player1Turn) {
		    currentPlayerLabel.setText("Current Player: " + player1Name);
		    currentPlayerLabel.setTextFill(player1Color);
		} else {
		    currentPlayerLabel.setText("Current Player: " + player2Name);
		    currentPlayerLabel.setTextFill(player2Color);
		}


		updateNumbersDisplay();
		if (numbers.length == 0) {
			declareWinner();
		}
	}
//...................................................................................................

	private int[] removeElementFromStart(int[] array) {
		int[] newArray = new int[array.length - 1];
		System.arraycopy(array, 1, newArray, 0, array.length - 1);
		return newArray;
	}
//...................................................................................................

	private int[] removeElementFromEnd(int[] array) {
		int[] newArray = new int[array.length - 1];
		System.arraycopy(array, 0, newArray, 0, array.length - 1);
		return newArray;
	}
//...................................................................................................

	private int[] addElementToArray(int[] array, int value) {
	    int[] newArray = new int[array.length + 1];
	    System.arraycopy(array, 0, newArray, 0, array.length);
	    newArray[array.length] = value;
	    return newArray;
	}

//...................................................................................................

	private void declareWinner() {
		String result;
		if (player1Score > player2Score) {
			result = player1Name + " wins!";
			currentPlayerLabel.setTextFill(player1Color);
		} else if (player2Score > player1Score) {
			result = player2Name + " wins!";
			currentPlayerLabel.setTextFill(player2Color);
		} else {
			result = "It's a tie!";
			currentPlayerLabel.setTextFill(Color.BLACK);
		}
		currentPlayerLabel.setText(result);
	}
//...................................................................................................

	public Scene getGameScene() {
		return gameScene;
	}
//...................................................................................................

	private void setButtonStyle(Button button) {
		button.setMinWidth(Button.USE_PREF_SIZE);
		button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
		button.setPrefWidth(150);
	}
}
