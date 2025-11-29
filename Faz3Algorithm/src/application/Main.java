package application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Line;

public class Main extends Application {

    private BorderPane rootLayout;
    private VBox controlsLayoutVBox;

    private Label sourceLabel;
    private ComboBox<String> sourceComboBox;
    private Label targetLabel;
    private ComboBox<String> targetComboBox;
    private Label filterLabel;
    private ComboBox<String> filterComboBox;
    private Label pathLabel;
    private TextArea pathTextArea;
    private Button runButton;
    private Label distanceLabel;
    private TextField distanceField;
    private Label costLabel;
    private TextField costField;
    private Label timeLabel;
    private TextField timeField;

    private HBox sourceHBox;
    private HBox targetHBox;
    private HBox filterHBox;
    private HBox distanceHBox;
    private HBox costHBox;
    private HBox timeHBox;

    private ImageView imageView;

    private List<CapitalCity> cities = new ArrayList<>();
    private List<CostTimeSouDes> routes = new ArrayList<>();
    private Graph graph = new Graph();

    private boolean selectSource = true; // Flag to toggle between source and target selection

    private Pane imagePane; // Image pane to add lines
    private List<Line> pathLines = new ArrayList<>(); // List to store path lines

    public void readCitiesFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean readingCities = true;

            if ((line = br.readLine()) != null) {
                // Skip header
            }

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                if (line.trim().equals("....")) {
                    readingCities = false;
                    continue;
                }

