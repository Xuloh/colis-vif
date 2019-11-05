package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Node;
import fr.insa.colisvif.model.Section;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
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

public class MainController {

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    @FXML
    private MenuItem openMap;

    @FXML
    private MenuItem close;

    @FXML
    private Pane canvasPane;

    @FXML
    private Canvas canvas;

    private Stage stage;

    private Controller controller;

    private CityMap map;

    private double scale;

    public MainController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.map = null;
        this.scale = 1d;
    }

    public void initialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une carte");

        this.openMap.addEventHandler(ActionEvent.ACTION, event -> {
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.loadCityMap(file);
        });

        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());

        this.canvas.heightProperty().bind(this.canvasPane.heightProperty());
        this.canvas.widthProperty().bind(this.canvasPane.widthProperty());

        InvalidationListener listener = observable -> this.windowResized();
        this.canvas.heightProperty().addListener(listener);
        this.canvas.widthProperty().addListener(listener);
    }

    public void windowResized() {
        this.clearMap();
        this.drawMap();
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
