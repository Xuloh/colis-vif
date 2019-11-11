package fr.insa.colisvif;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.view.UIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Main class of the App
 */
public class App extends Application {

    private static final Logger LOGGER = LogManager.getLogger(App.class);

    /**
     * Entry point called by JavaFX to launch the app
     * @param stage the main {@link Stage} of the app
     * @throws IOException if the main FXML document could not be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        LOGGER.info("Starting JavaFX application ...");

        Controller controller = new Controller();
        // create fxml loader
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/scene.fxml")
        );

        // create and set controller
        UIController uiController = new UIController(stage, controller);
        loader.setController(uiController);

        controller.setUIController(uiController);

        // load scene
        Scene scene = new Scene(loader.load());

        // apply css
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // init and show stage
        stage.setTitle("Colis Vif");
        stage.setScene(scene);
        stage.show();

        LOGGER.info("JavaFX application successfully started");
    }

    public static void main(String[] args) {
        // launch javafx app
        launch(args);
    }
}
