package fr.insa.colisvif.view;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Node;
import fr.insa.colisvif.model.Section;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A custom {@link BorderPane} that wraps and handles a {@link Canvas}
 * to render instances of {@link CityMap} and {@link DeliveryMap}.
 * @see Canvas
 */

// liste de Brice
// todo : ajouter une méthode drawRound

/* todo : Pour ce qui est des états, il va falloir que je change toutes les méthodes d'états de
 type 'Clic sur le canvas' en une seule méthode 'Clic gauche'. Ca implique que du coup le mapCanvas
 aient plusieurs méthodes qui me permettent de convertir un point de clic :
    - en Node (si le point cliqué est un Node on le renvoie)
    - en Step (si le point cliqué est un Vertex alors on renvoie la Step associé)
 */

public class MapCanvas extends BorderPane {

    private static final Logger LOGGER = LogManager.getLogger(MapCanvas.class);

    private CityMap cityMap;

    private DeliveryMap deliveryMap;

    private Canvas canvas;

    private GraphicsContext context;

    private DoubleProperty scale;

    private double baseZoom;

    private double originX;

    private double originY;

    private double dragOriginX;

    private double dragOriginY;

    private ToolsPane toolsPane;

    /**
     * Creates a new {@link MapCanvas} with a {@link ToolsPane}.
     * Equivalent to calling {@link MapCanvas#MapCanvas(boolean)}
     * with <code>true</code>.
     * @see ToolsPane
     */
    public MapCanvas() {
        this(true);
    }

