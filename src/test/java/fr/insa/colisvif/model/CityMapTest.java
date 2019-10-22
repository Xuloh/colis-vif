package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import org.junit.Test;
import static org.junit.Assert.*;

public class CityMapTest {

    @Test
    public void testUpdateLatMax() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -60, 24);

        int expected = -60;
        assert(expected == citymap.getLatMax());
    }

    @Test
    public void testUpdateLongMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -90, 120);

        int expected = 120;
        assert(expected == citymap.getLongMin());
    }

    @Test
    public void testUpdateLatMaxLongMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -60, 120);
        assert(-60 == citymap.getLatMax() && citymap.getLongMin() == 120) ;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBadNode() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -140, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBadSection() throws IdError {
        CityMap citymap = new CityMap();
        citymap.createSection(-240, "Supé Rue", 120, 124);
    }

    @Test
    public void testCityMapToString() throws IdError {
        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2, 1, 1);
        citymap.createNode(3, 2, 2);
        citymap.createSection(200, "Rue Antoine Tout Court", 2, 1);
        citymap.createSection(100, "Rue Antoine Tout Court", 3, 2);

        String expected = "Nodes : \n" +
                "ID : 1 | Latitude : 0.0 | Longitude : 0.0\n"+
                "ID : 2 | Latitude : 1.0 | Longitude : 1.0\n"+
                "ID : 3 | Latitude : 2.0 | Longitude : 2.0\n\n"+
                "Sections : \n" +
                "Length : 200.0 | Road Name : Rue Antoine Tout Court | Destination : 2 | Origin : 1\n"+
                "Length : 100.0 | Road Name : Rue Antoine Tout Court | Destination : 3 | Origin : 2\n";
        assertEquals("To String from CityMap is OK", expected, citymap.toString());

    }

}
