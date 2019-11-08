package fr.insa.colisvif.view;

import javafx.scene.layout.Pane;

import javafx.scene.control.TableView;

public class TextualView extends Pane {

    private TableView table;

    //private List<Vertex> vertexMap;

    public TextualView() {
        this.table = new TableView();

        TableColumn<String, Delivery> pickUpDurationColumn = new TableColumn<>("Durée enlèvement");
        pickUpDurationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
    }
}
