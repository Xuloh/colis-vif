/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package fr.insa.colisvif;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public String getGreeting() {
        return "Hello World.";
    }

    @Override
    public void start(Stage stage) throws IOException {
        // load main scene
        Parent root = FXMLLoader.load(getClass().getResource("/scene.fxml"));
        Scene scene = new Scene(root);

        // apply css
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // init and show stage
        stage.setTitle("Hello World");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        // launch javafx app
        launch(args);
    }
}
