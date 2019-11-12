package fr.insa.colisvif.view;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StatusBar extends HBox {

    private static final Logger LOGGER = LogManager.getLogger(StatusBar.class);

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
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        LOGGER.debug("Status view message : {}\nCaller : {}.{}", text, caller.getClassName(), caller.getMethodName());
    }

    public void setError(String text) {
        this.message.setTextFill(Color.rgb(255, 0, 0));
        this.message.setText(text);
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        LOGGER.debug("Status view error : {}\nCaller : {}.{}", text, caller.getClassName(), caller.getMethodName());
    }
}
