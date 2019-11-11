package fr.insa.colisvif.view;

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
// todo : permettre de demander une durée de pickup / dropoff @Sophie

public class TextualView extends Pane {

    private TableView<Vertex> table;

    /**
     * Creates a new {@link TableView} of {@link Vertex} with two {@link TableColumn}.
     * The first {@link TableColumn} corresponds to the nodeId
     * and the second {@link TableColumn} corresponds to the duration.
     *
     * @see TableView
     */
    public TextualView() {
        super();

        this.table = new TableView<>();
        this.getChildren().add(this.table);

        this.table.prefHeightProperty().bind(this.heightProperty());
        this.table.prefWidthProperty().bind(this.widthProperty());

        TableColumn<Vertex, Long> nodeIdColumn = new TableColumn<>("Id Node");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

        TableColumn<Vertex, Integer> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));

        this.table.getColumns().add(nodeIdColumn);
        this.table.getColumns().add(durationColumn);
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
        table.setItems(vertexList);
    }
}
