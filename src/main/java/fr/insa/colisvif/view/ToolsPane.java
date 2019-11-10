package fr.insa.colisvif.view;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;

/**
 * A simple {@link FlowPane} wrapping a few widgets
 * to control a {@link MapCanvas}.
 * @see MapCanvas
 */
public class ToolsPane extends FlowPane {

    private static final double BUTTON_SIZE = 30d;

    private Button autoZoomButton;

    private Button zoomPlusButton;

    private Button zoomMinusButton;

    private Slider zoomSlider;

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
            this.zoomPlusButton,
            this.zoomMinusButton,
            this.zoomSlider
        );
    }

    private void createAutoZoomButton() {
        this.autoZoomButton = new Button("A");
        this.autoZoomButton.setMaxWidth(BUTTON_SIZE);
        this.autoZoomButton.setMaxHeight(BUTTON_SIZE);
        this.autoZoomButton.setMinWidth(BUTTON_SIZE);
        this.autoZoomButton.setMinHeight(BUTTON_SIZE);
    }

    private void createZoomPlusButton() {
        this.zoomPlusButton = new Button("+");
        this.zoomPlusButton.setMaxWidth(BUTTON_SIZE);
        this.zoomPlusButton.setMaxHeight(BUTTON_SIZE);
        this.zoomPlusButton.setMinWidth(BUTTON_SIZE);
        this.zoomPlusButton.setMinHeight(BUTTON_SIZE);
    }

    private void createZoomMinusButton() {
        this.zoomMinusButton = new Button("-");
        this.zoomMinusButton.setMaxWidth(BUTTON_SIZE);
        this.zoomMinusButton.setMaxHeight(BUTTON_SIZE);
        this.zoomMinusButton.setMinWidth(BUTTON_SIZE);
        this.zoomMinusButton.setMinHeight(BUTTON_SIZE);
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
     * Returns the zoom plus {@link Button} of this {@link ToolsPane}
     * @return the zoom plus {@link Button} of this {@link ToolsPane}
     */
    public Button getZoomPlusButton() {
        return this.zoomPlusButton;
    }

    /**
     * Returns the zoom minus {@link Button} of this {@link ToolsPane}
     * @return the zoom minus {@link Button} of this {@link ToolsPane}
     */
    public Button getZoomMinusButton() {
        return this.zoomMinusButton;
    }

    /**
     * Returns the zoom {@link Slider} of this {@link ToolsPane}
     * @return the zoom {@link Slider} of this {@link ToolsPane}
     */
    public Slider getZoomSlider() {
        return this.zoomSlider;
    }
}
