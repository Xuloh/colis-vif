package fr.insa.colisvif.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class CityMapTest {

    @Test
    public void testUpdateLatMax() {
        CityMap cityMap = new CityMap();
        final double LNG_MIN = cityMap.getLngMin();
        cityMap.createNode(2405632, -60, 180);
        assertTrue(-60 == cityMap.getLatMax() && cityMap.getLngMin() == LNG_MIN);
    }

    @Test
    public void testUpdateLongMin() {
        CityMap cityMap = new CityMap();
        final double LAT_MAX = cityMap.getLatMax();
        cityMap.createNode(2405632, -90, 120);
        assertTrue(120 == cityMap.getLngMin() && cityMap.getLatMax() == LAT_MAX);
    }

    @Test
    public void testUpdateLatMaxLongMin() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(2405632, -60, 120);
        assertTrue(-60 == cityMap.getLatMax() && cityMap.getLngMin() == 120);
    }

    @Test
    public void testCreateSectionNewSection() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(101, -60, 120);
        cityMap.createNode(100, -66, 120);
        cityMap.createSection(10, "Rue de St-Germain", 100, 101);
        assertNotNull(cityMap.getMapSection().get("Rue de St-Germain"));
        assertEquals("[Length : 10.0 | Road Name : Rue de St-Germain | Destination : 101 | Origin : 100\n]", cityMap.getMapSection().get("Rue de St-Germain").toString());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCreateSectionIllegalOrigin() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, -60, 120);
        cityMap.createSection(10, "Rue de St-Germain", 2, 101);
    }

    @Test
    public void testCreateSectionUpdateSection() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(101, -60, 120);
        cityMap.createNode(100, -66, 120);
        cityMap.createNode(99, -65, 120);
        cityMap.createSection(10, "Rue de St-Germain", 100, 101);
        cityMap.createSection(10, "Rue de St-Germain", 99, 100);

        assertNotNull(cityMap.getMapSection().get("Rue de St-Germain"));
        assertEquals("[Length : 10.0 | Road Name : Rue de St-Germain | Destination : 101 | Origin : 100\n"
                     + ", Length : 10.0 | Road Name : Rue de St-Germain | Destination : 100 | Origin : 99\n]",
                     cityMap.getMapSection().get("Rue de St-Germain").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBadNode() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(2405632, -140, 120);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateBadSection() {
        CityMap cityMap = new CityMap();
        cityMap.createSection(-240, "Supé Rue", 124, 120);
    }

    @Test
    public void testCityMapToString() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        cityMap.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap.createSection(100, "Rue Antoine Tout Court", 2, 3);

        String expected = "Nodes : \n"
                          + "ID : 1 | Latitude : 0.0 | Longitude : 0.0\n"
                          + "ID : 2 | Latitude : 1.0 | Longitude : 1.0\n"
                          + "ID : 3 | Latitude : 2.0 | Longitude : 2.0\n\n"
                          + "Sections : \n"
                          + "Length : 200.0 | Road Name : Rue Antoine Tout Court | Destination : 2 | Origin : 1\n"
                          + "Length : 100.0 | Road Name : Rue Antoine Tout Court | Destination : 3 | Origin : 2\n";
        assertEquals("To String from CityMap is OK", expected, cityMap.toString());


    }

    @Test
    public void testEquals1() {
        CityMap cityMap1 = new CityMap();
        cityMap1.createNode(1, 0, 0);
        cityMap1.createNode(2, 1, 1);
        cityMap1.createNode(3, 2, 2);
        cityMap1.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap1.createSection(100, "Rue Antoine Tout Court", 2, 3);

        CityMap cityMap2 = new CityMap();
        cityMap2.createNode(1, 0, 0);
        cityMap2.createNode(2, 1, 1);
        cityMap2.createNode(3, 2, 2);
        cityMap2.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap2.createSection(100, "Rue Antoine Tout Court", 2, 3);

        assertEquals(cityMap1, cityMap2);
    }

    @Test
    public void testEquals2() {
        CityMap cityMap1 = new CityMap();
        cityMap1.createNode(1, 0, 0);
        cityMap1.createNode(2, 1, 1);
        cityMap1.createNode(3, 2, 2);
        cityMap1.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap1.createSection(100, "Rue Antoine Tout Court", 2, 3);

        assertEquals(cityMap1, cityMap1);
    }

    @Test
    public void testEquals3() {
        Integer test = 1;
        CityMap cityMap1 = new CityMap();
        cityMap1.createNode(1, 0, 0);
        cityMap1.createNode(2, 1, 1);
        cityMap1.createNode(3, 2, 2);
        cityMap1.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap1.createSection(100, "Rue Antoine Tout Court", 2, 3);

        assertNotEquals(cityMap1, test);
    }

    @Test
    public void getLngMin() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        assertEquals(0, cityMap.getLngMin(), 0.0);
    }

    @Test
    public void getLngMax() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        assertEquals(2, cityMap.getLngMax(), 0.0);
    }

    @Test
    public void getLatMin() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        assertEquals(0, cityMap.getLatMin(), 0.0);
    }

    @Test
    public void getLatMax() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        assertEquals(2, cityMap.getLatMax(), 0.0);
    }

    @Test
    public void getMapNode() {
        HashMap<Long, Node> testMap = new HashMap<>();
        testMap.put(1L, new Node(1, 0, 0));
        testMap.put(2L, new Node(2, 4, 10));
        testMap.put(3L, new Node(3, 56, 20));

        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 4, 10);
        cityMap.createNode(3, 56, 20);
        assertEquals(testMap, cityMap.getMapNode());
    }

    @Test
    public void getMapSection() {
        HashMap<Long, Node> testMap = new HashMap<>();
        testMap.put(1L, new Node(1, 0, 0));
        testMap.put(2L, new Node(2, 4, 10));
        testMap.put(3L, new Node(3, 56, 20));

        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 4, 10);
        cityMap.createNode(3, 56, 20);
        assertEquals(testMap, cityMap.getMapNode());
    }

    @Test
    public void testMapSection() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        cityMap.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap.createSection(100, "Rue Antoine Tout Court", 2, 3);
        cityMap.createSection(50, "Rue du Test", 1, 3);

        HashMap<String, List<Section>> testMap = new HashMap<>();
        List<Section> newSections = new ArrayList<>();
        newSections.add(new Section(200, "Rue Antoine Tout Court", 1, 2));
        newSections.add(new Section(100, "Rue Antoine Tout Court", 2, 3));
        testMap.put("Rue Antoine Tout Court", newSections);

        newSections = new ArrayList<>();
        newSections.add(new Section(50, "Rue du Test", 1, 3));
        testMap.put("Rue du Test", newSections);

        assertEquals(testMap, cityMap.getMapSection());
    }

    @Test
    public void testMapSection2() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        cityMap.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap.createSection(100, "Rue Antoine Tout Court", 2, 3);
        cityMap.createSection(50, "Rue du Test", 1, 3);

        HashMap<String, List<Section>> testMap = new HashMap<>();
        List<Section> newSections = new ArrayList<>();
        newSections.add(new Section(200, "Rue Antoine Tout Court", 1, 2));
        newSections.add(new Section(100, "Rue Antoine Tout Court", 2, 3));
        testMap.put("Rue Antoine Tout Court", newSections);

        newSections = new ArrayList<>();
        newSections.add(new Section(50, "Rue du Test", 1, 2));
        testMap.put("Rue du Test", newSections);

        assertNotEquals(testMap, cityMap.getMapSection());
    }

    @Test
    public void testMapSection3() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 1, 1);
        cityMap.createNode(3, 2, 2);
        cityMap.createSection(200, "Rue Antoine Tout Court", 1, 2);
        cityMap.createSection(100, "Rue Antoine Tout Court", 2, 3);
        cityMap.createSection(50, "Rue du Test", 1, 3);

        HashMap<String, List<Section>> testMap = new HashMap<>();
        List<Section> newSections = new ArrayList<>();
        newSections.add(new Section(200, "Rue Antoine Tout Court", 1, 2));
        newSections.add(new Section(100, "Rue Antoine Tout Court", 2, 4));
        testMap.put("Rue Antoine Tout Court", newSections);

        newSections = new ArrayList<>();
        newSections.add(new Section(50, "Rue du Test", 1, 3));
        testMap.put("Rue du Test", newSections);

        assertNotEquals(testMap, cityMap.getMapSection());
    }

    @Test
    public void getSection1() {
        Section expected =  new Section(2, "Rue du test", 1, 2);
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 10, 10);
        cityMap.createNode(2, 10, 10);

        cityMap.createSection(expected.getLength(), expected.getRoadName(), expected.getOrigin(), expected.getDestination());

        assertEquals(cityMap.getSection(expected.getOrigin(), expected.getDestination()), expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSection2() {
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 10, 10);
        cityMap.createNode(2, 10, 10);
        cityMap.createSection(2, "erzr", 1, 2);
        cityMap.getSection(1, 3);
    }
}
