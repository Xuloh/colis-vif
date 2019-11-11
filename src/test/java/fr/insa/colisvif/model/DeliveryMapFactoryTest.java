package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdException;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.util.Quadruplet;
import javafx.util.Pair;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DeliveryMapFactoryTest {

    @Test
    public void testCreateDeliveryMapFromXML()
            throws ParserConfigurationException, XMLException, SAXException, IOException, IdException, URISyntaxException {
        File cityFile = new File(getClass().getResource("/validPlan_test.xml").toURI());
        File deliveryFile = new File(getClass().getResource("/validDeliveryMatchingMap.xml").toURI());
        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(cityFile);
        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(deliveryFile, cityMap);

        DeliveryMap expectedResult = new DeliveryMap();
        expectedResult.createDelivery(0, 2684668925L, 2509481775L, 420, 600);
        expectedResult.createWarehouse(2684668925L, 28800);

        assertEquals(deliveryMap, expectedResult);

    }

    @Test(expected = IdException.class)
    public void testCreateDeliveryMapFromXMLImpossibleDeliveries()
            throws ParserConfigurationException, XMLException, SAXException, IOException, IdException, URISyntaxException {
        File cityFile = new File(getClass().getResource("/validPlan_test.xml").toURI());
        File deliveryFile = new File(getClass().getResource("/validDelivery.xml").toURI());
        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(cityFile);
        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(deliveryFile, cityMap);
        assertNull(deliveryMap);
    }

    @Test
    public void testLoadFileGood() throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        File file = new File(getClass().getResource("/validDelivery.xml").toURI());
        DeliveryMapFactory factory = new DeliveryMapFactory();
        factory.loadFile(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadFileNonExistent()
            throws ParserConfigurationException, SAXException, IOException {
        File file = new File("/notAtxtFile.txt");
        DeliveryMapFactory factory = new DeliveryMapFactory();
        factory.loadFile(file);
    }

    @Test
    public void testReadDeliveryGoodFile()
            throws URISyntaxException, ParserConfigurationException, SAXException, IOException, XMLException {
        File file = new File(getClass().getResource("/validDelivery.xml").toURI());
        DeliveryMapFactory factory = new DeliveryMapFactory();
        Element root = factory.loadFile(file);
        factory.readDelivery(root);

    }

    @Test(expected = XMLException.class)
    public void testReadDeliveryWrongFile()
            throws URISyntaxException, ParserConfigurationException, SAXException, IOException, XMLException {
        File file = new File(getClass().getResource("/InvalidDelivery.xml").toURI());
        DeliveryMapFactory factory = new DeliveryMapFactory();
        Element root = factory.loadFile(file);
        factory.readDelivery(root);

    }

    @Test
    public void testReadDeliveryResult()
            throws URISyntaxException, ParserConfigurationException, SAXException, IOException, XMLException {
        File file = new File(getClass().getResource("/validDelivery.xml").toURI());
        DeliveryMapFactory factory = new DeliveryMapFactory();
        Element root = factory.loadFile(file);
        List<Quadruplet<Long, Long, Integer, Integer>> result = factory.readDelivery(root);


        List<Quadruplet<Long, Long, Integer, Integer>> expectedResult = new ArrayList<>();
        expectedResult.add(new Quadruplet<>(1679901320L, 208769457L, 420, 600));
        expectedResult.add(new Quadruplet<>(208769120L, 25336179L, 420, 480));

        assertEquals(expectedResult, result);

    }

    @Test
    public void testReadWarehouse()
            throws XMLException, ParserConfigurationException, SAXException, IOException, URISyntaxException {

        File file = new File(getClass().getResource("/validDelivery.xml").toURI());
        DeliveryMapFactory factory = new DeliveryMapFactory();
        Element root = factory.loadFile(file);
        Pair<Long, Integer> warehouse = factory.readWarehouse(root);
        Pair expectedResult = new Pair<>(2835339774L, 28800);
        assertEquals(warehouse, expectedResult);

        assertEquals(warehouse, expectedResult);
    }

    @Test(expected = XMLException.class)
    public void testReadWarehouseWrongFile()
            throws URISyntaxException, ParserConfigurationException, SAXException, IOException, XMLException {
        File file = new File(getClass().getResource("/InvalidDelivery.xml").toURI());
        DeliveryMapFactory factory = new DeliveryMapFactory();
        Element root = factory.loadFile(file);
        factory.readWarehouse(root);

    }
}
