package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.*;
import fr.insa.colisvif.util.Quadruplet;
import javafx.beans.InvalidationListener;
import javafx.scene.canvas.Canvas;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    @FXML
    private BorderPane mainPane;

    @FXML
    private BorderPane rightPane;

    @FXML
    private MenuItem openDeliveryMap;

    @FXML
    private MenuItem close;

    @FXML
    private Pane canvasPane;

    @FXML
    private MenuItem openMap;

    //@FXML
    //private TableView textualView;

    @FXML
    private TextArea statusView;

    private Stage stage;

    private Controller controller;

    private MapCanvas mapCanvas;

    private TextualView textualView;

    public MainController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mapCanvas = new MapCanvas();
        this.textualView = new TextualView();
    }

    public void initialize() {
        //initialiseTextualView();

        FileChooser fileChooser = new FileChooser();

        this.openMap.addEventHandler(ActionEvent.ACTION, event -> {
            fileChooser.setTitle("Ouvrir une carte");
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.loadCityMap(file);
        });

        this.openDeliveryMap.addEventHandler(ActionEvent.ACTION, event -> {
            fileChooser.setTitle("Ouvrir un plan de livraison");
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.loadDeliveryMap(file, this.mapCanvas.getCityMap());
        });

        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());

        this.rightPane.setCenter(this.textualView);
        this.mainPane.setCenter(this.mapCanvas);
    }

    /*private void initialiseTextualView() {
        TableColumn<String, Vertex> nodeIdColumn = new TableColumn<>("Id Node");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

        TableColumn<String, Vertex> nodeIdColumn = new TableColumn<>("Id Node");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

        TableColumn<String, Vertex> durationColumn = new TableColumn<>("Id Node");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

        textualView.getColumns().addAll(nodeIdColumn, durationColumn);


        TableColumn<String, Delivery> pickUpDurationColumn = new TableColumn<>("Durée enlèvement");
        pickUpDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        TableColumn<String, Delivery> deliveryDurationColumn = new TableColumn<>("Durée dépôt");
        deliveryDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        textualView.getColumns().add(pickUpDurationColumn);
        textualView.getColumns().add(deliveryDurationColumn);
    }*/

    public void windowResized() {
        this.clearMap();
        this.drawMap();
    }

    /*public void writeDeliveries(DeliveryMap deliveryMap) {
        for (Vextex v : vertexMap) {
            textualView.getItems().add(v);
        }

        for (Delivery d : deliveryMap.getDeliveryList()) {
            textualView.getItems().add(deliveryMap.getDeliveryList().get(0));
        }

        System.out.println(deliveryMap.getDeliveryList().get(0).getPickUpDuration());
        textualView.getItems().add(deliveryMap.getDeliveryList().get(0));
        textualView.getItems().add(deliveryMap.getDeliveryList().get(0));
    }

    public void writeImpossibleDelivery(DeliveryMap deliveryMap) {
        StringBuilder builder = new StringBuilder("");
        builder.append(statusView.getText());
        if (!deliveryMap.getImpossibleDeliveries().isEmpty()) {
            builder.append("Bad Ones : ");
        }
        for (Delivery delivery : deliveryMap.getImpossibleDeliveries()) {
            builder.append(delivery.getPickUpNodeId() + "->" + delivery.getDropOffNodeId() + "/");
        }
        if (!deliveryMap.getImpossibleDeliveries().isEmpty()) {
            builder.append("\n");
        }
        statusView.setText(builder.toString());
    }*/

    public void clearMap() {
        this.mapCanvas.clearMap();
    }

    public void drawMap() {
        this.mapCanvas.drawMap();
    }

    public MapCanvas getMapCanvas() {
        return this.mapCanvas;
    }
}
