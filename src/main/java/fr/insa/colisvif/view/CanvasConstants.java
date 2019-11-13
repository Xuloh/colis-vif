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
    public static final double DELIVERY_NODE_RADIUS = 7d;

    public static final double DELIVERY_NODE_SELECTED_RADIUS_SCALE = 1.5d;

    /**
     * Radius used to render the city map nodes (in pixels)
     */
    public static final double CITY_MAP_NODE_RADIUS = DELIVERY_NODE_RADIUS / 2d;

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

    /**
     * Color used to render the city map nodes
     */
    public static final Color CITY_MAP_NODE_COLOR = Color.DARKORANGE;

    public static final Color ROUND_SECTION_COLOR = Color.LIME;

    public static final double ROUND_SECTION_WIDTH = 3d;

    private CanvasConstants() {

    }
}
