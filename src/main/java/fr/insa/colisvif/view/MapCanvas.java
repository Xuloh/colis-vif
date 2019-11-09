package fr.insa.colisvif.view;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.Node;
import fr.insa.colisvif.model.Section;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapCanvas extends BorderPane {

    private static final Color SECTION_COLOR = Color.grayRgb(85);

    private static final int DELIVERY_NODE_SIZE = 15;

    private CityMap cityMap;

    private DeliveryMap deliveryMap;

    private Canvas canvas;

    private GraphicsContext context;

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
        this.context = this.canvas.getGraphicsContext2D();
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
        this.context.clearRect(
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
    }

    public void drawDeliveryMap() {
        if (this.deliveryMap == null) {
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

        ColorGenerator colorGenerator = new ColorGenerator(pickupNodes.size(), 0.7);

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
                DELIVERY_NODE_SIZE
            );
        }
    }

    private void autoScale() {
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

    private void onResize() {
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
        this.drawLine(lat1, lng1, lat2, lng2, SECTION_COLOR);
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

        final int RADIUS = DELIVERY_NODE_SIZE / 2;

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

    private double latToPx(double lat) {
        return (this.cityMap.getLatMax() - lat) * this.scale + this.originY;
    }

    private double lngToPx(double lng) {
        return (lng - this.cityMap.getLngMin()) * this.scale + this.originX;
    }
}
