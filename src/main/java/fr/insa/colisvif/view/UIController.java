package fr.insa.colisvif.view;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * JavaFX controller used to handle all the different view components
 */
public class UIController {

    private static final Logger LOGGER = LogManager
        .getLogger(UIController.class);

    private Map<Integer, Color> colorMap;

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
    private MenuItem exportRound;

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

    @FXML
    private MenuItem addDeliveryItem;

    @FXML
    private MenuItem deleteDeliveryItem;

    @FXML
    private MenuItem editLocationItem;

    @FXML
    private MenuItem editSequenceItem;

    @FXML
    private MenuItem computeRoundItem;

    @FXML
    private TilePane tilePane;

    @FXML
    private CheckMenuItem darkModeToggle;

    @FXML
    private MenuItem autoZoomItem;

    @FXML
    private MenuItem zoomInItem;

    @FXML
    private MenuItem zoomOutItem;

    @FXML
    private MenuItem undoItem;

    @FXML
    private MenuItem redoItem;

    private Stage stage;

    private Controller controller;

    private MapCanvas mapCanvas;

    private VertexView vertexView;

    private StepView stepView;

    private StatusBar statusBar;

    private TimePicker timePicker;

    private ExportView exportView;

    /**
     * Creates a new UIController, passing in the {@link Stage}
     * and the MVC {@link Controller}
     * of the app
     * @param stage the main {@link Stage} of the app
     * @param controller {@link Controller} of the application the MVC {@link Controller} of the app
     */
    public UIController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        this.mapCanvas = new MapCanvas(this, true);
        this.vertexView = new VertexView(this);
        this.stepView = new StepView(this);
        this.statusBar = new StatusBar();
        this.timePicker = new TimePicker();
        this.exportView = new ExportView(this);
        this.colorMap = new HashMap<>();
        this.stage.getIcons().add(
            new Image(
                UIController.class
                    .getResourceAsStream("/icon.png")
            )
        );
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

        this.exportRound.addEventHandler(ActionEvent.ACTION, event -> {
            fileChooser.setTitle("Choisir un dossier de sauvegarde");
            File file = fileChooser.showSaveDialog(this.stage);
            this.controller.exportRound(file);
        });

        this.close.addEventHandler(ActionEvent.ACTION, event -> stage.close());

        // Edit menu

        this.undoItem.addEventHandler(ActionEvent.ACTION, event -> {
            this.controller.undo();
        });

        this.redoItem.addEventHandler(ActionEvent.ACTION, event -> {
            this.controller.redo();
        });



