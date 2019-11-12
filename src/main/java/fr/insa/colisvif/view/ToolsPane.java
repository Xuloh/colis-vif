package fr.insa.colisvif.view;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A simple {@link FlowPane} wrapping a few widgets
 * to control a {@link MapCanvas}.
 * @see MapCanvas
 */
public class ToolsPane extends FlowPane {

    private static final double BUTTON_SIZE = 30d;

    private Button autoZoomButton;

    private Button zoomInButton;

    private Button zoomOutButton;

    private Slider zoomSlider;

    private Map<String, Button> buttons;

    /**
     * Creates a new {@link ToolsPane}
     */
    public ToolsPane() {
        super(Orientation.VERTICAL);
        this.setColumnHalignment(HPos.CENTER);
        this.setVgap(5);

        this.createAutoZoomButton();
        this.createZoomPlusButton();
        this.createZoomMinusButton();
        this.createZoomSlider();

        this.getChildren().addAll(
            this.autoZoomButton,
            this.zoomInButton,
            this.zoomOutButton,
            this.zoomSlider
        );

        this.buttons = new HashMap<>();
        this.buttons.put("auto-zoom", this.autoZoomButton);
        this.buttons.put("zoom-in", this.zoomInButton);
        this.buttons.put("zoom-out", this.zoomOutButton);
    }

    private void createAutoZoomButton() {
        this.autoZoomButton = new Button("A");
        this.autoZoomButton.setMaxWidth(BUTTON_SIZE);
        this.autoZoomButton.setMaxHeight(BUTTON_SIZE);
        this.autoZoomButton.setMinWidth(BUTTON_SIZE);
        this.autoZoomButton.setMinHeight(BUTTON_SIZE);
    }

    private void createZoomPlusButton() {
        this.zoomInButton = new Button("+");
        this.zoomInButton.setMaxWidth(BUTTON_SIZE);
        this.zoomInButton.setMaxHeight(BUTTON_SIZE);
        this.zoomInButton.setMinWidth(BUTTON_SIZE);
        this.zoomInButton.setMinHeight(BUTTON_SIZE);
    }

    private void createZoomMinusButton() {
        this.zoomOutButton = new Button("-");
        this.zoomOutButton.setMaxWidth(BUTTON_SIZE);
        this.zoomOutButton.setMaxHeight(BUTTON_SIZE);
        this.zoomOutButton.setMinWidth(BUTTON_SIZE);
        this.zoomOutButton.setMinHeight(BUTTON_SIZE);
    }

    private void createZoomSlider() {
        this.zoomSlider = new Slider(CanvasConstants.MIN_ZOOM_SCALE, CanvasConstants.MAX_ZOOM_SCALE, 1d);
        this.zoomSlider.setOrientation(Orientation.VERTICAL);
    }

    /**
     * Returns the auto zoom {@link Button} of this {@link ToolsPane}
     * @return the auto zoom {@link Button} of this {@link ToolsPane}
     */
    public Button getAutoZoomButton() {
        return this.autoZoomButton;
    }

    /**
     * Returns the zoom in {@link Button} of this {@link ToolsPane}
     * @return the zoom in {@link Button} of this {@link ToolsPane}
     */
    public Button getZoomInButton() {
        return this.zoomInButton;
    }

    /**
     * Returns the zoom out {@link Button} of this {@link ToolsPane}
     * @return the zoom out {@link Button} of this {@link ToolsPane}
     */
    public Button getZoomOutButton() {
        return this.zoomOutButton;
    }

    /**
     * Returns the zoom {@link Slider} of this {@link ToolsPane}
     * @return the zoom {@link Slider} of this {@link ToolsPane}
     */
    public Slider getZoomSlider() {
        return this.zoomSlider;
    }

    public Button createButton(String name, String text) {
        if (this.buttons.containsKey(name)) {
            throw new InvalidParameterException("Button with name " + name + " already exists");
        }
        Button button = new Button(text);
        this.buttons.put(name, button);
        this.getChildren().add(button);
        return button;
    }

    public Button getButton(String name) {
        if (!this.buttons.containsKey(name)) {
            throw new NoSuchElementException("No button named " + name + " ToolsPane");
        }
        return this.buttons.get(name);
    }
}
