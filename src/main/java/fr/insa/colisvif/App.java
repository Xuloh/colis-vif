package fr.insa.colisvif;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.view.UIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Main class of the App
 */
public class App extends Application {

    private static final Logger LOGGER = LogManager.getLogger(App.class);

    final KeyCodeCombination undo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);

    final KeyCodeCombination redo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);

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
        scene.setOnKeyPressed(event -> {
            if (undo.match(event)) {
                controller.undo();
            } else if (redo.match(event)) {
                controller.redo();
            }
        });

        // init and show stage
        stage.setTitle("Colis Vif");
        stage.setScene(scene);
        stage.show();

        LOGGER.info("JavaFX application successfully started");
    }

    public static void main(String[] args) {
        // launch javafx app
        LOGGER.info(Runtime.getRuntime().maxMemory());
        launch(args);
    }
}
