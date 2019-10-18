package fr.insa.colisvif;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloWorldController implements Initializable {

    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // setup stuff with ui elements
        // ex :
        // this.label.setText("foobar");

        // (here there's nothing to do)
    }
}
