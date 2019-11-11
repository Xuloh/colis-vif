package fr.insa.colisvif.view;

import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator that generates colors spread evenly on the color wheel.
 * @see <a href="https://en.wikipedia.org/wiki/Color_wheel">The color wheel</a>
 */
public class ColorGenerator implements Iterator<Color> {

    private int count;

    private int current;

    private double opacity;

    /**
     * Creates a new {@link ColorGenerator} to generate the given number of colors
     * @param count the number of colors to generate
     */
    public ColorGenerator(int count) {
        this(count, 1.0);
    }

    /**
     * Creates a new {@link ColorGenerator} to generate the given number of colors
     * and applying the given opacity.
     * @param count the number of colors to generate
     * @param opacity the opacity to apply to the colors
     */
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
        if (this.current >= this.count) {
            throw new NoSuchElementException("No more colors to generate");
        }

        double hue = this.current * 360 / this.count;
        double saturation = 1.0;
        double brightness = 1.0;

        this.current++;

        return Color.hsb(hue, saturation, brightness, this.opacity);
    }
}
