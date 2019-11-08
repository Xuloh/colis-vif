package fr.insa.colisvif.view;

import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Vertex;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class TextualView extends Pane {

    private TableView<Vertex> table;

    private List<Vertex> vertexMap;

    public TextualView() {
        super();

        this.table = new TableView<>();
        this.getChildren().add(this.table);

        this.table.prefHeightProperty().bind(this.heightProperty());
        this.table.prefWidthProperty().bind(this.widthProperty());

        TableColumn<Vertex, Long> nodeIdColumn = new TableColumn<>("Id Node");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Vertex, Integer> durationColumn = new TableColumn<>("Duration");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("durationInSeconds"));

        table.getColumns().add(nodeIdColumn);
        table.getColumns().add(durationColumn);
    }

    public void setVertexMap(DeliveryMap deliveryMap) {
        vertexMap = new ArrayList<>();

        for (Delivery d : deliveryMap.getDeliveryList()) {
            vertexMap.add(new Vertex(d.getPickUpNodeId(), false, d.getPickUpDuration()));
            vertexMap.add(new Vertex(d.getDropOffNodeId(), true, d.getDropOffDuration()));
        }
    }

    public void printVertices() {
        for (Vertex v : vertexMap) {
            System.out.println(v.toString());
            table.getItems().add(v);
        }
    }
}
