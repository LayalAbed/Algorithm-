package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChoseFromfile {

	private int[] numbersList = new int[0]; 

	static Scene FileScene;
	VBox box;
	GameTowPlayer game = new GameTowPlayer();
	GameTheComputer computer = new GameTheComputer();

	public Scene getScene() {
		return FileScene;
	}


	private void readDataFromFileAndAddToList(File file, Stage primaryStage) throws IOException {
	    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
	        String firstLine = br.readLine();
	        if (firstLine == null || firstLine.trim().isEmpty()) {
	            System.out.println("Error: First line is empty or invalid.");
	            return;
	        }

	        int countSize;
	        try {
	            countSize = Integer.parseInt(firstLine.trim());
	            if (countSize <= 0 || countSize % 2 != 0) {
	            	showError("the number of coins must be even ");
	                System.out.println("Error: First line must be a positive even number.");
	                return;
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Error: Invalid number format in the first line.");
	            showError("Error: Invalid number format in the first line.");
	            return;
	        }

	        int count = 0;
	        String line;
	        numbersList = new int[countSize]; // Resize the array to the exact count

	        while ((line = br.readLine()) != null) {
	            line = line.trim();
	            if (line.isEmpty()) {
	                continue;
	            }

	            try {
	                int countValue = Integer.parseInt(line);
	                if (count < countSize) {
	                    numbersList[count] = countValue;
	                    count++;
	                } else {
	                    break; // Stop reading if we have reached the required count
	                }
	            } catch (NumberFormatException e) {
	            	showError("Error: Invalid number format in the first line."+ line);
	                System.out.println("Error: Invalid number format on line: " + line);
	                return;
	            }
	        }

	        if (count != countSize) {
	        	showError(" the file dos not contaneenaf data");
	            System.out.println("Error: The file contains fewer valid numbers (" + count
	                    + ") than the expected count (" + countSize + ").");
	            numbersList = new int[0]; // Reset the array
	            return;
	        }

	        System.out.println("Successfully read " + count + " numbers.");
	        for (int num : numbersList) {
	            System.out.println(num);
	        }

	    } catch (IOException e) {
        	showError("Error reading file: " + e.getMessage());
	        System.out.println("Error reading file: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	public void initializeChooseFromFile(Stage primaryStage) {
	    // Create button and label for file selection
	    Button openButton = new Button("Choose the file");
	    setButtonStyle(openButton);
	    Label label = new Label("Choose the file");
	    label.setStyle("-fx-font-size: 24px; -fx-text-fill: #000000;");

	    // Set up the button action to open a file chooser
	    openButton.setOnAction(e -> {
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	        fileChooser.setTitle("Open Resource File");
	        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

	        File file = fileChooser.showOpenDialog(primaryStage); // Open file chooser
	        if (file != null) {
	            try {
	                readDataFromFileAndAddToList(file, primaryStage); // Read data from the selected file

	                // Check if numbersList is empty
	                if (numbersList.length == 0) {
	                    System.out.println("The file is invalid or empty. Returning to the main scene.");
	                    primaryStage.setScene(OpeneScene.getScene());
	                } else {
	                    // Determine the game mode and initialize the appropriate scene
	                    if (OpeneScene.towPlayer.isSelected()) {
	                        game.initializeGameScene(primaryStage, numbersList);
	                        primaryStage.setScene(game.getGameScene());
	                    } else  {
	                        computer.initializeGameScene(primaryStage, numbersList);
	                        primaryStage.setScene(computer.getScene());
	                    }
	                }
	            } catch (IOException e1) {
	                System.out.println("Error reading file: " + e1.getMessage());
	                e1.printStackTrace();
	            }
	        } else {
	            System.out.println("File selection canceled.");
	        }
	    });

	    // Set up the layout for the file chooser scene
	    box = new VBox(30);
	    box.setAlignment(Pos.CENTER);
	    box.getChildren().addAll(label, openButton);
	    box.setStyle("-fx-background-color: #ADD8E6;");

	    FileScene = new Scene(box, 500, 500); // Create and assign the scene
	}
	private void showError(String errorMessage) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(" Eroor");
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

	private void setButtonStyle(Button button) {
		button.setMinWidth(Button.USE_PREF_SIZE);
		button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
		button.setPrefWidth(150);
	}
}
