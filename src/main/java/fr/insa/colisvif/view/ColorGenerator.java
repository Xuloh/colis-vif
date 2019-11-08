package fr.insa.colisvif.view;

import javafx.scene.paint.Color;

import java.util.Iterator;

public class ColorGenerator implements Iterator<Color> {

    private int count;

    private int current;

    private double opacity;

    public ColorGenerator(int count) {
        this(count, 1.0);
    }

    public ColorGenerator(int count, double opacity) {
        this.count = count;
        this.current = 0;
        this.opacity = opacity;
    }

    @Override
    public boolean hasNext() {
        return this.current < this.count;
    }

    @Override
    public Color next() {
        double hue = this.current * 360 / this.count;
        double saturation = 1.0;
        double brightness = 1.0;

        this.current++;

        return Color.hsb(hue, saturation, brightness, this.opacity);
    }
}
