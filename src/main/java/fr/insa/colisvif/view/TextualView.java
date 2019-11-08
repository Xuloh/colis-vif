package fr.insa.colisvif.view;

import fr.insa.colisvif.model.Vertex;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import javafx.scene.control.TableView;

import java.util.List;

public class TextualView extends Pane {

    private TableView table;

    private List<Vertex> vertexMap;

    public TextualView() {
        this.table = new TableView();

        TableColumn<String, Vertex> nodeIdColumn = new TableColumn<>("Id Node");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

        TableColumn<String, Vertex> durationColumn = new TableColumn<>("Id Node");
        nodeIdColumn.setCellValueFactory(new PropertyValueFactory<>("nodeId"));

        table.getColumns().addAll(nodeIdColumn, durationColumn);
    }
}
