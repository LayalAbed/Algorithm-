package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PrantTable {
	private int[] numbers; // Array of numbers representing the input
	private GameTheComputer computer; // GameTheComputer object to access the players' data
	private Scene scene; // Scene for the print DP table screen
	private VBox box; // VBox container to hold all UI components
//...................................................................................................

	// Method to set the GameTheComputer object
	public void setGameTheComputer(GameTheComputer computer) {
		this.computer = computer;
	}
//...................................................................................................

	// Method to initialize the print DP table scene
	public void initializePrintScene(Stage primaryStage, int[] numbersArray) {
		numbers = numbersArray; // Initialize the numbers array with the passed values

		// Create a VBox layout with spacing of 30 between elements
		box = new VBox(30);
		box.setAlignment(Pos.CENTER); // Center the elements in the VBox
		box.setStyle("-fx-background-color: lightblue;"); // Set background color to light blue

		// Text label for the DP table
		Text text = new Text("Print DP Table");

		// Back button to return to the game scene
		Button backButton = new Button("Back to the Game Scene");

		// Calculate the DP table using the numbers array
		int[][] dpTable = calculateOptimalStrategy(numbers);

		// Create a GridPane to display the DP table
		GridPane gridPane = createDpGrid(dpTable);

		// Create text to display the player numbers
		Text player1NumbersText = new Text("Player 1 Numbers: " + arrayToString(computer.player1Numbers));
		Text player2NumbersText = new Text("Player 2 Numbers: " + arrayToString(computer.player2Numbers));

		// Set colors for the player numbers text
		player1NumbersText.setFill(Color.RED);
		player2NumbersText.setFill(Color.BLUE);

		// Add the components (text, DP grid, player numbers, back button) to the VBox
		box.getChildren().addAll(text, gridPane, player1NumbersText, player2NumbersText, backButton);

		// Style the back button
		setButtonStyle(backButton);

		// Back button action to reset scores and return to the game scene
		backButton.setOnAction(e -> {
			computer.player1Score = 0;
			computer.player2Score = 0;
			computer.player1Numbers = new int[0]; // Reset player 1 numbers
			computer.player2Numbers = new int[0]; // Reset player 2 numbers

			if (computer == null) {
				System.out.println("Error");
			} else {
				computer.initializeGameScene(primaryStage, numbersArray); // Initialize the game scene
				primaryStage.setScene(computer.getScene()); // Set the game scene
			}
		});
		// ..........................................................................

		// Set the scene and show it in the primary stage
		scene = new Scene(box, 600, 600); // Adjust the size for better viewing of the table
		primaryStage.setScene(scene);
	}

//...................................................................................................
	// Method to calculate the DP table based on the input numbers array
	public int[][] calculateOptimalStrategy(int[] arr) {
	    // Return an empty table if the input array is empty
	    if (arr.length == 0) {
	        return new int[0][0];
	    }

	    int n = arr.length;
	    int[][] dpTable = new int[n][n]; // DP table to store optimal values for subarrays

	    // Loop through all subarray sizes (gaps) and their start and end indices
	    for (int Sub = 0; Sub < n; Sub++) {
	        for (int startIndex = 0, endIndex = Sub; endIndex < n; startIndex++, endIndex++) {
	            int option1 = 0, option2 = 0, option3 = 0; // Temporary variables for the options

	            // Calculate options based on subarray length and indices
	            if (startIndex + 2 <= endIndex) {
	                option1 = dpTable[startIndex + 2][endIndex]; // Option 1: choose arr[startIndex]
	            }
	            if (startIndex + 1 <= endIndex - 1) {
	                option2 = dpTable[startIndex + 1][endIndex - 1]; // Option 2: choose arr[startIndex + 1] and arr[endIndex - 1]
	            }
	            if (startIndex <= endIndex - 2) {
	                option3 = dpTable[startIndex][endIndex - 2]; // Option 3: choose arr[endIndex]
	            }

	            // Store the optimal value for the current subarray
	            dpTable[startIndex][endIndex] = Math.max(
	                arr[startIndex] + Math.min(option1, option2),
	                arr[endIndex] + Math.min(option2, option3)
	            );
	        }
	    }

	    return dpTable; // Return the filled DP table
	}

//...................................................................................................

	// Method to create a GridPane to display the DP table
	private GridPane createDpGrid(int[][] dp) {
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10); // Vertical gap between cells
		gridPane.setHgap(10); // Horizontal gap between cells
		gridPane.setAlignment(Pos.CENTER); // Align the grid in the center

		// Loop through the DP table and add Text nodes to the grid
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j < dp[i].length; j++) {
				Text text = new Text(String.valueOf(dp[i][j]));
				text.setFill(Color.WHITE); // Set text color to white

				// Set the background color based on the value in the DP table
				if (dp[i][j] > 0) {
					gridPane.add(createColoredCell(text, Color.CORNFLOWERBLUE), j, i); // Blue for positive values
				} else {
					gridPane.add(createColoredCell(text, Color.LIGHTGREY), j, i); // Grey for non-positive values
				}
			}
		}

		return gridPane;
	}
//...................................................................................................

	// Method to create a colored cell for the DP table
	private VBox createColoredCell(Text text, Color color) {
		VBox cell = new VBox();
		cell.setStyle("-fx-background-color: " + colorToHex(color) + "; " + "-fx-padding: 10; "
				+ "-fx-border-color: black; " + // Add black border
				"-fx-border-width: 2px;"); // Set border width
		cell.getChildren().add(text);
		return cell;
	}
//...................................................................................................

	// Helper method to convert Color to hex string
	private String colorToHex(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));
	}
//...................................................................................................

	// Method to return the current scene
	public Scene getGameScene() {
		return scene;
	}
//...................................................................................................

	// Method to style the back button
	private void setButtonStyle(Button button) {
		button.setMinWidth(Button.USE_PREF_SIZE); // Set button width to preferred size
		button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;"); // Add black border around button
	}
//...................................................................................................

	// Helper method to convert an int array to a string for display
	private String arrayToString(int[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(", "); // Add comma between numbers
			}
		}
		return sb.toString(); // Return the array as a string
	}
}
