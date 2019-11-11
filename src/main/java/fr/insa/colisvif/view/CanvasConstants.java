package fr.insa.colisvif.view;

import javafx.scene.paint.Color;

/**
 * Constants used in {@link MapCanvas}
 * @see MapCanvas
 */
public final class CanvasConstants {

    /**
     * Minimum zoom scale factor
     */
    public static final double MIN_ZOOM_SCALE = 1d;

    /**
     * Maximum zoom scale factor
     */
    public static final double MAX_ZOOM_SCALE = 5d;

    /**
     * Value used to increment/decrement the zoom scale factor
     */
    public static final double DELTA_ZOOM_SCALE = 0.1d;

    /**
     * Radius used to render the nodes on the map (in pixels).
     */
    public static final int DELIVERY_NODE_RADIUS = 7;

    /**
     * Square of the radius used to render the nodes on the map (in pixels)
     */
    public static final int DELIVERY_NODE_SQUARED_RADIUS = DELIVERY_NODE_RADIUS * DELIVERY_NODE_RADIUS;

    /**
     * Diameter used to render the nodes on the map (in pixels).
     */
    public static final int DELIVERY_NODE_DIAMETER = DELIVERY_NODE_RADIUS * 2;

    /**
     * Opacity used for delivery nodes' colors
     */
    public static final double NODE_OPACITY = 0.7d;

    /**
     * Color used to render the sections of the map
     */
    public static final Color SECTION_COLOR = Color.grayRgb(85);

    /**
     * Color used to render the warehouse
     */
    public static final Color WAREHOUSE_COLOR = Color.rgb(
        255,
        255,
        255,
        CanvasConstants.NODE_OPACITY
    );

    private CanvasConstants() {

    }
}
