package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameTheComputer {
	private int[] numbers;
	public int[] player1Numbers = new int[0];
	public int[] player2Numbers = new int[0];
	public int player1Score = 0;
	public int player2Score = 0;
	private boolean player1Turn = true;
	private String player1Name = "Player 1";
	private String player2Name = "Player 2";
	private Label player1ScoreLabel;
	private Label player2ScoreLabel;
	private Label currentPlayerLabel;
	private HBox numbersBox;
	private VBox gameScreenBox;
	private VBox player1Box;
	private VBox player2Box;
	private Color player1Color = Color.BLUE;
	private Color player2Color = Color.RED;
	static Scene gameScene;
	private Timeline gameTimeline;
	PrantTable prantTable = new PrantTable();

	public void initializeGameScene(Stage primaryStage, int[] numbersArray) {
		numbers = numbersArray;

		// Static names and colors
		player1Name = "Player 1";
		player2Name = "Player 2";

		// Game screen setup
		player1ScoreLabel = new Label(player1Name + "\nScore: " + player1Score);
		player2ScoreLabel = new Label(player2Name + "\nScore: " + player2Score);

		currentPlayerLabel = new Label("Current Player: " + player1Name);
		currentPlayerLabel.setFont(javafx.scene.text.Font.font("Arial", 24));
		currentPlayerLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: black;");

		numbersBox = new HBox(30);
		numbersBox.setAlignment(Pos.CENTER);
		updateNumbersDisplay();

		// Start button setup
		Button startButton = new Button("Start");
		setButtonStyle(startButton);
		startButton.setOnAction(e -> startGame());

		// Button to print the dp table
		Button printDpButton = new Button("Print DP Table");
		setButtonStyle(printDpButton);
		printDpButton.setOnAction(e -> {
			prantTable.setGameTheComputer(this); // Pass the current game instance to prantTable
			if (primaryStage != null) {
				prantTable.initializePrintScene(primaryStage, numbersArray); // Initialize and display the print scene
				primaryStage.setScene(prantTable.getGameScene()); // Change to the print scene
			} else {
				System.out.println("PrantTable is not initialized!");
			}
		});

		// Back button setup
		Button backButton = new Button("Back");
		setButtonStyle(backButton);
		backButton.setOnAction(e -> {
			player1Score = 0;
			player2Score = 0;
			player1Numbers = new int[0];
			player2Numbers = new int[0];
			numbers = new int[] {};
			player1Turn = true;
			primaryStage.setScene(OpeneScene.getScene());
		});

		// Put the buttons in a VBox so the Back button can be placed below the Start
		// button
		VBox buttonBox = new VBox(20, startButton, printDpButton, backButton); // Add the printDpButton here
		buttonBox.setAlignment(Pos.CENTER);

		player1Box = new VBox(10, new Label(player1Name + " Selected Numbers"));
		player1Box.setAlignment(Pos.CENTER);
		player1Box.setStyle("-fx-border-color: blue; -fx-padding: 10px;");

		player2Box = new VBox(10, new Label(player2Name + " Selected Numbers"));
		player2Box.setAlignment(Pos.CENTER);
		player2Box.setStyle("-fx-border-color: red; -fx-padding: 10px;");

		VBox player1ScoreBox = new VBox(30, player1ScoreLabel, player1Box);
		player1ScoreBox.setAlignment(Pos.CENTER);

		VBox player2ScoreBox = new VBox(30, player2ScoreLabel, player2Box);
		player2ScoreBox.setAlignment(Pos.CENTER);

		HBox scoresBox = new HBox(40, player1ScoreBox, player2ScoreBox);
		scoresBox.setAlignment(Pos.CENTER);

		// Assemble the game screen
		gameScreenBox = new VBox(30, numbersBox, scoresBox, currentPlayerLabel, buttonBox);
		gameScreenBox.setAlignment(Pos.CENTER);
		gameScreenBox.setStyle("-fx-background-color: lightblue;");

		// Set up and display the scene
		gameScene = new Scene(gameScreenBox, 700,700);
		primaryStage.setTitle("Number Selection Game");
		primaryStage.setScene(gameScene);
	}

	

	private void setButtonStyle(Button button) {
		button.setMinWidth(Button.USE_PREF_SIZE);
		button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
		button.setPrefWidth(150);
	}

	private void updateNumbersDisplay() {
		numbersBox.getChildren().clear();
		for (int number : numbers) {
			Circle circle = new Circle(30);
			circle.setFill(Color.WHITE);
			circle.setStroke(Color.BLACK);
			circle.setStrokeWidth(2);

			Label label = new Label(String.valueOf(number));
			label.setStyle("-fx-font-size: 18; -fx-text-fill: black;");
			StackPane stackPane = new StackPane();
			stackPane.getChildren().addAll(circle, label);
			numbersBox.getChildren().add(stackPane);
		}
	}
	

	private void startGame() {
		// Hide the start button and other UI components
		numbersBox.setVisible(true);

		// Setup a timeline to simulate the game flow dynamically
		KeyFrame time = new KeyFrame(Duration.seconds(1), e -> chooseNumber());
		gameTimeline = new Timeline(time);
		gameTimeline.setCycleCount(Timeline.INDEFINITE);
		gameTimeline.play();
	}

	private void chooseNumber() {
		if (numbers.length == 0) {
			gameTimeline.stop();
			declareWinner();
			return;
		}

		int optimalChoice = optimalStrategy(numbers);

		if (player1Turn) {
			player1Numbers = appendToArray(player1Numbers, optimalChoice);
			player1Score += optimalChoice;
			player1ScoreLabel.setText(player1Name + "\nScore: " + player1Score);
			addNumberToPlayerBox(player1Box, optimalChoice, player1Color);
		} else {
			player2Numbers = appendToArray(player2Numbers, optimalChoice);
			player2Score += optimalChoice;
			player2ScoreLabel.setText(player2Name + "\nScore: " + player2Score);
			addNumberToPlayerBox(player2Box, optimalChoice, player2Color);
		}

		numbers = removeFromArray(numbers, optimalChoice);
		updateNumbersDisplay();

		player1Turn = !player1Turn;
		currentPlayerLabel.setText("Current Player: " + (player1Turn ? player1Name : player2Name));
		currentPlayerLabel.setTextFill(player1Turn ? player1Color : player2Color);

		if (numbers.length == 0) {
			declareWinner();
		}
	}

	private void addNumberToPlayerBox(VBox playerBox, int number, Color color) {
		Circle circle = new Circle(20);
		circle.setFill(color);
		Label label = new Label(String.valueOf(number));
		label.setTextFill(Color.WHITE);
		StackPane stackPane = new StackPane(circle, label);
		playerBox.getChildren().add(stackPane);
	}

	private int[] appendToArray(int[] array, int element) {
		int[] newArray = new int[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = element;
		return newArray;
	}

	private int[] removeFromArray(int[] array, int element) {
		int[] newArray = new int[array.length - 1];
		int index = 0;
		boolean elementRemoved = false;

		for (int i = 0; i < array.length; i++) {
			if (array[i] == element && !elementRemoved) {
				// Remove the first instance of the element
				elementRemoved = true;
				continue; // Skip this element in the new array
			}
			newArray[index++] = array[i];
		}
		return newArray;
	}

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

	// Optimal strategy to get the best choice
	// أفضل استراتيجية لاختيار الرقم الأمثل
	private int optimalStrategy(int[] arr) {
		int n = arr.length;
		int[][] dp = new int[n][n];

		// ملء جدول البرمجة الديناميكية
		for (int gap = 0; gap < n; gap++) {
			for (int i = 0, j = gap; j < n; i++, j++) {
				int x = (i + 2 <= j) ? dp[i + 2][j] : 0;
				int y = (i + 1 <= j - 1) ? dp[i + 1][j - 1] : 0;
				int z = (i <= j - 2) ? dp[i][j - 2] : 0;

				dp[i][j] = Math.max(arr[i] + Math.min(x, y), // اختر الرقم الأول
						arr[j] + Math.min(y, z) // اختر الرقم الأخير
				);
			}
		}

		// العودة إلى الخيار الأمثل بناءً على الحسابات
		int i = 0, j = n - 1;
		if (arr[i] + Math.min((i + 2 <= j ? dp[i + 2][j] : 0), (i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0)) > arr[j]
				+ Math.min((i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0), (i <= j - 2 ? dp[i][j - 2] : 0))) {
			return arr[i];
		} else {
			return arr[j];
		}
	}

	public Scene getScene() {
		return gameScene;
	}
}
