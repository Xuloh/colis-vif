package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
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
    private MenuItem openMap;

    @FXML
    private MenuItem close;

    private Stage stage;

    private Controller controller;

    private MapCanvas mapCanvas;

    public MainController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mapCanvas = new MapCanvas();
    }

    public void initialize() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir une carte");

        this.openMap.addEventHandler(ActionEvent.ACTION, event -> {
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.openFile(file);
        });

        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());

        this.mainPane.setCenter(this.mapCanvas);
    }

    public MapCanvas getMapCanvas() {
        return this.mapCanvas;
    }
}
