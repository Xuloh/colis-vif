package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    private VertexView vertexView;

    private StepView stepView;

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
        this.mapCanvas = new MapCanvas(this, true);
        this.vertexView = new VertexView();
        this.stepView = new StepView();
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
     * Returns the associated {@link MapCanvas}
     * @return the associated {@link MapCanvas}
     */
    public MapCanvas getMapCanvas() {
        return this.mapCanvas;
    }

    /**
     * Returns the associated {@link VertexView}
     * @return the associated {@link VertexView}
     */
    public VertexView getVertexView() {
        return this.vertexView;
    }

    public void printStatus(String text) {
        this.statusBar.setStatus(text);
    }

    public void printError(String text) {
        this.statusBar.setError(text);
    }

    public void updateCityMap() {
        this.mapCanvas.updateCityMap();
    }

    public void updateDeliveryMap() {
        this.rightPane.setCenter(this.vertexView);
        this.mapCanvas.updateDeliveryMap();
    }

    public void updateRound() {
        if (this.controller.getRound() == null) {
            this.stepView.clearSteps();
            this.vertexView.printVertices(controller.getVertexList());
            this.rightPane.setCenter(this.vertexView);
        } else {
            this.vertexView.clearVertices();
            this.stepView.printSteps(FXCollections.observableArrayList(controller.getStepList()));
            this.rightPane.setCenter(this.stepView);
        }
        this.mapCanvas.updateRound();
        this.mapCanvas.redraw();
    }

    public void setShowCityMapNodesOnHover(boolean enable) {
        this.mapCanvas.setShowCityMapNodesOnHover(enable);
    }

    /*package-private*/ CityMap getCityMap() {
        return this.controller.getCityMap();
    }

    /*package-private*/ DeliveryMap getDeliveryMap() {
        return this.controller.getDeliveryMap();
    }

    /*package-private*/ Round getRound() {
        return this.controller.getRound();
    }

    /*package-private*/ ObservableList<Vertex> getVertexList() {
        return this.controller.getVertexList();
    }
}
