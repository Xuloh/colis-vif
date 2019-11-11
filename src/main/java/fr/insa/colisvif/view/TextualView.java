package fr.insa.colisvif.view;

import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import javafx.collections.ObservableList;
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

            this.vertexTable.prefHeightProperty().bind(this.heightProperty());
            this.vertexTable.prefWidthProperty().bind(this.widthProperty());

            TableColumn<Step, Long> nodeIdColumn = new TableColumn<>("Id Node");
            nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

            TableColumn<Vertex, Integer> durationColumn = new TableColumn<>("Duration");
            durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

            //this.vertexTable.getColumns().add(nodeIdColumn);
            this.vertexTable.getColumns().add(durationColumn);
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
}
