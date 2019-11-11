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

/**
 * A custom {@link Pane} that wraps and handles a {@link TableView}
 * to render instances of {@link Vertex} before the calculated itinerary
 * @see TableView
 */
public class TextualView extends Pane {

    private TableView<Vertex> vertexTable;

    private TableView<Step> stepTable;

    /**
     * Creates a new {@link TableView} of {@link Vertex} with two {@link TableColumn}.
     * The first {@link TableColumn} corresponds to the nodeId
     * and the second {@link TableColumn} corresponds to the duration.
     *
     * @see TableView
     */
    public TextualView(boolean isStepTable) {
        super();

        if (isStepTable) {

            this.stepTable = new TableView<>();
            this.getChildren().add(this.stepTable);
            this.stepTable.prefHeightProperty().bind(this.heightProperty());
            this.stepTable.prefWidthProperty().bind(this.widthProperty());

            TableColumn<Step, Long> deliveryIdColumn = new TableColumn<>("Id Delivery");
            deliveryIdColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));

            TableColumn<Step, Boolean> typeColumn = new TableColumn<>("Type");
            typeColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isPickUp()));
            typeColumn.setCellFactory(col -> new TableCell<Step, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty) ;
                    setText(empty ? null : item ? "Pick up" : "Drop off" );
                }
            });

            TableColumn<Step, Integer> arrivalColumn = new TableColumn<>("Arrival");
            arrivalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));

            TableColumn<Step, Integer> durationColumn = new TableColumn<>("Duration");
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

            TableColumn<Step, Integer> timeIntervalColumn = new TableColumn<>("Time Interval");
            timeIntervalColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
            timeIntervalColumn.setCellFactory(col -> new TableCell<Step, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty) ;
                setText("[" + (item - 3600) + " - " + (item + 3600) + "]" );
                }
            });

            this.stepTable.getColumns().add(deliveryIdColumn);
            this.stepTable.getColumns().add(typeColumn);
            this.stepTable.getColumns().add(arrivalColumn);
            this.stepTable.getColumns().add(durationColumn);
            this.stepTable.getColumns().add(timeIntervalColumn);

        }
        else {
            this.vertexTable = new TableView<>();
            this.getChildren().add(this.vertexTable);

            this.vertexTable.prefHeightProperty().bind(this.heightProperty());
            this.vertexTable.prefWidthProperty().bind(this.widthProperty());

            TableColumn<Vertex, Long> nodeIdColumn = new TableColumn<>("Id Node");
            nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

            TableColumn<Vertex, Integer> durationColumn = new TableColumn<>("Duration");
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

            this.vertexTable.getColumns().add(nodeIdColumn);
            this.vertexTable.getColumns().add(durationColumn);
        }
    }

    /**
     * Add rows of {@link Vertex}'s information in the {@link TableView}.
     *
     * @param vertexList the list of vertices to print.
     *
     * @see Vertex
     * @see TableView
     */
    public void printVertices(ObservableList<Vertex> vertexList) {
        vertexTable.setItems(vertexList);
    }

    public TableView<Step> getStepTable(){
        return this.stepTable;
    }
}
