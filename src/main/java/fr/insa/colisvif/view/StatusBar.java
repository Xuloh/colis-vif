package fr.insa.colisvif.view;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StatusBar extends HBox {

    private static final Logger LOGGER = LogManager.getLogger(StatusBar.class);

    private Label messageStatus;

    private Label messageError;

    private ScrollPane scrollPane;

    private boolean isDarkMode;

    public StatusBar() {
        super();
        this.messageStatus = new Label();
        this.messageError = new Label();
        this.messageError.setTextFill(Color.rgb(255, 0, 0));
        this.messageError.setId("label-error");
        this.scrollPane = new ScrollPane();

        this.getChildren().add(this.scrollPane);
        this.scrollPane.setContent(this.messageStatus);

        this.messageStatus.setText("Bienvenue sur l'application ColisVIF");
        this.scrollPane.prefWidthProperty().bind(this.widthProperty());
        this.scrollPane.setMaxHeight(20);
        this.isDarkMode = false;
    }

    public void setStatus(String text) {
        this.messageStatus.setText(text);
        this.scrollPane.setContent(this.messageStatus);
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        LOGGER.debug("Status view message : {}\nCaller : {}.{}", text, caller.getClassName(), caller.getMethodName());
    }

    public void setError(String text) {
        this.messageError.setText(text);
        this.scrollPane.setContent(this.messageError);
        StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
        LOGGER.debug("Status view error : {}\nCaller : {}.{}", text, caller.getClassName(), caller.getMethodName());
    }
}
