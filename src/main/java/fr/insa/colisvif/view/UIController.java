package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX controller used to handle all the different view components
 */
public class UIController {

    private static final Logger LOGGER = LogManager.getLogger(UIController.class);

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

    @FXML
    private TextArea statusView;

    @FXML
    private Button addDelivery;

    @FXML
    private Button deleteDelivery;

    @FXML
    private Button editLocation;

    @FXML
    private Button editSequence;

    private Stage stage;

    private Controller controller;

    private MapCanvas mapCanvas;

    private TextualView textualView;

    /**
     * Creates a new UIController, passing in the {@link Stage} and the MVC {@link Controller}
     * of the app
     * @param stage the main {@link Stage} of the app
     * @param controller the MVC {@link Controller} of the app
     */
    public UIController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mapCanvas = new MapCanvas();
        this.textualView = new TextualView();
    }

    /**
     * Called by JavaFX when creating the window.
     */
    public void initialize() {
        LOGGER.info("Initializing UI");

        FileChooser fileChooser = new FileChooser();

        // File menu
        this.openMap.addEventHandler(ActionEvent.ACTION, event -> {
            fileChooser.setTitle("Ouvrir une carte");
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.loadCityMap(file);
        });

        this.openDeliveryMap.addEventHandler(ActionEvent.ACTION, event -> {
            fileChooser.setTitle("Ouvrir un plan de livraison");
            File file = fileChooser.showOpenDialog(this.stage);
            this.controller.loadDeliveryMap(file);
        });

        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());

        // Edit buttons
        this.addDelivery.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.addDelivery();
        });
        this.deleteDelivery.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.deleteDelivery();
        });
        this.editSequence.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.editSequenceDelivery();
        });
        this.editLocation.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.editLocationDelivery();
        });

        this.rightPane.setCenter(this.textualView);
        this.mainPane.setCenter(this.mapCanvas);

        LOGGER.info("UI successfully initialized");
    }

    /**
     * Renders the associated {@link TextualView}
     */
    public void printTextualView() {
        this.textualView.printVertices(controller.getVertexList());
    }

    /**
     * Clears the associated {@link MapCanvas}.
     */
    public void clearCanvas() {
        this.mapCanvas.clearCanvas();
    }

    /**
     * Renders the associated {@link MapCanvas}
     */
    public void drawCanvas() {
        this.mapCanvas.drawCityMap();
        this.mapCanvas.drawDeliveryMap();
    }

    /**
     * Returns the associated {@link MapCanvas}
     * @return the associated {@link MapCanvas}
     */
    public MapCanvas getMapCanvas() {
        return this.mapCanvas;
    }

    /**
     * Returns the associated {@link TextualView}
     * @return the associated {@link TextualView}
     */
    public TextualView getTextualView() {
        return this.textualView;
    }
}
