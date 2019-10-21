package fr.insa.colisvif;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.net.URL;
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

    public void initialize() {
        this.openMap.addEventHandler(ActionEvent.ACTION, event -> System.out.println("openMap"));
    }

    public void postInit(Stage stage) {
        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());
        var context = canvas.getGraphicsContext2D();
        context.fillOval(100, 100, 200, 200);
        context.strokeLine(10, 50, 3, 35);
    }
}
