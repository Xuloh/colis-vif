package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.*;
import fr.insa.colisvif.util.Quadruplet;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class UIController {

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    @FXML
    private MenuItem openMap;

    @FXML
    private MenuItem openDeliveryMap;

    @FXML
    private MenuItem close;

    @FXML
    private Pane canvasPane;

    @FXML
    private Canvas canvas;

    @FXML
    private TableView textualView;

    @FXML
    private TextArea statusView;

    private Stage stage;

    private Controller controller;

    private CityMap map;

    private double scale;

    public UIController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.map = null;
        this.scale = 1d;
    }

    public void initialize() {
        initialiseTextualView();

        FileChooser fileChooser = new FileChooser();

        this.openMap.addEventHandler(ActionEvent.ACTION, event -> {
            fileChooser.setTitle("Ouvrir une carte");
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.loadCityMap(file);
        });

        this.openDeliveryMap.addEventHandler(ActionEvent.ACTION, event -> {
            fileChooser.setTitle("Ouvrir un plan de livraison");
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.loadDeliveryMap(file, map);
        });

        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());

        this.canvas.heightProperty().bind(this.canvasPane.heightProperty());
        this.canvas.widthProperty().bind(this.canvasPane.widthProperty());

        InvalidationListener listener = observable -> this.windowResized();
        this.canvas.heightProperty().addListener(listener);
        this.canvas.widthProperty().addListener(listener);
    }

    private void initialiseTextualView() {

        TableColumn<String, Delivery> pickUpDurationColumn = new TableColumn<>("Durée enlèvement");
        pickUpDurationColumn.setCellValueFactory(new PropertyValueFactory<>("pickUpDuration"));

        TableColumn<String, Delivery> deliveryDurationColumn = new TableColumn<>("Durée dépôt");
        deliveryDurationColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDuration"));

        textualView.getColumns().add(pickUpDurationColumn);
        textualView.getColumns().add(deliveryDurationColumn);
    }

    public void windowResized() {
        this.clearMap();
        this.drawMap();
    }

    public void writeDeliveries(DeliveryMap deliveryMap) {
        for (Delivery d : deliveryMap.getDeliveryList()) {
            textualView.getItems().add(deliveryMap.getDeliveryList().get(0));
        }

        System.out.println("ici");
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
            builder.append(delivery.getPickUpNodeId() + "->" + delivery.getDeliveryNodeId() + "/");
        }
        if (!deliveryMap.getImpossibleDeliveries().isEmpty()) {
            builder.append("\n");
        }
        statusView.setText(builder.toString());
    }

    public void clearMap() {
        var context = this.canvas.getGraphicsContext2D();
        context.clearRect(0, 0,
                          this.canvas.getWidth(),
                          this.canvas.getHeight());
    }

    public void drawMap() {
        if (this.map == null) {
            return;
        }

        var context = this.canvas.getGraphicsContext2D();

        final Color NODE_COLOR = Color.CORNFLOWERBLUE;
        final Color SECTION_COLOR = Color.BLACK;
        final int NODE_SIZE = 3;

        Map<Long, Node> nodes = this.map.getMapNode();

        context.setFill(SECTION_COLOR);
        List<Section> sections = this.map
            .getMapSection()
            .values()
            .stream()
            .reduce(new ArrayList<>(), (acc, val) -> {
                acc.addAll(val);
                return acc;
            });
        for (Section section : sections) {
            Node origin = nodes.get(section.getOrigin());
            Node destination = nodes.get(section.getDestination());

            double x1 = (origin.getLongitude() - this.map.getLngMin()) * this.scale;
            double y1 = (this.map.getLatMax() - origin.getLatitude()) * this.scale;

            double x2 = (destination.getLongitude() - this.map.getLngMin())
                        * this.scale;
            double y2 = (this.map.getLatMax() - destination.getLatitude())
                        * this.scale;

            context.strokeLine(x1, y1, x2, y2);
        }

        context.setFill(NODE_COLOR);
        for (Node node : nodes.values()) {
            double x = (node.getLongitude() - this.map.getLngMin()) * this.scale;
            double y = (this.map.getLatMax() - node.getLatitude()) * this.scale;
            context.fillOval(x - NODE_SIZE / 2d,
                             y - NODE_SIZE / 2d,
                             NODE_SIZE,
                             NODE_SIZE);
        }
    }

    public void setCityMap(CityMap map) {
        this.map = map;

        final double MAP_WIDTH = this.map.getLngMax() - this.map.getLngMin();
        final double MAP_HEIGHT = this.map.getLatMax() - this.map.getLatMin();
        final double CANVAS_WIDTH = this.canvas.getWidth();
        final double CANVAS_HEIGHT = this.canvas.getHeight();

        if (CANVAS_WIDTH > CANVAS_HEIGHT) {
            this.scale = CANVAS_HEIGHT / MAP_HEIGHT;
        } else {
            this.scale = CANVAS_WIDTH / MAP_WIDTH;
        }

    }
}
