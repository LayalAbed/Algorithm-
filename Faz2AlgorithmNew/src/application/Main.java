package application;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

    	String backgroundColorStyle = "-fx-background-color: #FFD1DC;";
//    	String borderStyle = "-fx-border-color: black; -fx-border-width: 1px;";

        BorderPane pane = new BorderPane();
        pane.setStyle(backgroundColorStyle);

        TabPane tabPane = new TabPane();

        // Compression Tab
        VBox compressBox = new VBox(10);
        compressBox.setAlignment(Pos.CENTER);

        Label label = new Label("Compress File");
        label.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 20));

        Button compress = new Button("Compress");
        compress.setMinWidth(150); // Increased size of the button
        compress.setMinHeight(40); // Increased size of the button
        compress.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        Button headerButton = new Button("Header");
        headerButton.setMinWidth(150); // Increased size of the button
        headerButton.setMinHeight(40); // Increased size of the button
        headerButton.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        Button statisticComButton = new Button("Statistic Comp");
        statisticComButton.setMinWidth(150); // Increased size of the button
        statisticComButton.setMinHeight(40); // Increased size of the button
        statisticComButton.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        TextArea compressTextArea = new TextArea();
        compressTextArea.setPrefSize(400, 250); // Adjusted size for the text area
        compressTextArea.setStyle("-fx-border-color: black;");

        // Create VBox for buttons on the right side
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(compress, headerButton, statisticComButton);

        // Create VBox for TextArea on the left side
        VBox textAreaBox = new VBox();
        textAreaBox.setAlignment(Pos.CENTER_LEFT);
        textAreaBox.getChildren().add(compressTextArea);

        // Create HBox to separate TextArea and Buttons
        HBox centerBox = new HBox(10);
        centerBox.getChildren().addAll(textAreaBox, buttonBox);
        centerBox.setAlignment(Pos.CENTER);

        compressBox.getChildren().addAll(label, centerBox);
        Tab compressTab = new Tab("Compression", compressBox);

        // Decompression Tab
        VBox decompressBox = new VBox(10);
        decompressBox.setAlignment(Pos.CENTER);

        Label decompressLabel = new Label("Decompress File");
        decompressLabel.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 20));

        Button decompress = new Button("Decompress");
        

        decompress.setMinWidth(150); // Increased size of the button
        decompress.setMinHeight(40); // Increased size of the button
        decompress.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        Button headerButtonDecompress = new Button("Header");
        headerButtonDecompress.setMinWidth(150); // Increased size of the button
        headerButtonDecompress.setMinHeight(40); // Increased size of the button
        headerButtonDecompress.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        Button statisticDEComButton = new Button("Statistic DEComp");
        statisticDEComButton.setMinWidth(150); // Increased size of the button
        statisticDEComButton.setMinHeight(40); // Increased size of the button
        statisticDEComButton.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");

        TextArea decompressTextArea = new TextArea();
        decompressTextArea.setPrefSize(400, 250); // Adjusted size for the text area
        decompressTextArea.setStyle("-fx-border-color: black;");

        // Create VBox for buttons on the right side for decompression
        VBox decompressButtonBox = new VBox(10);
        decompressButtonBox.setAlignment(Pos.CENTER_RIGHT);
        decompressButtonBox.getChildren().addAll(decompress, headerButtonDecompress, statisticDEComButton);

        // Create VBox for TextArea on the left side for decompression
        VBox decompressTextAreaBox = new VBox();
        decompressTextAreaBox.setAlignment(Pos.CENTER_LEFT);
        decompressTextAreaBox.getChildren().add(decompressTextArea);

        // Create HBox to separate TextArea and Buttons for decompression
        HBox decompressCenterBox = new HBox(10);
        decompressCenterBox.getChildren().addAll(decompressTextAreaBox, decompressButtonBox);
        decompressCenterBox.setAlignment(Pos.CENTER);

        decompressBox.getChildren().addAll(decompressLabel, decompressCenterBox);
        Tab decompressTab = new Tab("Decompression", decompressBox);

        // Add tabs to the tabPane
        tabPane.getTabs().addAll(compressTab, decompressTab);

        pane.setCenter(tabPane);

        Huffman Huffman = new Huffman();

        compress.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(primaryStage);

            Huffman.compress(file);
            System.out.println("Compression process done :)");

            headerButton.setOnAction(s -> {
                if (file != null) {
                    compressTextArea.appendText("File path: " + file.getPath() + "\nCompressed file path: "
                            + Huffman.outFileName + "\n\nASCII\tCharacter\tFrequency\tHuffCode\n");
                    for (int k = 0; k < Huffman.huffCodeArray.length; k++) {
                        if ((int) Huffman.huffCodeArray[k].getCharacter() == 10
                                || (int) Huffman.huffCodeArray[k].getCharacter() == 9) // Skip line feed and tab
                            continue;
                        compressTextArea.appendText(String.valueOf((int) Huffman.huffCodeArray[k].getCharacter())
                                + "\t" + Huffman.huffCodeArray[k].getCharacter() + "\t"
                                + Huffman.huffCodeArray[k].getCounter() + "\t"
                                + Huffman.huffCodeArray[k].getHuffCode() + "\n");
                    }
                }
            });

            statisticComButton.setOnAction(s -> {
                if (file != null) {
                    StringBuilder string = gatherCompressionStatistics(file, Huffman.outFileName);
                    compressTextArea.setText(string.toString());
                }
            });
        });

        decompress.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(primaryStage);

            Huffman.deCompress(file);
            System.out.println("Decompression process done :)");

            headerButtonDecompress.setOnAction(s -> {
                if (file != null) {
                    decompressTextArea.appendText("File path: " + file.getPath() + "\nOriginal file path: "
                            + Huffman.originalFileName + "\n\nASCII\tCharacter\tFrequency\tHuffCode\n");
                    for (int k = 0; k < Huffman.huffCodeArray.length; k++) {
                        if ((int) Huffman.huffCodeArray[k].getCharacter() == 10
                                || (int) Huffman.huffCodeArray[k].getCharacter() == 9)
                            continue;
                        decompressTextArea.appendText(String.valueOf((int) Huffman.huffCodeArray[k].getCharacter())
                                + "\t" + Huffman.huffCodeArray[k].getCharacter() + "\t"
                                + Huffman.huffCodeArray[k].getCounter() + "\t"
                                + Huffman.huffCodeArray[k].getHuffCode() + "\n");
                    }
                }
            });

            statisticDEComButton.setOnAction(s -> {
                if (file != null) {
                    StringBuilder string = gatherDecompressionStatistics(file, Huffman.originalFileName);
                    decompressTextArea.setText(string.toString());
                }
            });
        });

        Scene scene = new Scene(pane, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Huffman Project");
        primaryStage.show();
        
    }





	static void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public StringBuilder gatherDecompressionStatistics(File compressedFile, String decompressedFileName) {
		StringBuilder statistics = new StringBuilder();
		try {
			// Get sizes of compressed and decompressed files
			long compressedSize = compressedFile.length();
			System.out.println("compressedFile : " + compressedFile.length());
			File decompressedFile = new File(decompressedFileName);
			long decompressedSize = decompressedFile.length();
			System.out.println("decompressedFileName : " + decompressedFileName.length());
			// Calculate decompression ratio
			double decompressionRatio = (double) compressedSize / decompressedSize;
			// Append statistics to StringBuilder
			statistics.append("Decompression Statistics:\n");
			System.out.println("Decompression Statistics:\n");
			statistics.append("Compressed file size: ").append(compressedSize).append(" bytes\n");
			System.out.println("compressedSize : " + compressedSize);
			statistics.append("Decompressed file size: ").append(decompressedSize).append(" bytes\n");
			System.out.println("decompressedSize : " + decompressedSize);
			statistics.append("Decompression ratio: ").append(String.format("%.4f", decompressionRatio))
					.append(" % \n");
			System.out.println("decompressionRatio : " + decompressionRatio);
		} catch (Exception e) {
			statistics.append("Error gathering decompression statistics: ").append(e.getMessage()).append("\n");
		}
		return statistics;
	}

	public StringBuilder gatherCompressionStatistics(File originalFile, String outFileName) {
		StringBuilder statistics = new StringBuilder();
		try {
			// Get sizes of original and compressed files
			long originalSize = originalFile.length();
			long compressedSize = new File(outFileName).length(); // Get the length of the compressed file

			// Calculate compression ratio
			double compressionRatio = (double) compressedSize / originalSize;

			// Append statistics to StringBuilder
			statistics.append("Compression Statistics:\n");
			statistics.append("Original file size: ").append(originalSize).append(" bytes\n");
			statistics.append("Compressed file size: ").append(compressedSize).append(" bytes\n");
			statistics.append("Compression ratio: ").append(String.format("%.4f", compressionRatio)).append(" % \n");

		} catch (Exception e) {
			statistics.append("Error gathering compression statistics: ").append(e.getMessage()).append("\n");
		}
		return statistics;
	}
}
