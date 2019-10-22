package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import org.junit.Test;

import static org.junit.Assert.*;

public class CityMapTest {

    @Test
    public void testUpdateLatMax() {
        CityMap citymap = new CityMap();
        double LONGMIN = citymap.getLongMin();
        citymap.createNode(2405632, -60, 180);
        assert(-60 == citymap.getLatMax() && citymap.getLongMin() == LONGMIN);
    }

    @Test
    public void testUpdateLongMin() {
        CityMap citymap = new CityMap();
        double LATMAX = citymap.getLatMax();
        citymap.createNode(2405632, -90, 120);
        assert(120 == citymap.getLongMin() && citymap.getLatMax() == LATMAX);
    }

    @Test
    public void testUpdateLatMaxLongMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -60, 120);
        assert(-60 == citymap.getLatMax() && citymap.getLongMin() == 120) ;
    }

    @Test
    public void testCreateSectionNewSection() throws IdError {
        CityMap citymap = new CityMap();
        citymap.createNode(101, -60, 120);
        citymap.createNode(100, -66, 120);
        citymap.createSection(10, "Rue de St-Germain",  101,100);
        assertNotNull(citymap.getMapSection().get("Rue de St-Germain")) ;
        assertEquals( "[Length : 10.0 | Road Name : Rue de St-Germain | Destination : 101 | Origin : 100\n]",citymap.getMapSection().get("Rue de St-Germain").toString());
    }

    @Test
    public void testCreateSectionUpdateection() throws IdError {
        CityMap citymap = new CityMap();
        citymap.createNode(101, -60, 120);
        citymap.createNode(100, -66, 120);
        citymap.createNode(99, -65, 120);
        citymap.createSection(10, "Rue de St-Germain",  101,100);
        citymap.createSection(10, "Rue de St-Germain",  100,99);

        assertNotNull(citymap.getMapSection().get("Rue de St-Germain")) ;
        assertEquals( "[Length : 10.0 | Road Name : Rue de St-Germain | Destination : 101 | Origin : 100\n" +
                        ", Length : 10.0 | Road Name : Rue de St-Germain | Destination : 100 | Origin : 99\n]"
                ,citymap.getMapSection().get("Rue de St-Germain").toString());
    }

}
