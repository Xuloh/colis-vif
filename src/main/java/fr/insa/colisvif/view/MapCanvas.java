package fr.insa.colisvif.view;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Node;
import fr.insa.colisvif.model.Section;
import javafx.beans.InvalidationListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapCanvas extends Pane {

    private CityMap cityMap;

    private Canvas canvas;

    private double scale;

    public MapCanvas() {
        this.canvas = new Canvas();
        this.scale = 1d;
        this.getChildren().add(this.canvas);

        this.canvas.heightProperty().bind(this.heightProperty());
        this.canvas.widthProperty().bind(this.widthProperty());

        InvalidationListener listener = observable -> this.onResize();
        this.canvas.heightProperty().addListener(listener);
        this.canvas.widthProperty().addListener(listener);
    }

    public void clearMap() {
        var context = this.canvas.getGraphicsContext2D();
        context.clearRect(0, 0,
                          this.canvas.getWidth(),
                          this.canvas.getHeight());
    }

    public void drawMap() {
        if (this.cityMap == null) {
            return;
        }

        var context = this.canvas.getGraphicsContext2D();

        final Color NODE_COLOR = Color.CORNFLOWERBLUE;
        final Color SECTION_COLOR = Color.BLACK;
        final int NODE_SIZE = 3;

        Map<Long, Node> nodes = this.cityMap.getMapNode();

        context.setFill(SECTION_COLOR);
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

            double x1 = (origin.getLongitude() - this.cityMap.getLngMin()) * this.scale;
            double y1 = (this.cityMap.getLatMax() - origin.getLatitude()) * this.scale;

            double x2 = (destination.getLongitude() - this.cityMap.getLngMin())
                        * this.scale;
            double y2 = (this.cityMap.getLatMax() - destination.getLatitude())
                        * this.scale;

            context.strokeLine(x1, y1, x2, y2);
        }

        context.setFill(NODE_COLOR);
        for (Node node : nodes.values()) {
            double x = (node.getLongitude() - this.cityMap.getLngMin()) * this.scale;
            double y = (this.cityMap.getLatMax() - node.getLatitude()) * this.scale;
            context.fillOval(x - NODE_SIZE / 2d,
                             y - NODE_SIZE / 2d,
                             NODE_SIZE,
                             NODE_SIZE);
        }
    }

    public void setCityMap(CityMap map) {
        this.cityMap = map;
        this.autoScale();
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
        this.clearMap();
        this.drawMap();
    }

    public CityMap getCityMap() {
        return this.cityMap;
    }
}
