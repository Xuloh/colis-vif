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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A custom {@link BorderPane} that wraps and handles a {@link Canvas}
 * to render instances of {@link CityMap} and {@link DeliveryMap}.
 * @see Canvas
 */

// liste de Brice
// todo : ajouter une méthode drawRound

/* todo : Pour ce qui est des états, il va falloir que je change toutes les méthodes d'états de
 type 'Clic sur le canvas' en une seule méthode 'Clic gauche'. Ca implique que j'aient plusieurs méthodes
 qui me permettent de convertir un point de clic :
    - en Node (si le point cliqué est un Node on le renvoie)
    - en Step (si le point cliqué est un Vertex alors on renvoie la Step associé)

    Ca implique de pouvoir passer les clics de souris en paramètres. (coordonnées dans le canvas ?)

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

    private List<CanvasNode> deliveryCanvasNodes;

    private List<CanvasNode> cityMapCanvasNodes;

    private List<Section> mapSections;

    private boolean showCityMapNodesOnHover;

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
        this.deliveryCanvasNodes = new ArrayList<>();
        this.cityMapCanvasNodes = new ArrayList<>();
        this.showCityMapNodesOnHover = false;

        // wrap canvas in a pane to handle resize
        Pane canvasContainer = new Pane();
        this.canvas = new Canvas();
        this.context = this.canvas.getGraphicsContext2D();
        canvasContainer.getChildren().add(this.canvas);
        this.setCenter(canvasContainer);

        if (useTools) {
            this.createToolsPane();
        }

        this.scale.set(1d);

        // canvas resize listeners
        this.canvas.heightProperty().bind(canvasContainer.heightProperty());
        this.canvas.widthProperty().bind(canvasContainer.widthProperty());

        InvalidationListener listener = observable -> this.onResize();
        this.canvas.heightProperty().addListener(listener);
        this.canvas.widthProperty().addListener(listener);

        // canvas mouse events handlers
        this.canvas.setOnMousePressed(this::canvasOnMousePressed);
        this.canvas.setOnMouseDragged(this::canvasOnMouseDragged);
        this.canvas.setOnMouseReleased(this::canvasOnMouseReleased);
        this.canvas.setOnMouseMoved(this::canvasOnMouseMoved);
    }

    /**
     * Clears the canvas and redraws everything
     *
     * @see #clearCanvas()
     * @see #drawCityMap()
     * @see #drawDeliveryMap()
     * @see #drawCityMapNodesOverlay()
     */
    public void redraw() {
        LOGGER.info("Rendering canvas");
        long start = System.nanoTime();
        this.clearCanvas();
        this.drawCityMap();
        this.drawDeliveryMap();
        this.drawCityMapNodesOverlay();
        long renderTimeMillis = (System.nanoTime() - start) / 1_000_000;
        LOGGER.debug("Rendered canvas in {} ms", renderTimeMillis);
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
        LOGGER.debug("Rendering CityMap");

        if (this.cityMap == null) {
            LOGGER.warn("Tried to draw CityMap but CityMap is null");
            return;
        }

        Map<Long, Node> nodes = this.cityMap.getMapNode();

        for (Section section : this.mapSections) {
            Node origin = nodes.get(section.getOrigin());
            Node destination = nodes.get(section.getDestination());
            this.drawLineLatLng(
                origin.getLatitude(),
                origin.getLongitude(),
                destination.getLatitude(),
                destination.getLongitude(),
                CanvasConstants.SECTION_COLOR
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
        LOGGER.debug("Rendering DeliveryMap");

        if (this.deliveryMap == null) {
            LOGGER.warn("Tried to draw DeliveryMap but DeliveryMap is null");
            return;
        }

        for (CanvasNode canvasNode : this.deliveryCanvasNodes) {
            canvasNode.updateCoords();
            canvasNode.draw();
        }
    }

    /**
     * Renders the nodes hovered by the mouse.
     */
    public void drawCityMapNodesOverlay() {
        for (CanvasNode canvasNode : this.cityMapCanvasNodes) {
            canvasNode.updateCoords();
            canvasNode.draw();
        }
    }

    /**
     * Changes the mode to show city map nodes when the mouse hovers them.
     * @param showCityMapNodesOnHover true to activate the mode, false to deactivate
     */
    public void setShowCityMapNodesOnHover(boolean showCityMapNodesOnHover) {
        this.showCityMapNodesOnHover = showCityMapNodesOnHover;
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

        if (this.cityMap != null) {
            this.mapSections = this.cityMap
                .getMapSection()
                .values()
                .stream()
                .reduce(new ArrayList<>(), (acc, val) -> {
                    acc.addAll(val);
                    return acc;
                });

            this.cityMapCanvasNodes = this.cityMap
                .getMapNode()
                .values()
                .stream()
                .map(node -> new CanvasNode(
                    node.getId(),
                    NodeType.CITY_MAP_NODE,
                    CanvasConstants.CITY_MAP_NODE_COLOR,
                    CanvasConstants.CITY_MAP_NODE_RADIUS
                ))
                .collect(Collectors.toList());
        }
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

        if (this.deliveryMap != null) {
            ColorGenerator colorGenerator = new ColorGenerator(
                this.deliveryMap.getSize(),
                CanvasConstants.NODE_OPACITY,
                2
            );

            this.deliveryMap
                .getDeliveryList()
                .stream()
                .flatMap(delivery -> Stream.of(delivery.getPickUp(), delivery.getDropOff()))
                .map(vertex -> new CanvasNode(
                    vertex.getNodeId(),
                    vertex.isPickUp() ? NodeType.DELIVERY_PICKUP : NodeType.DELIVERY_DROP_OFF,
                    colorGenerator.next(),
                    CanvasConstants.DELIVERY_NODE_RADIUS
                ))
                .forEach(canvasNode -> this.deliveryCanvasNodes.add(canvasNode));

            this.deliveryCanvasNodes.add(new CanvasNode(
                this.deliveryMap.getWarehouseNodeId(),
                NodeType.DELIVERY_WAREHOUSE,
                CanvasConstants.WAREHOUSE_COLOR,
                CanvasConstants.DELIVERY_NODE_RADIUS
            ));
        }
    }

    /**
     * Returns the {@link DeliveryMap} assigned to this {@link MapCanvas}
     * @return the {@link DeliveryMap} assigned to this {@link MapCanvas}
     */
    public DeliveryMap getDeliveryMap() {
        return this.deliveryMap;
    }

    private void canvasOnMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.MIDDLE) {
            this.canvas.setCursor(Cursor.CLOSED_HAND);
            this.dragOriginX = this.originX - event.getX();
            this.dragOriginY = this.originY - event.getY();
        }
    }

    private void canvasOnMouseDragged(MouseEvent event) {
        if (event.getButton() == MouseButton.MIDDLE) {
            this.originX = event.getX() + this.dragOriginX;
            this.originY = event.getY() + this.dragOriginY;
            this.redraw();
        }
    }

    private void canvasOnMouseReleased(MouseEvent event) {
        if (event.getButton() == MouseButton.MIDDLE) {
            this.canvas.setCursor(Cursor.DEFAULT);
        }
    }

    private void canvasOnMouseMoved(MouseEvent event) {
        LOGGER.trace("Updating hovered nodes");
        long start = System.nanoTime();

        boolean foundIntersect = false;

        Iterator<CanvasNode> it = this.showCityMapNodesOnHover
            ? this.cityMapCanvasNodes.iterator()
            : this.deliveryCanvasNodes.iterator();

        while (it.hasNext()) {
            CanvasNode canvasNode = it.next();

            if (canvasNode.intersects(event.getX(), event.getY())) {
                LOGGER.trace("CanvasNode intersection : " + canvasNode);
                this.canvas.setCursor(Cursor.HAND);
                canvasNode.selected = true;
                foundIntersect = true;
            } else {
                canvasNode.selected = false;
            }
        }

        if (!foundIntersect) {
            this.canvas.setCursor(Cursor.DEFAULT);
        }

        this.redraw();

        long computeTimeMillis = (System.nanoTime() - start) / 1_000_000;
        LOGGER.trace("Updated hovered nodes in {} ms", computeTimeMillis);
    }

    private void createToolsPane() {
        ToolsPane toolsPane = new ToolsPane();
        this.setRight(toolsPane);

        this.scale.bindBidirectional(toolsPane.getZoomSlider().valueProperty());
        this.scale.addListener((observable, oldValue, newValue) -> {
            this.redraw();
        });

        toolsPane.getAutoZoomButton().addEventHandler(ActionEvent.ACTION, event -> {
            this.scale.set(1d);
            this.originX = 0;
            this.originY = 0;
            this.redraw();
        });

        toolsPane.getZoomInButton().addEventHandler(ActionEvent.ACTION, event -> {
            double scale = this.scale.get();
            scale = Math.min(scale + CanvasConstants.DELTA_ZOOM_SCALE, CanvasConstants.MAX_ZOOM_SCALE);
            this.scale.set(scale);
        });

        toolsPane.getZoomOutButton().addEventHandler(ActionEvent.ACTION, event -> {
            double scale = this.scale.get();
            scale = Math.max(scale - CanvasConstants.DELTA_ZOOM_SCALE, CanvasConstants.MIN_ZOOM_SCALE);
            this.scale.set(scale);
        });
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
        this.redraw();
    }

    private void drawCircleLatLng(double lat, double lng, Paint paint, double radius) {
        this.drawCircle(this.lngToPx(lng), this.latToPx(lat), paint, radius);
    }

    private void drawCircle(double x, double y, Paint paint, double radius) {
        Paint prevFill = this.context.getFill();
        this.context.setFill(paint);
        this.context.fillOval(
            x - radius,
            y - radius,
            radius * 2d,
            radius * 2d
        );
        this.context.setFill(prevFill);

        Paint prevStroke = this.context.getStroke();
        this.context.setStroke(Color.BLACK);
        this.context.strokeOval(
            x - radius,
            y - radius,
            radius * 2d,
            radius * 2d
        );
        this.context.setStroke(prevStroke);
    }

    private void drawLineLatLng(double lat1, double lng1, double lat2, double lng2, Paint paint) {
        this.drawLine(this.lngToPx(lng1), this.latToPx(lat1), this.lngToPx(lng2), this.latToPx(lat2), paint);
    }

    private void drawLine(double x1, double y1, double x2, double y2, Paint paint) {
        Paint prevStroke = this.context.getStroke();
        this.context.setStroke(paint);
        this.context.strokeLine(x1, y1, x2, y2);
        this.context.setStroke(prevStroke);
    }

    private void drawTriangleLatLng(double lat, double lng, Paint paint, double radius) {
        this.drawTriangle(this.lngToPx(lng), this.latToPx(lat), paint, radius);
    }

    private void drawTriangle(double centerX, double centerY, Paint paint, double radius) {
        double[] x = new double[3];
        double[] y = new double[3];

        for (int k = 0; k < 3; k++) {
            x[k] = radius * Math.cos(2 * k * Math.PI / 3 - Math.PI / 2) + centerX;
            y[k] = radius * Math.sin(2 * k * Math.PI / 3 - Math.PI / 2) + centerY;
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

    private void drawSquareLatLng(double lat, double lng, Paint paint, double radius) {
        this.drawSquare(this.lngToPx(lng), this.latToPx(lat), paint, radius);
    }

    private void drawSquare(double centerX, double centerY, Paint paint, double radius) {
        double[] x = {
            centerX - radius,
            centerX + radius,
            centerX + radius,
            centerX - radius
        };

        double[] y = {
            centerY - radius,
            centerY - radius,
            centerY + radius,
            centerY + radius
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

    private class CanvasNode {

        /*package-private*/ double x;

        /*package-private*/ double y;

        /*package-private*/ boolean selected;

        private long nodeId;

        private NodeType type;

        private Paint paint;

        private double radius;

        /*package-private*/ CanvasNode(long nodeId, NodeType type, Paint paint, double radius) {
            this.nodeId = nodeId;
            this.type = type;
            this.paint = paint;
            this.x = 0;
            this.y = 0;
            this.selected = false;
            this.radius = radius;
        }

        /*package-private*/ void updateCoords() {
            Node node = cityMap.getMapNode().get(this.nodeId);
            this.x = lngToPx(node.getLongitude());
            this.y = latToPx(node.getLatitude());
        }

        /*package-private*/ void draw() {
            switch (this.type) {
            case DELIVERY_PICKUP:
                drawTriangle(
                    this.x,
                    this.y,
                    this.paint,
                    this.radius
                );
                break;
            case DELIVERY_DROP_OFF:
                drawCircle(
                    this.x,
                    this.y,
                    this.paint,
                    this.radius
                );
                break;
            case DELIVERY_WAREHOUSE:
                drawSquare(
                    this.x,
                    this.y,
                    this.paint,
                    this.radius
                );
                break;
            case CITY_MAP_NODE:
                if (this.selected) {
                    drawCircle(
                        this.x,
                        this.y,
                        this.paint,
                        this.radius
                    );
                }
                break;
            default:
                break;
            }
        }

        /*package-private*/ boolean intersects(double x, double y) {
            double squaredDistance = (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
            return squaredDistance <= CanvasConstants.DELIVERY_NODE_SQUARED_RADIUS;
        }

        @Override
        public String toString() {
            return "CanvasNode{" + "x=" + x + ", y=" + y + ", selected=" + selected + ", nodeId=" + nodeId + ", type=" + type + '}';
        }
    }
}