                if (readingCities) {
                    String[] parts = line.split(":");
                    if (parts.length == 3) {
                        String cityName = parts[0].trim();  // Trim any leading/trailing spaces
                        double latitude = Double.parseDouble(parts[1].trim());
                        double longitude = Double.parseDouble(parts[2].trim());
                        
                        // Debugging: Print city name and coordinates to ensure correct parsing
                        System.out.println("Adding City: '" + cityName + "' Lat: " + latitude + ", Lon: " + longitude);

                        // Add the city to the list and graph
                        CapitalCity city = new CapitalCity(cityName, latitude, longitude);
                        cities.add(city);
                        graph.addVertex(cityName);  // Add the city to the graph

                        // Debugging: Verify cities in the graph
                        System.out.println("Cities in graph: " + graph.getVertices());
                    }
                } else {
                    String[] routeParts = line.split(":");
                    if (routeParts.length == 4) {
                        String sourceCityName = routeParts[0].trim();
                        String destinationCityName = routeParts[1].trim();
                        double cost = Double.parseDouble(routeParts[2].trim());
                        double time = Double.parseDouble(routeParts[3].trim());

                        CostTimeSouDes route = new CostTimeSouDes(sourceCityName, destinationCityName, cost, time);
                        routes.add(route);
                        graph.addEdgebetwenMatrex(sourceCityName, destinationCityName, time, cost);
                        graph.printCities();
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public void initializeMapMain(Stage primaryStage) {
        rootLayout = new BorderPane();
        controlsLayoutVBox = new VBox(10);
        controlsLayoutVBox.setAlignment(Pos.CENTER);

        Image image = new Image("file:///C:/Users/user/Desktop/java%20act/Algoo3/MAP.jpg");
        double imageWidth = 640;
        double imageHeight = 320;
        imageView = new ImageView(image);
        imageView.setFitWidth(imageWidth);
        imageView.setFitHeight(imageHeight);
        imageView.setPreserveRatio(true);

        imagePane = new Pane();
        imagePane.setPrefSize(imageWidth, imageHeight);
        imagePane.getChildren().add(imageView);

        // Draw circles for cities and set click event
        for (CapitalCity city : cities) {
            double[] position = city.CityInMap(imageWidth, imageHeight);
            System.out.println("City: " + city.getName() + " Position: " + Arrays.toString(position)); // Debugging print

            Circle cityCircle = new Circle(2.5);
            cityCircle.setFill(Color.RED);
            cityCircle.setCenterX(position[0]);
            cityCircle.setCenterY(position[1]);

            Text cityName = new Text(city.getName());
            cityName.setFill(Color.WHEAT);
            cityName.setFont(javafx.scene.text.Font.font(10));
            cityName.setX(position[0]);
            cityName.setY(position[1] - 5);

            EventHandler<MouseEvent> cityClickHandler = event -> {
                if (selectSource) {
                    sourceComboBox.setValue(city.getName());
                } else {
                    targetComboBox.setValue(city.getName());
                }
                selectSource = !selectSource; 
            };

            cityCircle.setOnMouseClicked(cityClickHandler);
            cityName.setOnMouseClicked(cityClickHandler);

            imagePane.getChildren().addAll(cityCircle, cityName);
        }

        controlsLayoutVBox.setPadding(new Insets(10));
        controlsLayoutVBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");

        sourceLabel = new Label("Source:");
        setLabelStyle(sourceLabel);
        sourceComboBox = new ComboBox<>();
        setComboBoxStyle(sourceComboBox);
        for (String city : graph.getVertices()) {
            sourceComboBox.getItems().add(city);
        }

        targetLabel = new Label("Target:");
        setLabelStyle(targetLabel);
        targetComboBox = new ComboBox<>();
        setComboBoxStyle(targetComboBox);
        for (String city : graph.getVertices()) {
            targetComboBox.getItems().add(city);
        }

        filterLabel = new Label("Filter:   ");
        setLabelStyle(filterLabel);
        filterComboBox = new ComboBox<>();
        setComboBoxStyle(filterComboBox);
        filterComboBox.getItems().addAll("Cost", "Time", "Distance");

        pathLabel = new Label("Path:");
        setLabelStyle(pathLabel);
        pathTextArea = new TextArea();
        setTextAreaStyle(pathTextArea);
        pathTextArea.setPrefHeight(100);

        runButton = new Button("Run");
        setButtonStyle(runButton);

        distanceLabel = new Label("Distance:");
        setLabelStyle(distanceLabel);
        distanceField = new TextField();
        setTextFieldStyle(distanceField);
        distanceField.setEditable(false);

        costLabel = new Label("Cost:");
        setLabelStyle(costLabel);
        costField = new TextField();
        setTextFieldStyle(costField);
        costField.setEditable(false);

        timeLabel = new Label("Time:");
        setLabelStyle(timeLabel);
        timeField = new TextField();
        setTextFieldStyle(timeField);
        timeField.setEditable(false);

        sourceHBox = new HBox(10, sourceLabel, sourceComboBox);
        targetHBox = new HBox(10, targetLabel, targetComboBox);
        filterHBox = new HBox(10, filterLabel, filterComboBox);
        distanceHBox = new HBox(10, distanceLabel, distanceField);
        costHBox = new HBox(10, costLabel, costField);
        timeHBox = new HBox(10, timeLabel, timeField);

        controlsLayoutVBox.getChildren().addAll(sourceHBox, targetHBox, filterHBox, pathLabel, pathTextArea, runButton, distanceHBox, costHBox, timeHBox);

        rootLayout.setCenter(controlsLayoutVBox);
        rootLayout.setLeft(imagePane);

        
        runButton.setOnAction(event -> {
            // Validate if all fields are filled
            if (sourceComboBox.getValue() == null || targetComboBox.getValue() == null || filterComboBox.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields.");
                alert.show();
                return;
            }

            String sourceCityName = sourceComboBox.getValue();
            String targetCityName = targetComboBox.getValue();
            String filterType = filterComboBox.getValue();

            // Calculate the shortest path and related metrics based on the selected filter
            List<String> path = graph.findShortestPathByMethod (sourceCityName, targetCityName, filterType);

            double totalDistance = 0, totalCost = 0, totalTime = 0;

            if (pathLines == null) {
                pathLines = new ArrayList<>();
            } else {
                imagePane.getChildren().removeAll(pathLines);
                pathLines.clear();
            }

            if (!path.isEmpty()) {
                drawPathLines(path, imageWidth, imageHeight); 

                for (int i = 0; i < path.size() - 1; i++) {
                    String currentCity = path.get(i);
                    String nextCity = path.get(i + 1);

                    double distance = graph.getDistanceBetweenCities(currentCity, nextCity, cities);
                    double cost = graph.getCostBetweenCities(currentCity, nextCity);
                    double time = graph.getTimeBetweenCities(currentCity, nextCity);

                    if (distance >= 0) {
                        totalDistance += distance;
                    } else {
                        System.out.println("No direct route from " + currentCity + " to " + nextCity);
                    }

                    if (cost >= 0) {
                        totalCost += cost;
                    }

                    if (time >= 0) {
                        totalTime += time;
                    }
                }
            }

                       if (path.isEmpty()) {
                pathTextArea.setText("No path found between " + sourceCityName + " and " + targetCityName);
                distanceField.clear();
                costField.clear();
                timeField.clear();
            } else {
                pathTextArea.setText(String.join(" -> ", path));

                if (totalDistance == 0) {
                    distanceField.setText("0 km");
                } else if (totalDistance == -1) {
                    distanceField.setText("No direct route available.");
                } else {
                    distanceField.setText(totalDistance + " km");
                }

                costField.setText(totalCost + " $");
                timeField.setText(totalTime + " Min");

                switch (filterType) {
                    case "Distance":
                        distanceField.setStyle("-fx-background-color: lightyellow;");
                        costField.setStyle("");
                        timeField.setStyle("");
                        break;
                    case "Cost":
                        distanceField.setStyle("");
                        costField.setStyle("-fx-background-color: lightyellow;");
                        timeField.setStyle("");
                        break;
                    case "Time":
                        distanceField.setStyle("");
                        costField.setStyle("");
                        timeField.setStyle("-fx-background-color: lightyellow;");
                        break;
                    default:
                        break;
                }
            }

            imagePane.requestLayout();
        });



        Scene scene = new Scene(rootLayout, 900, 450);
        primaryStage.setTitle("Map Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawPathLines(List<String> path, double imageWidth, double imageHeight) {
        imagePane.getChildren().removeAll(pathLines);
        pathLines.clear();

        for (int i = 0; i < path.size() - 1; i++) {
            String currentCity = path.get(i);
            String nextCity = path.get(i + 1);



            double[] startPosition = graph.getCityPos(currentCity, imageWidth, imageHeight, cities);
            double[] endPosition = graph.getCityPos(nextCity, imageWidth, imageHeight, cities);
            // Check if the positions are within bounds
            if (startPosition[0] >= 0 && startPosition[0] <= imageWidth &&
                startPosition[1] >= 0 && startPosition[1] <= imageHeight &&
                endPosition[0] >= 0 && endPosition[0] <= imageWidth &&
                endPosition[1] >= 0 && endPosition[1] <= imageHeight) {

                // Create a line to represent the path between the cities
                Line pathLine = new Line(startPosition[0], startPosition[1], endPosition[0], endPosition[1]);
                pathLine.setStroke(Color.YELLOW);
                pathLine.setStrokeWidth(3);

                // Add the line to the pathLines list and the image pane
                pathLines.add(pathLine);
                imagePane.getChildren().add(pathLine);
            }
        }
    }

    private void setButtonStyle(Button button) {
        button.setMinWidth(Button.USE_PREF_SIZE);
        button.setStyle("-fx-border-color: #000000; -fx-border-width: 2px;");
    }

    private void setLabelStyle(Label label) {
        label.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold;");
    }

    private void setComboBoxStyle(ComboBox<String> comboBox) {
        comboBox.setStyle("-fx-border-color: #800000; -fx-border-width: 2px; -fx-background-color: white;");
        comboBox.setPrefWidth(150);
    }

    private void setTextFieldStyle(TextField textField) {
        textField.setStyle("-fx-border-color: #800000; -fx-border-width: 2px; -fx-background-color: white;");
    }

    private void setTextAreaStyle(TextArea textArea) {
        textArea.setStyle("-fx-border-color: #800000; -fx-border-width: 2px; -fx-background-color: white;");
    }

    @Override
    public void start(Stage primaryStage) {
        File file = new File("C:\\Users\\user\\Desktop\\java act\\Algoo3\\src\\CapitalCity.txt");
        readCitiesFile(file);
        initializeMapMain(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}