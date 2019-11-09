package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class UIController {

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    @FXML
    private BorderPane mainPane;

    @FXML
    private MenuItem openDeliveryMap;

    @FXML
    private MenuItem close;

    @FXML
    private Pane canvasPane;

    @FXML
    private MenuItem openMap;

    @FXML
    private ListView textualView;

    @FXML
    private TextArea statusView;

    private Stage stage;

    private Controller controller;

    private MapCanvas mapCanvas;

    public UIController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mapCanvas = new MapCanvas();
    }

    public void initialize() {
        FileChooser fileChooser = new FileChooser();

        textualView.getItems().add("test");

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

        this.mainPane.setCenter(this.mapCanvas);
    }

    public void writeDeliveries(DeliveryMap deliveryMap) {
        textualView.getItems().add("test");
    }

    public void clearCanvas() {
        this.mapCanvas.clearCanvas();
    }

    public void drawCanvas() {
        this.mapCanvas.drawCityMap();
        this.mapCanvas.drawDeliveryMap();
    }

    public MapCanvas getMapCanvas() {
        return this.mapCanvas;
    }
}
