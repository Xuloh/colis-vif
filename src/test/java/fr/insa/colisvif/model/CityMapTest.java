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
    public void testCreateSectionUpdateSection() throws IdError {
        CityMap citymap = new CityMap();
        citymap.createNode(101, -60, 120);
        citymap.createNode(100, -66, 120);
        citymap.createNode(99, -65, 120);
        citymap.createSection(10, "Rue de St-Germain", 101, 100);
        citymap.createSection(10, "Rue de St-Germain", 100, 99);

        assertNotNull(citymap.getMapSection().get("Rue de St-Germain"));
        assertEquals("[Length : 10.0 | Road Name : Rue de St-Germain | Destination : 101 | Origin : 100\n" +
                        ", Length : 10.0 | Road Name : Rue de St-Germain | Destination : 100 | Origin : 99\n]"
                , citymap.getMapSection().get("Rue de St-Germain").toString());
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
