package fr.insa.colisvif.view;

import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Vertex;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class TextualView extends Pane {

    private TableView<Vertex> table;

    private TableColumn<Vertex, Long> nodeIdColumn;

    private TableColumn<Vertex, Integer> durationColumn;

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

    public void printVertices(ObservableList<Vertex> vertexList) {
        table.setItems(vertexList);
    }
}
