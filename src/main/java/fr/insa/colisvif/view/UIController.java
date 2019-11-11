package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.Step;
import javafx.collections.FXCollections;
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

    @FXML
    private Button computeRound;

    private Stage stage;

    private Controller controller;

    private MapCanvas mapCanvas;

    private TextualView vertexView;

    private TextualView stepView;

    private StatusBar statusBar;

    /**
     * Creates a new UIController, passing in the {@link Stage}
     * and the MVC {@link Controller}
     * of the app
     * @param stage the main {@link Stage} of the app
     * @param controller the MVC {@link Controller} of the app
     */
    public UIController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mapCanvas = new MapCanvas();
        this.vertexView = new TextualView(false);
        this.stepView = new TextualView(true);
        this.statusBar = new StatusBar();
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

        this.computeRound.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.computeRound();
        });

        // Edit buttons
        this.addDelivery.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.addDelivery();
        });
        this.deleteDelivery.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.deleteDelivery();
        });
        this.editSequence.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            Step step = this.stepView.getStepTable().getSelectionModel().getSelectedItem();
            if (step != null) {
                this.controller.editSequenceDelivery(step);
            }
        });
        this.editLocation.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            Step step = this.stepView.getStepTable().getSelectionModel().getSelectedItem();
            if (step != null) {
                this.controller.editLocationDelivery(step);
            }
        });

        this.rightPane.setCenter(this.vertexView);
        this.mainPane.setCenter(this.mapCanvas);
        this.mainPane.setBottom(this.statusBar);

        LOGGER.info("UI successfully initialized");
    }

    /**
     * Renders the associated {@link TextualView}
     */
    public void printVertexView() {
        this.vertexView.printVertices(controller.getVertexList());
    }

    /**
     * Renders the associated {@link TextualView}
     */
    private void printStepView() {
        this.stepView.printSteps(FXCollections.observableArrayList( controller.getStepList()));
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
    public TextualView getVertexView() {
        return this.vertexView;
    }

    public void printStatus(String text) {
        this.statusBar.setStatus(text);
    }

    public void printError(String text) {
        this.statusBar.setError(text);
    }

    public void updateTable() {
        this.printStepView();
        this.rightPane.setCenter(this.stepView);
    }
}
