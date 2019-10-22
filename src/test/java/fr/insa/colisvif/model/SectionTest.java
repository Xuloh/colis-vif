package fr.insa.colisvif.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class SectionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testSectionNegativeLength() {
        Section section = new Section(-20,"Rue de la Paix",100,101);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSectionZeroLength() {
        Section section = new Section(0,"Rue de la Paix",100,101);
    }

    @Test
    public void testSectionToString() {
        Section section = new Section(20,"Rue de la Paix",100,101);
        assertEquals("Length : 20.0 | Road Name : Rue de la Paix | Destination : 100 | Origin : 101\n",section.toString());
    }


}
