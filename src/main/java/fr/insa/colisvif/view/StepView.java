package fr.insa.colisvif.view;

import static java.lang.Math.abs;

import fr.insa.colisvif.model.Step;
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
 * instances of {@link Step} once the itinerary is computed.
 *
 * @see TableView
 */

public class StepView extends Pane {

    private static final Logger LOGGER = LogManager.getLogger(StepView.class);

    private TableView<Step> stepTable;

    private UIController uiController;

    private List<Consumer<Vertex>> eventHandlers;

    private TableColumn<Step, Integer> deliveryIdColumn;

    /**
     * Creates a new {@link TableView} of {@link Step} with five {@link
     * TableColumn}. The first {@link TableColumn} corresponds to the
     * deliveryIdC. The second {@link TableColumn} corresponds to the type. The
     * third {@link TableColumn} corresponds to the arrival time. The fourth
     * {@link TableColumn} corresponds to the duration. The fifth {@link
     * TableColumn} corresponds to the time interval. Also links {@link
     * javafx.event.EventHandler} to the TableView
     *
     * @param uiController bound to the {@link StepView}
     *
     * @see TableView
     */

    public StepView(UIController uiController) {
        super();
        this.uiController = uiController;
        this.stepTable = new TableView<>();
        this.getChildren().add(this.stepTable);
        this.stepTable.prefHeightProperty().bind(this.heightProperty());
        this.stepTable.prefWidthProperty().bind(this.widthProperty());
        this.eventHandlers = new ArrayList<>();

        this.stepTable.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    Step step = this.stepTable.getSelectionModel()
                        .getSelectedItem();
                    for (Consumer<Vertex> eventHandler : this.eventHandlers) {
                        eventHandler.accept(step.getArrival());
                    }
                }
            });

        deliveryIdColumn = new TableColumn<>("N° livraison");
        deliveryIdColumn
            .setCellValueFactory(new PropertyValueFactory<>("deliveryID"));
        deliveryIdColumn.setCellFactory(col -> new TableCell<Step, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (uiController.getColorMap() != null
                    && uiController.getColorMap().get(item) != null) {

                    Color color = uiController.getColorMap().get(item);
                    String cssColor =
                        "rgb(" + (255 * color.getRed()) + "," + (255 * color
                            .getGreen())
                            + "," + (255 * color.getBlue()) + ")";
                    setStyle("-fx-background-color:" + cssColor);
                    setText("" + item);
                }
            }
        });

        TableColumn<Step, Boolean> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(
            cellData -> new SimpleBooleanProperty(
                cellData.getValue().isPickUp()));
        typeColumn.setCellFactory(col -> new TableCell<Step, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Enlèvement" : "Dépôt");
            }
        });

        TableColumn<Step, Integer> arrivalColumn = new TableColumn<>("Arrivée");
        arrivalColumn
            .setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        arrivalColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                } else {
                    int minutes = item / 60;
                    int heures = minutes / 60;
                    minutes = minutes % 60;

                    String minutesStr =
                        minutes < 10 ? "0" + minutes : "" + minutes;
                    String heuresStr = heures < 10 ? "0" + heures : "" + heures;

                    setText(heuresStr + ":" + minutesStr);
                }
            }
        });

        TableColumn<Step, Integer> durationColumn = new TableColumn<>("Durée");
        durationColumn
            .setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.setCellFactory(col -> new TableCell<Step, Integer>() {
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

                    String secondesStr =
                        secondes < 10 ? "0" + secondes : "" + secondes;
                    String minutesStr =
                        minutes < 10 ? "0" + minutes : "" + minutes;
                    String heuresStr = heures < 10 ? "0" + heures : "" + heures;

                    setText(heuresStr + ":" + minutesStr + ":" + secondesStr);
                }
            }
        });

        TableColumn<Step, Integer> timeIntervalColumn = new TableColumn<>(
            "Intervalle d'arrivée");
        timeIntervalColumn.setCellValueFactory(
            new PropertyValueFactory<>("initialArrivalDate"));
        timeIntervalColumn
            .setCellFactory(col -> new TableCell<Step, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null) {
                        setText(null);
                    } else {
                        Step step = getTableView().getItems().get(getIndex());
                        if (abs(item - step.getArrivalDate()) > 3600) {
                            setStyle("-fx-background-color: " + " red");
                        } else {
                            setStyle(
                                "-fx-background-color: " + " mediumseagreen");
                        }
                        int minutes = item / 60;
                        int heures = minutes / 60;
                        minutes = minutes % 60;
                        String minutesStr =
                            minutes < 10 ? "0" + minutes : "" + minutes;
                        String heuresStrInf =
                            (heures - 1) < 10 ? "0" + (heures - 1)
                                : "" + (heures - 1);
                        String heuresStrSup =
                            (heures + 1) < 10 ? "0" + (heures + 1)
                                : "" + (heures + 1);

                        setText("[" + heuresStrInf + ":" + minutesStr + " - "
                            + heuresStrSup + ":"
                            + minutesStr + "]");
                    }
                }
            });

        this.stepTable.getColumns().add(deliveryIdColumn);
        this.stepTable.getColumns().add(typeColumn);
        this.stepTable.getColumns().add(arrivalColumn);
        this.stepTable.getColumns().add(durationColumn);
        this.stepTable.getColumns().add(timeIntervalColumn);
    }

    /**
     * Gets the {@link TableView} associated to this StepView
     *
     * @return the {@link TableView} of {@link Step}
     */
    public TableView<Step> getStepTable() {
        return this.stepTable;
    }

    /**
     * Removes the items of the {@link TableView} and erases the CSS style of
     * the deliveryIdColumn cells
     */
    public void clearSteps() {
        stepTable.getItems().clear();
        deliveryIdColumn.setCellFactory(col -> new TableCell<Step, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (uiController.getColorMap() != null
                    && uiController.getColorMap().get(item) != null) {

                    Color color = uiController.getColorMap().get(item);
                    String cssColor =
                        "rgb(" + (255 * color.getRed()) + "," + (255 * color
                            .getGreen())
                            + "," + (255 * color.getBlue()) + ")";
                    setStyle("-fx-background-color:" + cssColor);
                    setText("" + item);
                }
            }
        });
        LOGGER.info("Steps cleared");
    }

    /**
     * Set the TableView items using an Observable list of Step
     *
     * @param stepList {@link List} of {@link Step} to visualize in the {@link
     *                 StepView}
     */
    public void printSteps(ObservableList<Step> stepList) {
        this.clearSteps();
        if (stepList != null) {
            stepTable.setItems(stepList);
            LOGGER.info("Steps printed");
        }
    }

    /**
     * Adds an eventHandler to StepView
     *
     * @param eventHandler eventHandler to attach to the class
     */
    public void addEventHandlerOnSelect(Consumer<Vertex> eventHandler) {
        this.eventHandlers.add(eventHandler);
    }


    /**
     * Select the first row in the TableView matching the input delivery ID and
     * Type
     *
     * @param deliveryID the ID of the delivery associated to the vertex
     *                   selected in the {@link MapCanvas}
     * @param type       the type of the associated to the vertex selected in
     *                   the {@link MapCanvas}
     */
    public void onSelection(int deliveryID, boolean type) {
        this.stepTable.getSelectionModel().clearSelection();
        int indiceToSelect = 0;
        ObservableList<Step> items = this.stepTable.getItems();
        for (Step item : items) {
            if (item.getType() == type && item.getDeliveryID() == deliveryID) {
                break;
            }
            indiceToSelect++;

        }
        this.stepTable.getSelectionModel().select(indiceToSelect);
        this.stepTable.scrollTo(indiceToSelect);
    }

}
