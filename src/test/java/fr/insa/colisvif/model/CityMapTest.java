package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CityMapTest {

    @Test
    public void testUpdateLatMax() {
        CityMap citymap = new CityMap();
        final double LNG_MIN = citymap.getLngMin();
        citymap.createNode(2405632, -60, 180);
        assertTrue(-60 == citymap.getLatMax() && citymap.getLngMin() == LNG_MIN);
    }

    @Test
    public void testUpdateLongMin() {
        CityMap citymap = new CityMap();
        final double LAT_MAX = citymap.getLatMax();
        citymap.createNode(2405632, -90, 120);
        assertTrue(120 == citymap.getLngMin() && citymap.getLatMax() == LAT_MAX);
    }

    @Test
    public void testUpdateLatMaxLongMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(2405632, -60, 120);
        assertTrue(-60 == citymap.getLatMax() && citymap.getLngMin() == 120);
    }

    @Test
    public void testCreateSectionNewSection() throws IdError {
        CityMap citymap = new CityMap();
        citymap.createNode(101, -60, 120);
        citymap.createNode(100, -66, 120);
        citymap.createSection(10, "Rue de St-Germain", 101, 100);
        assertNotNull(citymap.getMapSection().get("Rue de St-Germain"));
        assertEquals("[Length : 10.0 | Road Name : Rue de St-Germain | Destination : 101 | Origin : 100\n]", citymap.getMapSection().get("Rue de St-Germain").toString());
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
        assertEquals("[Length : 10.0 | Road Name : Rue de St-Germain | Destination : 101 | Origin : 100\n"
                     + ", Length : 10.0 | Road Name : Rue de St-Germain | Destination : 100 | Origin : 99\n]",
                     citymap.getMapSection().get("Rue de St-Germain").toString());
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

        String expected = "Nodes : \n"
                          + "ID : 1 | Latitude : 0.0 | Longitude : 0.0\n"
                          + "ID : 2 | Latitude : 1.0 | Longitude : 1.0\n"
                          + "ID : 3 | Latitude : 2.0 | Longitude : 2.0\n\n"
                          + "Sections : \n"
                          + "Length : 200.0 | Road Name : Rue Antoine Tout Court | Destination : 2 | Origin : 1\n"
                          + "Length : 100.0 | Road Name : Rue Antoine Tout Court | Destination : 3 | Origin : 2\n";
        assertEquals("To String from CityMap is OK", expected, citymap.toString());


    }

    @Test
    public void testEquals() throws IdError  {
        CityMap citymap_1 = new CityMap();
        citymap_1.createNode(1, 0, 0);
        citymap_1.createNode(2, 1, 1);
        citymap_1.createNode(3, 2, 2);
        citymap_1.createSection(200, "Rue Antoine Tout Court", 2, 1);
        citymap_1.createSection(100, "Rue Antoine Tout Court", 3, 2);

        CityMap citymap_2 = new CityMap();
        citymap_2.createNode(1, 0, 0);
        citymap_2.createNode(2, 1, 1);
        citymap_2.createNode(3, 2, 2);
        citymap_2.createSection(200, "Rue Antoine Tout Court", 2, 1);
        citymap_2.createSection(100, "Rue Antoine Tout Court", 3, 2);

        assertEquals(true,citymap_1.equals(citymap_2));

    }

    @Test
    public void getLngMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2, 1, 1);
        citymap.createNode(3, 2, 2);
        assert(0 == citymap.getLngMin());
    }

    @Test
    public void getLngMax() {
        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2, 1, 1);
        citymap.createNode(3, 2, 2);
        assert(2 == citymap.getLngMax());
    }

    @Test
    public void getLatMin() {
        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2, 1, 1);
        citymap.createNode(3, 2, 2);
        assert(0 == citymap.getLatMin());
    }

    @Test
    public void getLatMax() {
        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2, 1, 1);
        citymap.createNode(3, 2, 2);
        assert(2 == citymap.getLatMax());
    }

    @Test
    public void getMapNode() {
        HashMap<Long,Node> test_map = new HashMap<Long,Node>();
        test_map.put((long) 1,new Node(1,0,0));
        test_map.put((long) 2,new Node(2,4,10));
        test_map.put((long) 3,new Node(3,56,20));

        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2,4,10);
        citymap.createNode(3,56,20);
        assertEquals(test_map , citymap.getMapNode());
    }

    @Test
    public void getMapSection() {
        HashMap<Long,Node> test_map = new HashMap<Long,Node>();
        test_map.put((long) 1,new Node(1,0,0));
        test_map.put((long) 2,new Node(2,4,10));
        test_map.put((long) 3,new Node(3,56,20));

        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2,4,10);
        citymap.createNode(3,56,20);
        assertEquals(test_map , citymap.getMapNode());
    }

    @Test
    public void testToString() throws IdError {
        CityMap citymap = new CityMap();
        citymap.createNode(1, 0, 0);
        citymap.createNode(2, 1, 1);
        citymap.createNode(3, 2, 2);
        citymap.createSection(200, "Rue Antoine Tout Court", 2, 1);
        citymap.createSection(100, "Rue Antoine Tout Court", 3, 2);
        citymap.createSection(50, "Rue du Test", 3, 1);

        HashMap<String, List<Section>> test_map = new HashMap<>();
        List<Section> newSections = new ArrayList<>();
        newSections.add(new Section(200,"Rue Antoine Tout Court",2,1) );
        newSections.add(new Section(100,"Rue Antoine Tout Court",3,2) );
        test_map.put("Rue Antoine Tout Court",newSections);

        newSections = new ArrayList<>();
        newSections.add(new Section(50,"Rue du Test",3,1) );
        test_map.put("Rue du Test",newSections);

        assertEquals(test_map,citymap.getMapSection());
    }
}
