package fr.insa.colisvif.view;

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
 * A custom {@link Pane} that wraps and handles a {@link TableView} to render instances of {@link
 * Vertex} before the calculated itinerary
 *
 * @see TableView
 */
/* todo : permettre de demander une durée de pickup / dropoff (renvoyer un String) @Sophie */
//todo : adapter
public class StepView extends Pane {

    private static final Logger LOGGER = LogManager.getLogger(StepView.class);

    private TableView<Step> stepTable;

    private UIController uiController;

    private List<Consumer<Vertex>> eventHandlers;


    /**
     * Creates a new {@link TableView} of {@link Vertex} with two {@link TableColumn}. The first
     * {@link TableColumn} corresponds to the nodeId and the second {@link TableColumn} corresponds
     * to the duration.
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
                    Step step = this.stepTable.getSelectionModel().getSelectedItem();
                    for (Consumer<Vertex> eventHandler : this.eventHandlers) {
                        eventHandler.accept(step.getArrival());
                    }
                    //LOGGER.debug("CLICK DANS LA STEPVIEW : " + step.getDeliveryID());
                }
            });

        TableColumn<Step, Integer> deliveryIdColumn = new TableColumn<>("N° livraison");
        deliveryIdColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));
        deliveryIdColumn.setCellFactory(col -> new TableCell<Step, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (uiController.getColorMap() != null
                    && uiController.getColorMap().get(item) != null) {

                    Color color = uiController.getColorMap().get(item);
                    String css_color =
                        "rgb(" + (255 * color.getRed()) + "," + (255 * color.getGreen())
                            + "," + (255 * color.getBlue()) + ")";
                    setStyle("-fx-background-color:" + css_color);
                    setText("" + item);
                }
            }
        });

        TableColumn<Step, Boolean> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(
            cellData -> new SimpleBooleanProperty(cellData.getValue().isPickUp()));
        typeColumn.setCellFactory(col -> new TableCell<Step, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Enlèvement" : "Livraison");
            }
        });

        TableColumn<Step, Integer> arrivalColumn = new TableColumn<>("Arrivée");
        arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
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

                    String minutes_str = minutes < 10 ? "0" + minutes : "" + minutes;
                    String heures_str = heures < 10 ? "0" + heures : "" + heures;

                    setText(heures_str + ":" + minutes_str);
                }
            }
        });

        TableColumn<Step, Integer> durationColumn = new TableColumn<>("Durée");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
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

                    String secondes_str = secondes < 10 ? "0" + secondes : "" + secondes;
                    String minutes_str = minutes < 10 ? "0" + minutes : "" + minutes;
                    String heures_str = heures < 10 ? "0" + heures : "" + heures;

                    setText(heures_str + ":" + minutes_str + ":" + secondes_str);
                }
            }
        });

        //TODO : change with initialArrivalDate
        TableColumn<Step, Integer> timeIntervalColumn = new TableColumn<>("Intervalle d'arrivée");
        timeIntervalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        timeIntervalColumn.setCellFactory(col -> new TableCell<Step, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                } else {
                    int minutes = item / 60;
                    int heures = minutes / 60;
                    minutes = minutes % 60;
                    String minutes_str = minutes < 10 ? "0" + minutes : "" + minutes;
                    String heures_str_inf =
                        (heures - 1) < 10 ? "0" + (heures - 1) : "" + (heures - 1);
                    String heures_str_sup =
                        (heures + 1) < 10 ? "0" + (heures + 1) : "" + (heures + 1);

                    setText("[" + heures_str_inf + ":" + minutes_str + " - " + heures_str_sup + ":"
                        + minutes_str + "]");
                }
            }
        });

        this.stepTable.getColumns().add(deliveryIdColumn);
        this.stepTable.getColumns().add(typeColumn);
        this.stepTable.getColumns().add(arrivalColumn);
        this.stepTable.getColumns().add(durationColumn);
        this.stepTable.getColumns().add(timeIntervalColumn);
    }

    public TableView<Step> getStepTable() {
        return this.stepTable;
    }

    public void clearSteps() {
        stepTable.getItems().clear();
        LOGGER.info("Steps cleared");
    }

    public void printSteps(ObservableList<Step> stepList) {
        this.clearSteps();
        if (stepList != null) {
            stepTable.setItems(stepList);
            LOGGER.info("Steps printed");
        }
    }

    public void addEventHandlerOnSelect(Consumer<Vertex> eventHandler) {
        this.eventHandlers.add(eventHandler);
    }


    public void onSelection(int deliveryID, boolean type) {
        this.stepTable.getSelectionModel().clearSelection();
        int indice_to_select = 0;
        ObservableList<Step> items = this.stepTable.getItems();
        for (Step item : items) {
            if (item.getType() == type && item.getDeliveryID() == deliveryID) {
                break;
            }
            indice_to_select++;

        }
        this.stepTable.getSelectionModel().select(indice_to_select);
        this.stepTable.scrollTo(indice_to_select);
    }

}