    /**
     * Creates a new {@link MapCanvas}.
     * Optionaly adds a {@link ToolsPane} depending on the value of
     * <code>useTools</code>.
     *
     * @param useTools if <code>true</code>, adds a {@link ToolsPane} to
     * the {@link MapCanvas}
     *
     * @see ToolsPane
     */
    public MapCanvas(boolean useTools) {
        super();

        this.scale = new SimpleDoubleProperty();
        this.baseZoom = 1d;
        this.originX = 0d;
        this.originY = 0d;

        Pane canvasContainer = new Pane();
        this.canvas = new Canvas();
        this.context = this.canvas.getGraphicsContext2D();
        canvasContainer.getChildren().add(this.canvas);
        this.setCenter(canvasContainer);


        if (useTools) {
            this.toolsPane = new ToolsPane();
            this.setRight(toolsPane);

            this.scale.bindBidirectional(this.toolsPane.getZoomSlider().valueProperty());
            this.scale.addListener((observable, oldValue, newValue) -> {
                this.clearCanvas();
                this.drawCityMap();
                this.drawDeliveryMap();
            });

            this.toolsPane.getAutoZoomButton().addEventHandler(ActionEvent.ACTION, event -> {
                this.scale.set(1d);
                this.originX = 0;
                this.originY = 0;
                this.clearCanvas();
                this.drawCityMap();
                this.drawDeliveryMap();
            });

            this.toolsPane.getZoomInButton().addEventHandler(ActionEvent.ACTION, event -> {
                double scale = this.scale.get();
                scale = Math.min(scale + CanvasConstants.DELTA_ZOOM_SCALE, CanvasConstants.MAX_ZOOM_SCALE);
                this.scale.set(scale);
            });

            this.toolsPane.getZoomOutButton().addEventHandler(ActionEvent.ACTION, event -> {
                double scale = this.scale.get();
                scale = Math.max(scale - CanvasConstants.DELTA_ZOOM_SCALE, CanvasConstants.MIN_ZOOM_SCALE);
                this.scale.set(scale);
            });
        }

        this.scale.set(1d);

        this.canvas.heightProperty().bind(canvasContainer.heightProperty());
        this.canvas.widthProperty().bind(canvasContainer.widthProperty());

        InvalidationListener listener = observable -> this.onResize();
        this.canvas.heightProperty().addListener(listener);
        this.canvas.widthProperty().addListener(listener);

        this.canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                this.canvas.setCursor(Cursor.CLOSED_HAND);
                this.dragOriginX = this.originX - event.getX();
                this.dragOriginY = this.originY - event.getY();
            }
        });

        this.canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                this.originX = event.getX() + this.dragOriginX;
                this.originY = event.getY() + this.dragOriginY;
                this.clearCanvas();
                this.drawCityMap();
                this.drawDeliveryMap();
            }
        });

        this.canvas.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                this.canvas.setCursor(Cursor.DEFAULT);
            }
        });
    }

    /**
     * Clears the {@link Canvas}
     */
    public void clearCanvas() {
        LOGGER.debug("Clearing canvas");
        this.context.clearRect(
            0,
            0,
            this.canvas.getWidth(),
            this.canvas.getHeight()
        );
    }

    /**
     * Renders the {@link CityMap} on the {@link Canvas}.
     * If no {@link CityMap} has been specified with
     * {@link #setCityMap(CityMap)}, does nothing.
     *
     * @see #setCityMap(CityMap)
     */
    public void drawCityMap() {
        LOGGER.debug("Drawing CityMap");

        if (this.cityMap == null) {
            LOGGER.warn("Tried to draw CityMap but CityMap is null");
            return;
        }

        Map<Long, Node> nodes = this.cityMap.getMapNode();
        List<Section> sections = this.cityMap
            .getMapSection()
            .values()
            .stream()
            .reduce(new ArrayList<>(), (acc, val) -> {
                acc.addAll(val);
                return acc;
            });

        for (Section section : sections) {
            Node origin = nodes.get(section.getOrigin());
            Node destination = nodes.get(section.getDestination());
            this.drawLine(
                origin.getLatitude(),
                origin.getLongitude(),
                destination.getLatitude(),
                destination.getLongitude()
            );
        }
    }

    /**
     * Renders the {@link DeliveryMap} on the {@link Canvas}.
     * If no {@link DeliveryMap} has been specified with
     * {@link #setDeliveryMap(DeliveryMap)}, does nothing.
     *
     * @see #setDeliveryMap(DeliveryMap)
     */
    public void drawDeliveryMap() {
        LOGGER.debug("Drawing DeliveryMap");

        if (this.deliveryMap == null) {
            LOGGER.warn("Tried to draw DeliveryMap but DeliveryMap is null");
            return;
        }

        Map<Long, Node> nodes = this.cityMap.getMapNode();

        List<Node> pickupNodes = new ArrayList<>();
        List<Node> dropOffNodes = new ArrayList<>();

        this.deliveryMap.getDeliveryList()
            .forEach(delivery -> {
                dropOffNodes.add(nodes.get(delivery.getDropOffNodeId()));
                pickupNodes.add(nodes.get(delivery.getPickUpNodeId()));
            });

        ColorGenerator colorGenerator = new ColorGenerator(pickupNodes.size(), CanvasConstants.NODE_OPACITY);

        for (int i = 0; i < pickupNodes.size(); i++) {
            Node pickupNode = pickupNodes.get(i);
            Node dropOffNode = dropOffNodes.get(i);
            Color color = colorGenerator.next();

            this.drawTriangle(
                pickupNode.getLatitude(),
                pickupNode.getLongitude(),
                color
            );

            this.drawPoint(
                dropOffNode.getLatitude(),
                dropOffNode.getLongitude(),
                color,
                CanvasConstants.DELIVERY_NODE_SIZE
            );
        }

        Node warehouse = nodes.get(this.deliveryMap.getWarehouseNodeId());
        Color color = Color.rgb(255, 255, 255, CanvasConstants.NODE_OPACITY);

        this.drawSquare(
            warehouse.getLatitude(),
            warehouse.getLongitude(),
            color
        );
    }

    private void computeBaseZoom() {
        if (this.cityMap == null) {
            this.baseZoom = 1d;
        } else {
            final double MAP_WIDTH = this.cityMap.getLngMax() - this.cityMap.getLngMin();
            final double MAP_HEIGHT = this.cityMap.getLatMax() - this.cityMap.getLatMin();
            final double CANVAS_WIDTH = this.canvas.getWidth();
            final double CANVAS_HEIGHT = this.canvas.getHeight();

            if (CANVAS_WIDTH > CANVAS_HEIGHT) {
                this.baseZoom = CANVAS_HEIGHT / MAP_HEIGHT;
            } else {
                this.baseZoom = CANVAS_WIDTH / MAP_WIDTH;
            }
        }
    }

    private void onResize() {
        LOGGER.debug("Resizing map");
        this.computeBaseZoom();
        this.clearCanvas();
        this.drawCityMap();
        this.drawDeliveryMap();
    }

    /**
     * Assigns the given {@link CityMap} to this {@link MapCanvas}.
     * If <code>null</code> is passed, the {@link MapCanvas}
     * will stop rendering a {@link CityMap}.
     *
     * @param cityMap The {@link CityMap} to render on this {@link MapCanvas}
     */
    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
        this.computeBaseZoom();
    }

    /**
     * Returns the {@link CityMap} assigned to this {@link MapCanvas}
     * @return the {@link CityMap} assigned to this {@link MapCanvas}
     */
    public CityMap getCityMap() {
        return this.cityMap;
    }

    /**
     * Assigns the given {@link DeliveryMap} to this {@link MapCanvas}.
     * If <code>null</code> is passed, the {@link MapCanvas}
     * will stop rendering a {@link DeliveryMap}.
     *
     * @param deliveryMap The {@link DeliveryMap} to render on this {@link MapCanvas}
     */
    public void setDeliveryMap(DeliveryMap deliveryMap) {
        this.deliveryMap = deliveryMap;
    }

    /**
     * Returns the {@link DeliveryMap} assigned to this {@link MapCanvas}
     * @return the {@link DeliveryMap} assigned to this {@link MapCanvas}
     */
    public DeliveryMap getDeliveryMap() {
        return this.deliveryMap;
    }

    private void drawPoint(double lat, double lng, Paint paint, int size) {
        double x = this.lngToPx(lng);
        double y = this.latToPx(lat);

        Paint prevFill = this.context.getFill();
        this.context.setFill(paint);
        this.context.fillOval(
            x - size / 2d,
            y - size / 2d,
            size,
            size
        );
        this.context.setFill(prevFill);

        Paint prevStroke = this.context.getStroke();
        this.context.setStroke(Color.BLACK);
        this.context.strokeOval(
            x - size / 2d,
            y - size / 2d,
            size,
            size
        );
        this.context.setStroke(prevStroke);
    }

    private void drawLine(double lat1, double lng1, double lat2, double lng2) {
        this.drawLine(lat1, lng1, lat2, lng2, CanvasConstants.SECTION_COLOR);
    }

    private void drawLine(double lat1, double lng1, double lat2, double lng2, Paint paint) {
        double x1 = this.lngToPx(lng1);
        double y1 = this.latToPx(lat1);

        double x2 = this.lngToPx(lng2);
        double y2 = this.latToPx(lat2);

        Paint prevStroke = this.context.getStroke();
        this.context.setStroke(paint);
        this.context.strokeLine(x1, y1, x2, y2);
        this.context.setStroke(prevStroke);
    }

    private void drawTriangle(double lat, double lng, Paint paint) {
        double centerX = this.lngToPx(lng);
        double centerY = this.latToPx(lat);

        double[] x = new double[3];
        double[] y = new double[3];

        final int RADIUS = CanvasConstants.DELIVERY_NODE_SIZE / 2;

        for (int k = 0; k < 3; k++) {
            x[k] = RADIUS * Math.cos(2 * k * Math.PI / 3 - Math.PI / 2) + centerX;
            y[k] = RADIUS * Math.sin(2 * k * Math.PI / 3 - Math.PI / 2) + centerY;
        }

        Paint prevFill = this.context.getFill();
        this.context.setFill(paint);
        this.context.fillPolygon(x, y, 3);
        this.context.setStroke(prevFill);

        Paint prevStroke = this.context.getStroke();
        this.context.setStroke(Color.BLACK);
        this.context.strokePolygon(x, y, 3);
        this.context.setStroke(prevStroke);
    }

    private void drawSquare(double lat, double lng, Paint paint) {
        double centerX = this.lngToPx(lng);
        double centerY = this.latToPx(lat);

        final int RADIUS = CanvasConstants.DELIVERY_NODE_SIZE / 2;

        double[] x = {
            centerX - RADIUS,
            centerX + RADIUS,
            centerX + RADIUS,
            centerX - RADIUS
        };

        double[] y = {
            centerY - RADIUS,
            centerY - RADIUS,
            centerY + RADIUS,
            centerY + RADIUS
        };

        Paint prevFill = this.context.getFill();
        this.context.setFill(paint);
        this.context.fillPolygon(x, y, 4);
        this.context.setStroke(prevFill);

        Paint prevStroke = this.context.getStroke();
        this.context.setStroke(Color.BLACK);
        this.context.strokePolygon(x, y, 4);
        this.context.setStroke(prevStroke);
    }

    private double latToPx(double lat) {
        return (this.cityMap.getLatMax() - lat) * this.scale.get() * this.baseZoom + this.originY;
    }

    private double lngToPx(double lng) {
        return (lng - this.cityMap.getLngMin()) * this.scale.get() * this.baseZoom + this.originX;
    }
}
