package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Node;
import fr.insa.colisvif.model.Section;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
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
    private Canvas canvas;

    private Stage stage;

    private Controller controller;

    public MainController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void initialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une carte");

        this.openMap.addEventHandler(ActionEvent.ACTION, event -> {
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.openFile(file);
        });

        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());
    }

    public void clearMap() {
        var context = this.canvas.getGraphicsContext2D();
        context.clearRect(0, 0,
                          this.canvas.getWidth(),
                          this.canvas.getHeight());
    }

    public void drawMap(CityMap map) {
        var context = this.canvas.getGraphicsContext2D();
        final int LAT_TO_PX = 10000;
        final int LNG_TO_PX = 10000;

        final Color NODE_COLOR = Color.CORNFLOWERBLUE;
        final Color SECTION_COLOR = Color.BLACK;
        final int NODE_SIZE = 3;

        Map<Long, Node> nodes = map.getMapNode();

        context.setFill(SECTION_COLOR);
        List<Section> sections = map
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

            double x1 = (origin.getLongitude() - map.getLngMin()) * LNG_TO_PX;
            double y1 = (map.getLatMax() - origin.getLatitude()) * LAT_TO_PX;

            double x2 = (destination.getLongitude() - map.getLngMin())
                        * LNG_TO_PX;
            double y2 = (map.getLatMax() - destination.getLatitude())
                        * LAT_TO_PX;

            context.strokeLine(x1, y1, x2, y2);
        }

        context.setFill(NODE_COLOR);
        for (Node node : nodes.values()) {
            double x = (node.getLongitude() - map.getLngMin()) * LNG_TO_PX;
            double y = (map.getLatMax() - node.getLatitude()) * LAT_TO_PX;
            context.fillOval(x - NODE_SIZE / 2d,
                             y - NODE_SIZE / 2d,
                             NODE_SIZE,
                             NODE_SIZE);
        }
    }
}
