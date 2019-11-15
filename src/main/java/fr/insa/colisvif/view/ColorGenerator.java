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

    private int duplicate;

    private double hue;

    private double saturation;

    private double brightness;

    /**
     * Creates a new {@link ColorGenerator}
     * to generate the given number of colors
     * @param count the number of colors to generate
     */
    public ColorGenerator(int count) {
        this(count, 1.0, 1);
    }

    /**
     * Creates a new {@link ColorGenerator}
     * to generate the given number of colors
     * and applying the given opacity.
     * @param count the number of colors to generate
     * @param opacity the opacity to apply to the colors
     */
    public ColorGenerator(int count, double opacity) {
        this(count, opacity, 1);
    }

    /**
     * Creates a new {@link ColorGenerator}
     * to generate the given number of colors
     * and applying the given opacity. Each color will be returned in sequence
     * as much time as the value of <code>duplicate</code>.
     * @param count the number of colors to generate
     * @param opacity the opacity to apply to the colors
     * @param duplicate the number of times each color is to be generated
     */
    public ColorGenerator(int count, double opacity, int duplicate) {
        this.count = count * duplicate;
        this.current = 0;
        this.opacity = opacity;
        this.duplicate = duplicate;
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

        if (this.current % this.duplicate == 0) {
            this.hue = this.current * 360 / this.count;
            this.saturation = 1.0;
            this.brightness = 1.0;
        }

        this.current++;
        return Color.hsb(
            this.hue,
            this.saturation,
            this.brightness,
            this.opacity
        );
    }
}
