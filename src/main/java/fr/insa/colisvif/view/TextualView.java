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

    private ObservableList<Vertex> vertexMap;

    private TableColumn<Vertex, Long> nodeIdColumn;

    private TableColumn<Vertex, Integer> durationColumn;

    public TextualView() {
        super();

        this.table = new TableView<>();
        this.getChildren().add(this.table);

        this.table.prefHeightProperty().bind(this.heightProperty());
        this.table.prefWidthProperty().bind(this.widthProperty());

        this.nodeIdColumn = new TableColumn<>("Id Node");
        this.nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        this.durationColumn = new TableColumn<>("Duration");
        this.durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationInSeconds"));

        this.table.getColumns().add(nodeIdColumn);
        this.table.getColumns().add(durationColumn);
    }

    public void setVertexMap(DeliveryMap deliveryMap) {
        this.vertexMap = FXCollections.observableArrayList();

        for (Delivery d : deliveryMap.getDeliveryList()) {
            vertexMap.add(new Vertex(d.getPickUpNodeId(), false, d.getPickUpDuration()));
            vertexMap.add(new Vertex(d.getDropOffNodeId(), true, d.getDropOffDuration()));
        }
    }

    public void printVertices() {
        table.setItems(vertexMap);
    }
}
