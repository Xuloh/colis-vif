package fr.insa.colisvif.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class SectionTest {

    @Test
    public void testSectionGood() {
        Section section = new Section(20, "Rue de la Paix", 100, 101);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSectionNegativeLength() {
        Section section = new Section(-20, "Rue de la Paix", 100, 101);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSectionZeroLength() {
        Section section = new Section(0, "Rue de la Paix", 100, 101);
    }

    /*
    @Test(expected = IllegalArgumentException.class)
    public void testSectionOriginSameAsDestination() {
        Section section = new Section(0,"Rue de la Paix",100,100);
    }
    */

    @Test
    public void testSectionToString() {
        Section section = new Section(20, "Rue de la Paix", 100, 101);
        assertEquals("Length : 20.0 | Road Name : Rue de la Paix | Destination : 100 | Origin : 101\n", section.toString());
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
        Section section = new Section(20, "Rue de la Paix", expected, 101);
        assertEquals(expected, section.getDestination());
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
    }
}
