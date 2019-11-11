package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.XMLException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class CityMapTest {

    public static <T> Object getAttribute(Class<T> clazz, T targetObject, String attributeName) {
        try {
            Field field = clazz.getDeclaredField(attributeName);
            field.setAccessible(true);
            return field.get(targetObject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Object invokeMethod(Class<T> clazz, T targetObject, String methodName, Class<?>[] parametersTypes, Object[] parametersValues) throws InvocationTargetException {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parametersTypes);
            method.setAccessible(true);
            return method.invoke(targetObject, parametersValues);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void callDijkstra(CityMap cityMap, int start) throws InvocationTargetException {
        Class[] paramType = new Class[1];
        paramType[0] = long.class;
        Integer[] param = new Integer[1];
        param[0] = 1;
        invokeMethod(CityMap.class, cityMap, "dijkstra", paramType, param);
    }

    public int timeTravel(int distance){
        return (distance / (int) (15. / 3.6));
    }

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
        String expected = "[Section{"
                + "length=" + 10.0
                + ", roadName='" + "Rue de St-Germain" + '\''
                + ", origin=" + 100
                + ", destination=" + 101
                + "}]";
        assertEquals(expected, cityMap.getMapSection().get("Rue de St-Germain").toString());
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

        String expected = "[Section{"
                + "length=" + 10.0
                + ", roadName='" + "Rue de St-Germain" + '\''
                + ", origin=" + 100
                + ", destination=" + 101
                + "}, Section{"
                + "length=" + 10.0
                + ", roadName='" + "Rue de St-Germain" + '\''
                + ", origin=" + 99
                + ", destination=" + 100
                + "}]";

        assertNotNull(cityMap.getMapSection().get("Rue de St-Germain"));
        assertEquals(expected,
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
                            + "Section{"
                            + "length=" + 200.0
                            + ", roadName='" + "Rue Antoine Tout Court" + '\''
                            + ", origin=" + 1
                            + ", destination=" + 2
                            + "}Section{"
                            + "length=" + 100.0
                            + ", roadName='" + "Rue Antoine Tout Court" + '\''
                            + ", origin=" + 2
                            + ", destination=" + 3
                            + "}";
        assertEquals(expected, cityMap.toString());
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

    @Test
    public void dijkstraOneRoad() {
        int lengthSection1 = 5;
        int lengthSection2 = 10;
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 0, 0);
        cityMap.createNode(3, 0, 0);
        cityMap.createSection(lengthSection1, "Rue 1", 1, 2);
        cityMap.createSection(lengthSection2, "Rue 1", 2, 3);

        Class[] paramType = new Class[1];
        paramType[0] = long.class;
        Integer[] param = new Integer[1];
        param[0] = 1;

        try {
            callDijkstra(cityMap, 1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        assertEquals(cityMap.getLength(1, 3), lengthSection1 + lengthSection2, 0.1);
        assertEquals(cityMap.getLength(1, 2), lengthSection1, 0.1);
    }

    @Test (expected = InvocationTargetException.class)
    public void dijkstraOneRoadWrong1() throws InvocationTargetException {
        CityMap cityMap = new CityMap();
        callDijkstra(cityMap, 1);
    }



    @Test
    public void dijkstraTriangle() {
        int lengthSection1 = 5;
        int lengthSection2 = 10;
        int lengthSection3 = 12;
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 0, 0);
        cityMap.createNode(3, 0, 0);
        cityMap.createSection(lengthSection1, "Rue 1", 1, 2);
        cityMap.createSection(lengthSection1, "Rue 1", 2, 1);
        cityMap.createSection(lengthSection2, "Rue 1", 2, 3);
        cityMap.createSection(lengthSection2, "Rue 1", 3, 2);
        cityMap.createSection(lengthSection3, "Rue 1", 3, 1);
        cityMap.createSection(lengthSection3, "Rue 1", 1, 3);

        try {
            callDijkstra(cityMap, 1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        assertEquals(cityMap.getLength(1, 3), lengthSection3,0.1);
        assertEquals(cityMap.getLength(1, 2), lengthSection1, 0.1);
    }

    @Test
    public void dijkstra2Roads() {
        int lengthSection1 = 5;
        int lengthSection2 = 5;
        int lengthSection3 = 5;
        int lengthSection4 = 1;
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 0, 0);
        cityMap.createNode(3, 0, 0);
        cityMap.createNode(4, 0, 0);
        cityMap.createSection(lengthSection1, "Rue 1", 1, 2);
        cityMap.createSection(lengthSection2, "Rue 1", 2, 3);
        cityMap.createSection(lengthSection3, "Rue 1", 3, 4);
        cityMap.createSection(lengthSection4, "Rue 1", 1, 3);

        try {
            callDijkstra(cityMap, 1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        assertEquals(cityMap.getLength(1, 1), 0, 0.1);
        assertEquals(cityMap.getLength(1, 2), lengthSection1, 0.1);
        assertEquals(cityMap.getLength(1, 3), lengthSection4, 0.1);
        assertEquals(cityMap.getLength(1, 4), lengthSection4 + lengthSection3,0.1);
    }

    @Test
    public void dijkstraDeadEnd() {
        int lengthSection1 = 1;
        int lengthSection2 = 2;
        int lengthSection3 = 5;
        int lengthSection4 = 7;
        CityMap cityMap = new CityMap();
        cityMap.createNode(1, 0, 0);
        cityMap.createNode(2, 0, 0);
        cityMap.createNode(3, 0, 0);
        cityMap.createNode(4, 0, 0);
        cityMap.createNode(5, 0, 0);
        cityMap.createSection(lengthSection1, "Rue 1", 1, 2);
        cityMap.createSection(lengthSection2, "Rue 1", 2, 3);
        cityMap.createSection(lengthSection3, "Rue 1", 2, 4);
        cityMap.createSection(lengthSection4, "Rue 1", 4, 5);

        try {
            callDijkstra(cityMap, 1);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        assertEquals(cityMap.getLength(1, 1), 0, 0.1);
        assertEquals(cityMap.getLength(1, 2), lengthSection1, 0.1);
        assertEquals(cityMap.getLength(1, 3), lengthSection1 + lengthSection2, 0.1);
        assertEquals(cityMap.getLength(1, 4), lengthSection1 + lengthSection3,0.1);
        assertEquals(cityMap.getLength(1, 5), lengthSection1 + lengthSection3 + lengthSection4,0.1);
    }

    @Test
    public void shortestRoundSmallTest1() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test1Algo_plan.xml";
        String urlDelivery = "/testAlgo/test1Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 0.1", 0, 1));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 2), 0);
        step2.addSection(new Section(250, "Rue 1.2", 1, 2));
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(250));

        expected.add(step1);
        expected.add(step2);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);

        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest2() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test2Algo_plan.xml";
        String urlDelivery = "/testAlgo/test2Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 0.1", 0, 1));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 2), 0);
        step2.addSection(new Section(250, "Rue 3.2", 3, 2));
        step2.addSection(new Section(250, "Rue 1.3", 1, 3));

        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(500));

        expected.add(step1);
        expected.add(step2);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest3() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test3Algo_plan.xml";
        String urlDelivery = "/testAlgo/test3Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 0.1", 0, 1));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 2), 0);
        step2.addSection(new Section(500, "Rue 1.2", 1, 2));

        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(500));

        expected.add(step1);
        expected.add(step2);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest4() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test4Algo_plan.xml";
        String urlDelivery = "/testAlgo/test4Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 0.1", 0, 1));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 0);
        step2.addSection(new Section(250, "Rue 1.3", 1, 3));
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(250));

        Step step3 = new Step(new Vertex(1, Vertex.PICK_UP, 1), 1);
        step3.addSection(new Section(250, "Rue 3.2", 3, 2));
        step3.setArrivalDate(step2.getArrivalDate() + step2.getDuration() + timeTravel(250));

        Step step4 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 1);
        step4.addSection(new Section(250, "Rue 3.4", 3, 4));
        step4.addSection(new Section(250, "Rue 2.3", 2, 3));
        step4.setArrivalDate(step3.getArrivalDate() + step3.getDuration() + timeTravel(500));

        expected.add(step1);
        expected.add(step2);
        expected.add(step3);
        expected.add(step4);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest5() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test5Algo_plan.xml";
        String urlDelivery = "/testAlgo/test5Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 0.1", 0, 1));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 0);
        step2.addSection(new Section(1250, "Rue 6.4", 6, 4));
        step2.addSection(new Section(1250, "Rue 1.6", 1, 6));
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(2500));

        Step step3 = new Step(new Vertex(1, Vertex.PICK_UP, 250), 1);
        step3.addSection(new Section(250, "Rue 4.3", 4, 3));
        step3.setArrivalDate(step2.getArrivalDate() + step2.getDuration() + timeTravel(250));

        Step step4 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 1);
        step4.addSection(new Section(250, "Rue 4.5", 4, 5));
        step4.addSection(new Section(250, "Rue 3.4", 3, 4));
        step4.setArrivalDate(step3.getArrivalDate() + step3.getDuration() + timeTravel(500));

        expected.add(step1);
        expected.add(step2);
        expected.add(step3);
        expected.add(step4);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest6() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test6Algo_plan.xml";
        String urlDelivery = "/testAlgo/test6Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 0.1", 0, 1));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(6, Vertex.PICK_UP, 1), 1);
        step2.addSection(new Section(500, "Rue 7.6", 7, 6));
        step2.addSection(new Section(250, "Rue 1.7", 1, 7));
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(750));

        Step step3 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 1);
        step3.addSection(new Section(250, "Rue 5.4", 5, 4));
        step3.addSection(new Section(250, "Rue 6.5", 6, 5));
        step3.setArrivalDate(step2.getArrivalDate() + step2.getDuration() + timeTravel(500));

        Step step4 = new Step(new Vertex(1, Vertex.DROP_OFF, 2), 0);
        step4.addSection(new Section(250, "Rue 4.3", 4, 3));
        step4.setArrivalDate(step3.getArrivalDate() + step3.getDuration() + timeTravel(250));

        expected.add(step1);
        expected.add(step2);
        expected.add(step3);
        expected.add(step4);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest7() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test7Algo_plan.xml";
        String urlDelivery = "/testAlgo/test7Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.setArrivalDate(8*3600);

        Step step2 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 2);
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration());

        Step step3 = new Step(new Vertex(1, Vertex.PICK_UP, 1), 3);
        step3.setArrivalDate(step2.getArrivalDate() + step2.getDuration());

        Step step4 = new Step(new Vertex(2, Vertex.DROP_OFF, 1), 2);
        step4.addSection(new Section(250, "Rue 0.2", 0, 2));
        step4.setArrivalDate(step3.getArrivalDate() + step3.getDuration() + timeTravel(250));

        Step step5 = new Step(new Vertex(3, Vertex.PICK_UP, 1), 1);
        step5.addSection(new Section(250, "Rue 2.3", 2, 3));
        step5.setArrivalDate(step4.getArrivalDate() + step4.getDuration() + timeTravel(250));

        Step step6 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 0);
        step6.addSection(new Section(250, "Rue 3.1", 3, 1));
        step6.setArrivalDate(step5.getArrivalDate() + step5.getDuration() + timeTravel(250));

        Step step7 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 1);
        step7.setArrivalDate(step6.getArrivalDate() + step6.getDuration());

        Step step8 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 3);
        step8.setArrivalDate(step7.getArrivalDate() + step7.getDuration());

        expected.add(step1);
        expected.add(step2);
        expected.add(step3);
        expected.add(step4);
        expected.add(step5);
        expected.add(step6);
        expected.add(step7);
        expected.add(step8);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest8() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test8Algo_plan.xml";
        String urlDelivery = "/testAlgo/test8Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.setArrivalDate(8*3600);

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 0);
        step2.addSection(new Section(250, "Rue 0.1", 0, 1));
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(250));

        Step step3 = new Step(new Vertex(1, Vertex.PICK_UP, 1), 1);
        step3.setArrivalDate(step2.getArrivalDate() + step2.getDuration());

        Step step4 = new Step(new Vertex(0, Vertex.DROP_OFF, 1), 1);
        step4.addSection(new Section(250, "Rue 1.0", 1, 0));
        step4.setArrivalDate(step3.getArrivalDate() + step3.getDuration() + timeTravel(250));

        Step step5 = new Step(new Vertex(1, Vertex.PICK_UP, 1), 2);
        step5.addSection(new Section(250, "Rue 0.1", 0, 1));
        step5.setArrivalDate(step4.getArrivalDate() + step4.getDuration() + timeTravel(250));

        Step step6 = new Step(new Vertex(1, Vertex.PICK_UP, 1), 3);
        step6.setArrivalDate(step5.getArrivalDate() + step5.getDuration());

        Step step7 = new Step(new Vertex(2, Vertex.DROP_OFF, 1), 2);
        step7.addSection(new Section(500, "Rue 1.2", 1, 2));
        step7.setArrivalDate(step6.getArrivalDate() + step6.getDuration() + timeTravel(500));

        Step step8 = new Step(new Vertex(2, Vertex.DROP_OFF, 1), 3);
        step8.setArrivalDate(step7.getArrivalDate() + step7.getDuration());

        expected.add(step1);
        expected.add(step2);
        expected.add(step3);
        expected.add(step4);
        expected.add(step5);
        expected.add(step6);
        expected.add(step7);
        expected.add(step8);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 0);
    }

    @Test
    public void shortestRoundSmallTest9() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test9Algo_plan.xml";
        String urlDelivery = "/testAlgo/test9Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(0, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 1.0", 1, 0));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 0);
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(0));

        expected.add(step1);
        expected.add(step2);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);
        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 1);
    }

    @Test
    public void shortestRoundSmallTest10() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testAlgo/test10Algo_plan.xml";
        String urlDelivery = "/testAlgo/test10Algo_demande.xml";
        CityMapFactory cf = new CityMapFactory();
        DeliveryMapFactory df = new DeliveryMapFactory();
        Round round;

        List<Step> expected = new ArrayList<>();

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 1), 0);
        step1.addSection(new Section(250, "Rue 1.2", 1, 2));
        step1.setArrivalDate(8*3600 + timeTravel(250));

        Step step2 = new Step(new Vertex(1, Vertex.DROP_OFF, 1), 0);
        step2.addSection(new Section(250, "Rue 2.0", 2, 0));
        step2.setArrivalDate(step1.getArrivalDate() + step1.getDuration() + timeTravel(250));

        expected.add(step1);
        expected.add(step2);

        CityMap cityMap = cf.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));
        DeliveryMap deliveryMap = df.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        round = cityMap.shortestRound(deliveryMap);

        assertEquals(expected, round.getSteps());
        assertEquals(round.getWarehouseNodeId(), 1);
    }
}