        this.computeRound.addEventHandler(ActionEvent.ACTION, actionEvent -> {
            this.controller.computeRound();
        });
        this.computeRound.addEventHandler(
            MouseEvent.MOUSE_ENTERED,
            e -> printStatus("Calcule le trajet optimal du cycliste "
                             + "pour récupérer tous les colis et les livrer.")
        );
        this.computeRoundItem.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> this.controller.computeRound()
        );
        this.computeRoundItem.addEventHandler(
            MouseEvent.MOUSE_ENTERED,
            e -> printStatus("Calcule le trajet optimal du cycliste "
                             + "pour récupérer tous les colis et les livrer.")
        );

        // Edit buttons
        this.addDelivery.addEventHandler(
            ActionEvent.ACTION, actionEvent -> this.controller.addDelivery()
        );
        this.addDelivery.addEventHandler(
            MouseEvent.MOUSE_ENTERED,
            e -> printStatus("Ajoute une livraison en deux étapes : "
                             + "définition de l'arrêt de récupération du colis"
                             + ", et définition de l'arrêt de livraison.")
        );
        this.addDeliveryItem.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> this.controller.addDelivery()
        );
        this.addDeliveryItem.addEventHandler(
            MouseEvent.MOUSE_ENTERED,
            e -> printStatus("Ajoute une livraison en deux étapes :"
                             + " définition de l'arrêt de récupération du colis"
                             + ", et définition de l'arrêt de livraison.")
        );

        this.deleteDelivery.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> deleteDelivery()
        );

        this.deleteDeliveryItem.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> deleteDelivery()
        );

        this.editLocation.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> editLocationDelivery()
        );

        this.editLocationItem.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> editLocationDelivery()
        );

        this.editSequence.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> editSequenceDelivery()
        );

        this.editSequenceItem.addEventHandler(
            ActionEvent.ACTION,
            actionEvent -> editSequenceDelivery()
        );

        this.mapCanvas.addNodeMouseClickHandler((nodeId, vertex) -> {
            if (vertex != null) {
                this.stepView.onSelection(
                    vertex.getDeliveryId(),
                    vertex.getType()
                );
                this.vertexView.onSelection(
                    vertex.getDeliveryId(),
                    vertex.getType()
                );
                LOGGER.debug("Delivery selected " + vertex.getDeliveryId());
                LOGGER.debug("Type " + vertex.getType());
            } else {
                this.stepView.getStepTable()
                             .getSelectionModel().select(null);
                this.vertexView.getVertexTable()
                               .getSelectionModel().select(null);
            }
        });

        this.stepView.addEventHandlerOnSelect(vertex -> {
            if (vertex != null) {
                this.mapCanvas.setSelectedVertex(vertex);
                this.controller.inputSelectedVertex(vertex);
            }
        });

        this.vertexView.addEventHandlerOnSelect(vertex -> {
            if (vertex != null) {
                this.mapCanvas.setSelectedVertex(vertex);
            }
        });

        this.darkModeToggle.addEventHandler(ActionEvent.ACTION, event -> {
            if (this.darkModeToggle.isSelected()) {
                this.stage.getScene()
                    .getStylesheets()
                    .add(
                        getClass()
                            .getResource("/dark-mode.css")
                            .toExternalForm()
                    );
            } else {
                this.stage.getScene().getStylesheets().clear();
            }
        });

        this.autoZoomItem.addEventHandler(
            ActionEvent.ACTION,
            event -> this.mapCanvas.autoZoom()
        );

        this.zoomInItem.addEventHandler(
            ActionEvent.ACTION,
            event -> this.mapCanvas.zoomIn()
        );

        this.zoomOutItem.addEventHandler(
            ActionEvent.ACTION,
            event -> this.mapCanvas.zoomOut()
        );

        this.rightPane.setCenter(this.vertexView);
        this.mainPane.setCenter(this.mapCanvas);
        this.mainPane.setBottom(this.statusBar);

        this.disableButtons();

        LOGGER.info("UI successfully initialized");
    }

    private void editSequenceDelivery() {
        Step step = this.stepView
            .getStepTable()
            .getSelectionModel()
            .getSelectedItem();
        if (step != null) {
            this.controller.editSequenceDelivery(step);
        } else {
            printError("Choisissez d'abord un arrêt à modifier.");
        }
    }

    private void editLocationDelivery() {
        Step step = this.stepView
            .getStepTable()
            .getSelectionModel()
            .getSelectedItem();
        if (step != null) {
            this.controller.editLocationDelivery(step);
        } else {
            printError("Choisissez d'abord un arrêt à modifier.");
        }
    }

    private void deleteDelivery() {
        Step step = this.stepView
            .getStepTable()
            .getSelectionModel()
            .getSelectedItem();
        if (step != null) {
            this.controller.deleteDelivery(step);
        } else {
            printError("Choisissez d'abord un arrêt à supprimer.");
        }
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

    /**
     * Displays a status message in the {@link StatusBar}
     * @param message the message to print
     */
    public void printStatus(String message) {
        this.statusBar.setStatus(message);
    }

    /**
     * Displays an error message in the {@link StatusBar}
     * @param error the error message to print
     */
    public void printError(String error) {
        this.statusBar.setError(error);
    }

    /**
     * Updates the {@link CityMap} on the UI
     */
    public void updateCityMap() {
        this.mapCanvas.updateCityMap();

        this.disableButtons();
        this.openDeliveryMap.setDisable(false);
    }

    /**
     * Updates the {@link DeliveryMap} on the UI
     */
    public void updateDeliveryMap() {
        if (getDeliveryMap() != null) {
            this.stepView.clearSteps();
            this.vertexView.clearVertices();
            ColorGenerator colorGenerator = new ColorGenerator(
                getDeliveryMap().getSize(),
                CanvasConstants.NODE_OPACITY,
                1
            );

            for (Delivery d : getDeliveryMap().getDeliveryList()) {
                this.colorMap.put(d.getId(), colorGenerator.next());
            }
        }
        this.rightPane.setCenter(this.vertexView);
        this.mapCanvas.updateDeliveryMap();

    }

    /**
     * Enables all the buttons on the UI
     */
    public void enableButtons() {
        this.openDeliveryMap.setDisable(false);
        this.exportRound.setDisable(false);
        this.computeRound.setDisable(false);
        this.addDelivery.setDisable(false);
        this.deleteDelivery.setDisable(false);
        this.editSequence.setDisable(false);
        this.editLocation.setDisable(false);
        this.computeRoundItem.setDisable(false);
        this.addDeliveryItem.setDisable(false);
        this.deleteDeliveryItem.setDisable(false);
        this.editSequenceItem.setDisable(false);
        this.editLocationItem.setDisable(false);
    }

    /**
     * Disables all the buttons on the UI
     */
    public void disableButtons() {
        this.openDeliveryMap.setDisable(true);
        this.exportRound.setDisable(true);
        this.computeRound.setDisable(true);
        this.addDelivery.setDisable(true);
        this.deleteDelivery.setDisable(true);
        this.editSequence.setDisable(true);
        this.editLocation.setDisable(true);
        this.computeRoundItem.setDisable(true);
        this.addDeliveryItem.setDisable(true);
        this.deleteDeliveryItem.setDisable(true);
        this.editSequenceItem.setDisable(true);
        this.editLocationItem.setDisable(true);
    }

    /**
     * Enables or disables the buttons on the UI depending on the current state
     */
    public void setButtons() {
        this.disableButtons();
        if (this.controller.getDeliveryMap() == null) {
            LOGGER.debug("DELIVERYMAP IS NULL");
            this.openDeliveryMap.setDisable(false);
        } else if (this.controller.getRound() == null) {
            LOGGER.debug("ROUND IS NULL");
            this.openDeliveryMap.setDisable(false);
            this.computeRound.setDisable(false);
            this.computeRoundItem.setDisable(false);
        } else {
            LOGGER.debug("NOTHING IS NULL");
            this.enableButtons();
            this.computeRound.setDisable(true);
            this.computeRoundItem.setDisable(true);
        }
    }

    /**
     * Displays the time picker when adding a pick up node
     */
    public void addPickUp() {
        this.timePicker.resetTimePicker("Durée d'enlèvement : ");
        if (!this.tilePane.getChildren().contains(this.timePicker)) {
            this.tilePane.getChildren().add(this.timePicker);
        }
    }

    /**
     * Displays the time picker when adding a drop off node
     */
    public void addDropOff() {
        this.timePicker.resetTimePicker("Durée du dépôt : ");
        if (!this.tilePane.getChildren().contains(this.timePicker)) {
            this.tilePane.getChildren().add(this.timePicker);
        }
    }

    /**
     * Returns the time input from the time picker
     * @return the time input from the time picker
     */
    public int getTimeFromPicker() {
        LOGGER.info(this.timePicker.getTimeValue());
        return this.timePicker.getTimeValue();
    }

    /**
     * Clears the time picker
     */
    public void clearTimePicker() {
        this.tilePane.getChildren().remove(this.timePicker);
    }

    /**
     * Updates the {@link Round} on the UI
     */
    public void updateRound() {
        if (this.controller.getRound() == null) {
            this.stepView.clearSteps();
            this.vertexView.printVertices(controller.getVertexList());
            this.rightPane.setCenter(this.vertexView);
        } else {
            this.vertexView.clearVertices();
            this.stepView.printSteps(
                FXCollections.observableArrayList(controller.getStepList())
            );
            this.rightPane.setCenter(this.stepView);
        }
        this.mapCanvas.updateRound();
        this.mapCanvas.redraw();
    }

    /**
     * Toggles the nodes displayed on the {@link MapCanvas}
     * @param enable if <code>true</code>, displays {@link CityMap} nodes. If
     * false, displays {@link Delivery} nodes
     *
     * @see MapCanvas#setShowCityMapNodesOnHover(boolean)
     */
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

    /*package-private*/ StepView getStepView() {
        return this.stepView;
    }

    /**
     * Adds a global mouse click event handler
     * @param eventHandler the handler that will receive mouse click events
     * @see MouseEvent#MOUSE_CLICKED
     */
    public void addMouseClickEventHandler(EventHandler<MouseEvent> eventHandler) {
        this.stage.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    /**
     * Returns a {@link Map} that maps {@link Delivery} ID to {@link Color}
     * @return a {@link Map} that maps {@link Delivery} ID to {@link Color}
     */
    public Map<Integer, Color> getColorMap() {
        return colorMap;
    }

}
