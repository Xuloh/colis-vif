package fr.insa.colisvif.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SectionTest {

    @Test
    public void testSectionGood() {
        new Section(20, "Rue de la Paix", 100, 101);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSectionNegativeLength() {
        new Section(-20, "Rue de la Paix", 100, 101);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSectionZeroLength() {
        new Section(0, "Rue de la Paix", 100, 101);
    }

    @Test
    public void testSectionToString() {
        Section section = new Section(20, "Rue de la Paix", 101, 100);
        String expected = "Section{"
            + "length=" + 20.0
            + ", roadName='" + "Rue de la Paix" + '\''
            + ", origin=" + 101
            + ", destination=" + 100
            + '}';

        assertEquals(expected, section.toString());
    }


    @Test
    public void getLength() {
        long expected = 20;
        Section section = new Section(expected, "Rue de la Paix", 100, 101);
        assertEquals(expected, section.getLength(), 0.001);
    }

    @Test
    public void getRoadName() {
        String expected = "Rue de la Paix";
        Section section = new Section(20, expected, 100, 101);
        assertEquals(expected, section.getRoadName());
    }

    @Test
    public void getDestination() {
        long expected = 100;
        Section section = new Section(20, "Rue de la Paix", 101, expected);
        assertEquals(expected, section.getDestination());
    }

    @Test
    public void getDestinationWrong() {
        long expected = 100;
        Section section = new Section(20, "Rue de la Paix", 101, expected + 1);
        assertNotEquals(expected, section.getDestination());
    }

    @Test
    public void getOrigin() {
        long expected = 100;
        Section section = new Section(20, "Rue de la Paix", expected, 101);
        assertEquals(expected, section.getOrigin());
    }

    @Test
    public void getOriginWrong() {
        long expected = 100;
        Section section = new Section(20, "Rue de la Paix", expected + 1, 101);
        assertNotEquals(expected, section.getOrigin());
    }

    @Test
    public void testEquals() {
        Section section = new Section(20, "Rue de la Paix", 100, 101);
        Section expected = new Section(20, "Rue de la Paix", 100, 101);
        assertEquals(expected, section);
        assertEquals(section, section);

        expected = new Section(20, "Rue de la Paix", 101, 101);
        assertNotEquals(expected, section);
        expected = new Section(20, "Rue de la Paix", 100, 100);
        assertNotEquals(expected, section);
        assertNotEquals(expected, 1);
    }
}
