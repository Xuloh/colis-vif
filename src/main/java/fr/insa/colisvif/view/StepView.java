package fr.insa.colisvif.view;

import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A custom {@link Pane} that wraps and handles a {@link TableView}
 * to render instances of {@link Vertex} before the calculated itinerary
 * @see TableView
 */
/* todo : permettre de demander une durée de pickup / dropoff (renvoyer un String) @Sophie */
//todo : adapter
public class StepView extends Pane {
    private static final Logger LOGGER = LogManager.getLogger(StepView.class);

    private TableView<Step> stepTable;

    /**
     * Creates a new {@link TableView} of {@link Vertex} with two {@link TableColumn}.
     * The first {@link TableColumn} corresponds to the nodeId
     * and the second {@link TableColumn} corresponds to the duration.
     *
     * @see TableView
     */
    public StepView() {
        super();
        this.stepTable = new TableView<>();
        this.getChildren().add(this.stepTable);
        this.stepTable.prefHeightProperty().bind(this.heightProperty());
        this.stepTable.prefWidthProperty().bind(this.widthProperty());


        TableColumn<Step, Long> deliveryIdColumn = new TableColumn<>("N° de livraison");
        deliveryIdColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));

        /*
        TableColumn<Step, Vertex> typeColumn = new TableColumn<>("Durée");
        typeColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().FONCTION_GET_VERTEX.isPickUp()));
        typeColumn.setCellFactory(col -> new TableCell<Step, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty) ;
                setText(empty ? null : item ? "Enlèvement" : "Livraison" );
            }
        });
         */
        TableColumn<Step, Boolean> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isPickUp()));
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
                if(item == null) {
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



        /*
        TableColumn<Step, Vertex> durationColumn = new TableColumn<>("Durée");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("NOM_VARIABLE_VERTEX"));
        durationColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Vertex item, boolean empty) {
                super.updateItem(item, empty) ;
                if(item==null)
                {
                    setText(null);
                } else {
                    int secondes_tot = item.getDuration();
                    int secondes = secondes_tot % 60;
                    int minutes = secondes_tot / 60;
                    int heures = minutes / 60;
                    minutes = minutes % 60;

                    String secondes_str = secondes < 10 ? "0" + secondes : "" + secondes;
                    String minutes_str = minutes < 10 ? "0" + minutes : "" + minutes;
                    String heures_str = heures < 10 ? "0" + heures : "" + heures;

                    setText(heures_str + ":" + minutes_str + ":" + secondes_str);
                }
            }
        });
         */
        TableColumn<Step, Integer> durationColumn = new TableColumn<>("Durée");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        durationColumn.setCellFactory(col -> new TableCell<Step, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (item==null) {
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
                    String heures_str_inf = (heures - 1) < 10 ? "0" + (heures - 1) : "" + (heures - 1);
                    String heures_str_sup = (heures + 1) < 10 ? "0" + (heures + 1) : "" + (heures + 1);

                    setText("[" +  heures_str_inf + ":" + minutes_str + ":" + "00"
                            + " - " + heures_str_sup + ":" + minutes_str + ":" + "00" + "]");
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
}
