package fr.insa.colisvif.view;

import fr.insa.colisvif.model.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A custom {@link Pane} that wraps and handles a {@link TableView} to render
 * instances of {@link Vertex} before the calculated itinerary
 *
 * @see TableView
 */
public class VertexView extends Pane {

    private static final Logger LOGGER = LogManager.getLogger(VertexView.class);

    private TableView<Vertex> vertexTable;

    private UIController uiController;

    private List<Consumer<Vertex>> eventHandlers;

    private TableColumn<Vertex, Integer> deliveryIdColumn;


    /**
     * Creates a new {@link TableView} of {@link Vertex} with three
     * {@link TableColumn}. The first {@link TableColumn} corresponds to the
     * number of the delivery, the second {@link TableColumn} corresponds
     * to the type of the stop of the vertex and the last one corresponds to the
     * duration.
     *
     * @see TableView
     */
    public VertexView(UIController uiController) {
        super();
        this.uiController = uiController;
        this.vertexTable = new TableView<>();
        this.getChildren().add(this.vertexTable);

        this.vertexTable.prefHeightProperty().bind(this.heightProperty());
        this.vertexTable.prefWidthProperty().bind(this.widthProperty());

        this.eventHandlers = new ArrayList<>();

        this.vertexTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        Vertex vertex = this.vertexTable.getSelectionModel()
                                .getSelectedItem();
                        for (Consumer<Vertex> eventHandler
                                : this.eventHandlers) {
                            eventHandler.accept(vertex);
                        }
                    }
                });

        deliveryIdColumn = new TableColumn<>("N° livraison");
        deliveryIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("deliveryId"));
        deliveryIdColumn.setCellFactory(
            col -> new TableCell<Vertex, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (uiController.getColorMap() != null
                                && uiController.getColorMap().get(item)
                                != null) {

                            Color color = uiController.getColorMap().get(item);
                            String cssColor =
                                    "rgb(" + (255 * color.getRed()) + ","
                                            + (255 * color.getGreen())
                                            + "," + (255 * color.getBlue())
                                            + ")";
                            setStyle("-fx-background-color:" + cssColor);
                            setText("" + item);
                        }
                    }
                });

        TableColumn<Vertex, Boolean> typeColumn =
                new TableColumn<>("Type");
        typeColumn.setCellValueFactory(
            cellData -> new SimpleBooleanProperty(
                        cellData.getValue().isPickUp()));
        typeColumn.setCellFactory(
            col -> new TableCell<Vertex, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(
                                empty ? null : item ? "Enlèvement" : "Livraison"
                        );
                    }
                });

        TableColumn<Vertex, Integer> durationColumn =
                new TableColumn<>("Durée");
        durationColumn.setCellValueFactory(
                new PropertyValueFactory<>("duration"));
        durationColumn.setCellFactory(
            col -> new TableCell<Vertex, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null) {
                            setText(null);
                        } else {
                            int secondes = item % 60;
                            int minutes = item / 60;
                            int heures = minutes / 60;
                            minutes = minutes % 60;

                            String secondesStr = secondes < 10 ? "0"
                                    + secondes : "" + secondes;
                            String minutesStr = minutes < 10 ? "0"
                                    + minutes : "" + minutes;
                            String heuresStr = heures < 10 ? "0" + heures : ""
                                    + heures;

                            setText(heuresStr + ":" + minutesStr + ":"
                                    + secondesStr);
                        }
                    }
                });

        this.vertexTable.getColumns().add(deliveryIdColumn);
        this.vertexTable.getColumns().add(typeColumn);
        this.vertexTable.getColumns().add(durationColumn);

    }

    /**
     * @return the {@link TableView} of {@link Vertex}
     */
    public TableView<Vertex> getVertexTable() {
        return this.vertexTable;
    }

    /**
     * Add rows of {@link Vertex}'s information in the {@link TableView}.
     *
     * @param vertexList the list of vertices to print.
     * @see Vertex
     * @see TableView
     */
    public void printVertices(ObservableList<Vertex> vertexList) {
        this.clearVertices();
        if (vertexList != null) {
            vertexTable.setItems(vertexList);
            LOGGER.info("Vertices printed");
        }
    }

    /**
     * Clear the rows and colors of the {@link TableView}
     */
    public void clearVertices() {
        vertexTable.getItems().clear();
        deliveryIdColumn.setCellFactory(
            col -> new TableCell<Vertex, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (uiController.getColorMap() != null
                                && uiController.getColorMap().get(item)
                                != null) {

                            Color color = uiController.getColorMap().get(item);
                            String cssColor =
                                    "rgb(" + (255 * color.getRed()) + ","
                                            + (255 * color.getGreen())
                                            + "," + (255 * color.getBlue())
                                            + ")";
                            setStyle("-fx-background-color:" + cssColor);
                            setText("" + item);
                        }
                    }
                });
        LOGGER.info("Vertices cleared");
    }

    /**
     * Add an event to the rows of the {@link TableView}
     *
     * @param eventHandler
     */
    public void addEventHandlerOnSelect(Consumer<Vertex> eventHandler) {
        this.eventHandlers.add(eventHandler);
    }

    /**
     * Compare the {@link Vertex} of the {@link TableView}
     * to the given deliveryID and type to highlight the rows
     *
     * @param deliveryID
     * @param type
     */
    public void onSelection(int deliveryID, boolean type) {
        this.vertexTable.getSelectionModel().clearSelection();
        int indiceToSelect = 0;
        ObservableList<Vertex> items = this.vertexTable.getItems();
        for (Vertex item : items) {
            if (item.getType() == type && item.getDeliveryId() == deliveryID) {
                break;
            }
            indiceToSelect++;

        }
        this.vertexTable.getSelectionModel().select(indiceToSelect);
        this.vertexTable.scrollTo(indiceToSelect);
    }
}
