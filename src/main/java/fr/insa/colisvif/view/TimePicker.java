package fr.insa.colisvif.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.TilePane;

/**
 * A custom {@link TilePane} that wraps and handles a {@link Spinner} and
 * a {@link Label}.
 */
public class TimePicker extends TilePane {

    private Spinner<Integer> spinner;

    private Label label;

    /**
     * Creates a new {@link Spinner} and an associated {@link Label}.
     */
    public TimePicker() {
        super();

        this.label = new Label("test");
        this.spinner = new Spinner<>();
        this.getChildren().add(this.label);
        this.getChildren().add(this.spinner);
        this.spinner.setMaxWidth(55);
        this.setAlignment(Pos.TOP_CENTER);
        this.setMaxWidth(200);
        this.setMaxHeight(100);

        this.setHgap(0);
        this.setVgap(0);

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        60,
                        5);

        this.spinner.setValueFactory(valueFactory);
    }

    /**
     * Reset the initial value of the {@link Spinner}
     *
     * @param labelText
     */
    public void resetTimePicker(String labelText) {
        this.label.setText(labelText);

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        0,
                        60,
                        5);

        spinner.setValueFactory(valueFactory);
    }

    /**
     * Get the time selected by the user.
     *
     * @return the value of the spinner
     */
    public int getTimeValue() {
        return this.spinner.getValue();
    }
}
