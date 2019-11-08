package fr.insa.colisvif.view;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Node;
import fr.insa.colisvif.model.Section;
import javafx.beans.InvalidationListener;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapCanvas extends BorderPane {

    private static final Color NODE_COLOR = Color.CORNFLOWERBLUE;

    private static final Color SECTION_COLOR = Color.BLACK;

    private static final Color DELIVERY_COLOR = Color.RED;

    private static final Color PICKUP_COLOR = Color.GREEN;

    private static final int NODE_SIZE = 3;

    private CityMap cityMap;

    private DeliveryMap deliveryMap;

    private Canvas canvas;

    private double scale;

    private double originX;

    private double originY;

    public MapCanvas() {
        this(true);
    }

    public MapCanvas(boolean useTools) {
        super();

        Pane canvasContainer = new Pane();
        this.canvas = new Canvas();
        canvasContainer.getChildren().add(this.canvas);
        this.setCenter(canvasContainer);


        if (useTools) {
            this.setRight(new ToolsPane());
        }

        this.canvas.heightProperty().bind(canvasContainer.heightProperty());
        this.canvas.widthProperty().bind(canvasContainer.widthProperty());

        InvalidationListener listener = observable -> this.onResize();
        this.canvas.heightProperty().addListener(listener);
        this.canvas.widthProperty().addListener(listener);

        this.scale = 1d;
        this.originX = 0d;
        this.originY = 0d;
    }

    public void clearCanvas() {
        var context = this.canvas.getGraphicsContext2D();
        context.clearRect(
            0,
            0,
            this.canvas.getWidth(),
            this.canvas.getHeight()
        );
    }

    public void drawCityMap() {
        if (this.cityMap == null) {
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

        for (Node node : nodes.values()) {
            this.drawPoint(node.getLatitude(), node.getLongitude());
        }
    }

    public void drawDeliveryMap() {
        if (this.deliveryMap == null) {
            return;
        }

        Map<Long, Node> nodes = this.cityMap.getMapNode();

        List<Node> pickupNodes = new ArrayList<>();
        List<Node> deliveryNodes = new ArrayList<>();

        this.deliveryMap.getDeliveryList()
            .forEach(delivery -> {
                deliveryNodes.add(nodes.get(delivery.getDeliveryNodeId()));
                pickupNodes.add(nodes.get(delivery.getPickUpNodeId()));
            });

        for (Node pickupNode : pickupNodes) {
            this.drawPoint(
                pickupNode.getLatitude(),
                pickupNode.getLongitude(),
                PICKUP_COLOR
            );
        }

        for (Node deliveryNode : deliveryNodes) {
            this.drawPoint(
                deliveryNode.getLatitude(),
                deliveryNode.getLongitude(),
                DELIVERY_COLOR
            );
        }
    }

    public void autoScale() {
        final double MAP_WIDTH = this.cityMap.getLngMax() - this.cityMap.getLngMin();
        final double MAP_HEIGHT = this.cityMap.getLatMax() - this.cityMap.getLatMin();
        final double CANVAS_WIDTH = this.canvas.getWidth();
        final double CANVAS_HEIGHT = this.canvas.getHeight();

        if (CANVAS_WIDTH > CANVAS_HEIGHT) {
            this.scale = CANVAS_HEIGHT / MAP_HEIGHT;
        } else {
            this.scale = CANVAS_WIDTH / MAP_WIDTH;
        }
    }

    public void onResize() {
        this.clearCanvas();
        this.drawCityMap();
        this.drawDeliveryMap();
    }

    public void setCityMap(CityMap map) {
        this.cityMap = map;
        this.autoScale();
    }

    public CityMap getCityMap() {
        return this.cityMap;
    }

    public void setDeliveryMap(DeliveryMap deliveryMap) {
        this.deliveryMap = deliveryMap;
    }

    public DeliveryMap getDeliveryMap() {
        return this.deliveryMap;
    }

    private void drawPoint(double lat, double lng) {
        this.drawPoint(lat, lng, NODE_COLOR);
    }

    private void drawPoint(double lat, double lng, Paint paint) {
        double x = this.lngToPx(lng);
        double y = this.latToPx(lat);

        var context = this.canvas.getGraphicsContext2D();
        Paint prevFill = context.getFill();
        context.setFill(paint);
        context.fillOval(
            x - NODE_SIZE / 2d,
            y - NODE_SIZE / 2d,
            NODE_SIZE,
            NODE_SIZE
        );
        context.setFill(prevFill);
    }

    private void drawLine(double lat1, double lng1, double lat2, double lng2) {
        this.drawLine(lat1, lng1, lat2, lng2, SECTION_COLOR);
    }

    private void drawLine(double lat1, double lng1, double lat2, double lng2, Paint paint) {
        double x1 = this.lngToPx(lng1);
        double y1 = this.latToPx(lat1);

        double x2 = this.lngToPx(lng2);
        double y2 = this.latToPx(lat2);

        var context = this.canvas.getGraphicsContext2D();
        Paint prevFill = context.getFill();
        context.setFill(paint);
        context.strokeLine(x1, y1, x2, y2);
        context.setFill(prevFill);
    }

    private double latToPx(double lat) {
        return (this.cityMap.getLatMax() - lat) * this.scale + this.originY;
    }

    private double lngToPx(double lng) {
        return (lng - this.cityMap.getLngMin()) * this.scale + this.originX;
    }
}
