package fr.insa.colisvif.view;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class StatusBar extends HBox {

    private Label message;

    private ScrollPane scrollPane;

    public StatusBar() {
        super();
        this.message = new Label();
        this.scrollPane = new ScrollPane();

        this.getChildren().add(this.scrollPane);
        this.scrollPane.setContent(this.message);

        this.message.setText("Bienvenue sur l'application ColisVIF");
        this.scrollPane.prefWidthProperty().bind(this.widthProperty());
        this.scrollPane.setMaxHeight(20);
    }

    public void setStatus(String text) {
        this.message.setTextFill(Color.rgb(0, 0, 0));
        this.message.setText(text);
    }

    public void setError(String text) {
        this.message.setTextFill(Color.rgb(255, 0, 0));
        this.message.setText(text);
    }
}
