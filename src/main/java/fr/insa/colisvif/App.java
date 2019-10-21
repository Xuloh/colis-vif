package fr.insa.colisvif;

import fr.insa.colisvif.view.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // create fxml loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene.fxml"));

        // create and set controller
        MainController controller = new MainController(stage);
        loader.setController(controller);

        // load scene
        Scene scene = new Scene(loader.load());

        // apply css
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // init and show stage
        stage.setTitle("Colis Vif");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // launch javafx app
        launch(args);
    }
}
