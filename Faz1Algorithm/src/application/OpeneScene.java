package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class OpeneScene extends Application {

	private VBox maaineSceneVBox;
	static Scene mainScene;
	static Button chooseFileButton, choosManually, chooseRandomly;
	static CheckBox towPlayer, Computer;

	static ChoosManuallyScene choosManuallyScene = new ChoosManuallyScene();
	static RandomeScreen randomeScreen = new RandomeScreen();
	static ChoseFromfile choseFromfile = new ChoseFromfile();

	private void initializeMainScene(Stage primaryStage) {
		Text chooseText = new Text("Please enter the way you want to play:");
		chooseText.setFont(Font.font("Arial", 20));
		chooseText.setStyle("-fx-font-weight: bold;");
		chooseText.setFill(Color.BLACK);

		// Initialize buttons
		chooseFileButton = new Button("Choose The Number from file");
		setButtonStyle(chooseFileButton);
		chooseFileButton.setMinWidth(Button.USE_PREF_SIZE);

		choosManually = new Button("Choose Take Number Manually");
		setButtonStyle(choosManually);
		choosManually.setMinWidth(Button.USE_PREF_SIZE);

		chooseRandomly = new Button("Choose The Number Randomly");
		setButtonStyle(chooseRandomly);
		chooseRandomly.setMinWidth(Button.USE_PREF_SIZE);

		// Initialize CheckBoxes
		towPlayer = new CheckBox("Two Player");
		towPlayer.setStyle("-fx-font-weight: bold;");

		Computer = new CheckBox("The Computer");
		Computer.setStyle("-fx-font-weight: bold;");

		// Make sure only one CheckBox is selected
		towPlayer.setOnAction(e -> {
			if (towPlayer.isSelected()) {
				Computer.setSelected(false); // Deselect the other option
			}
		});

		Computer.setOnAction(e -> {
			if (Computer.isSelected()) {
				towPlayer.setSelected(false); // Deselect the other option
			}
		});

		// Layout for checkboxes
		HBox checHBox = new HBox(30);
		checHBox.setAlignment(Pos.CENTER);
		checHBox.getChildren().addAll(towPlayer, Computer);

		// Button actions
		chooseFileButton.setOnAction(e -> {
			if (isCheckboxSelected()) {
				choseFromfile.initializeChooseFromFile(primaryStage);
				primaryStage.setScene(choseFromfile.getScene());
			} else {
				showAlert("Please select either 'Two Player' or 'The Computer'.");
			}
		});

		choosManually.setOnAction(e -> {
			// Check if a checkbox is selected before proceeding
			if (isCheckboxSelected()) {
				choosManuallyScene.initializechoosManually(primaryStage);
				primaryStage.setScene(choosManuallyScene.getScene());

			} else {
				showAlert("Please select either 'Two Player' or 'The Computer'.");
			}
		});

		chooseRandomly.setOnAction(e -> {
			if (isCheckboxSelected()) {

				randomeScreen.initializeRandomScen(primaryStage);
				primaryStage.setScene(randomeScreen.getScene());
			} else {
				showAlert("Please select either 'Two Player' or 'The Computer'.");
			}
		});

		// Main layout
		maaineSceneVBox = new VBox(30);
		maaineSceneVBox.setStyle("-fx-background-color: lightblue;");
		maaineSceneVBox.setAlignment(Pos.CENTER);
		maaineSceneVBox.getChildren().addAll(chooseText, checHBox, chooseFileButton, chooseRandomly, choosManually);

		mainScene = new Scene(maaineSceneVBox, 500, 500);
	}

	// Method to check if any checkbox is selected
	private boolean isCheckboxSelected() {
		return towPlayer.isSelected() || Computer.isSelected();
	}

	// Method to show an alert if no checkbox is selected
	private void showAlert(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Selection Required");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	// Method to set button style
	private void setButtonStyle(Button button) {
		button.setMinWidth(Button.USE_PREF_SIZE);
		button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
	}

	static public Scene getScene() {
		return mainScene;
	}

	@Override
	public void start(Stage primaryStage) {
		initializeMainScene(primaryStage);
		primaryStage.setTitle("Game Setup");
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
