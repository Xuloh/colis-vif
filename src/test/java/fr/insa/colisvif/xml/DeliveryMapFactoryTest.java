package fr.insa.colisvif.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.CityMapFactory;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.DeliveryMapFactory;
import fr.insa.colisvif.util.Quadruplet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DeliveryMapFactoryTest {

  @Test
  public void testCreateDeliveryMapFromXML()
      throws ParserConfigurationException, SAXException, IOException, IdError, URISyntaxException {
    File city_file = new File(getClass().getResource("/validPlan_test.xml").toURI());
    File delivery_file = new File(getClass().getResource("/validDeliverymatchingMap.xml").toURI());
    CityMapFactory cityMapFactory = new CityMapFactory();
    CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(city_file);
    DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
    DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(delivery_file,cityMap);

    DeliveryMap expected_result = new DeliveryMap();
    expected_result.createDelivery(2684668925L,2509481775L,420,600);
    expected_result.setWarehouseNodeId(2684668925L);
    expected_result.setStartDateInSeconds(28800);

    assertEquals(deliveryMap,expected_result);

  }

  @Test
  public void testCreateDeliveryMapFromXML_impossibleDeliveries()
      throws ParserConfigurationException, SAXException, IOException, IdError, URISyntaxException {
    File city_file = new File(getClass().getResource("/validPlan_test.xml").toURI());
    File delivery_file = new File(getClass().getResource("/validDelivery.xml").toURI());
    CityMapFactory cityMapFactory = new CityMapFactory();
    CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(city_file);
    DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
    DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(delivery_file,cityMap);
    assertNull(deliveryMap);
  }

  @Test
  public void testLoadFileGood()
      throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
    File file = new File(getClass().getResource("/validDelivery.xml").toURI());
    DeliveryMapFactory factory = new DeliveryMapFactory();
    factory.loadFile(file);
  }

  /*@Test(expected = NullPointerException.class)
  public void testLoadFileNonExistent()
      throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
    File file = new File(getClass().getResource("/notReadable.txt").toURI());
    DeliveryMapFactory factory = new DeliveryMapFactory();
    factory.loadFile(file);
  }*/

  @Test
  public void testReadDeliveryGoodFile()
      throws URISyntaxException, ParserConfigurationException, SAXException, IOException, XMLException {
      File file = new File(getClass().getResource("/validDelivery.xml").toURI());
      DeliveryMapFactory factory = new DeliveryMapFactory();
      Element root = factory.loadFile(file);
      factory.readDelivery(root);

  }

  @Test (expected = XMLException.class)
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
    List<Quadruplet<Long, Long, Integer, Integer>>  result = factory.readDelivery(root);


    List<Quadruplet<Long, Long, Integer, Integer>> expected_result = new ArrayList<> ();
    expected_result.add(new Quadruplet((long)1679901320,(long)208769457,420,600));
    expected_result.add(new Quadruplet((long)208769120,(long)25336179,420,480));

    assertEquals(expected_result,result);

  }

  @Test
  public void testReadWarehouse()
      throws XMLException, ParserConfigurationException, SAXException, IOException, URISyntaxException {

    File file = new File(getClass().getResource("/validDelivery.xml").toURI());
    DeliveryMapFactory factory = new DeliveryMapFactory();
    Element root = factory.loadFile(file);
    Pair<Long, Integer> warehouse = factory.readWarehouse(root);
    Pair expected_result = new Pair((long)2835339774L,28800) ;
    assertEquals(warehouse,expected_result);

    assertEquals(warehouse,expected_result);
  }

  @Test (expected = XMLException.class)
  public void testReadWarehouseWrongFile()
      throws URISyntaxException, ParserConfigurationException, SAXException, IOException, XMLException {
    File file = new File(getClass().getResource("/InvalidDelivery.xml").toURI());
    DeliveryMapFactory factory = new DeliveryMapFactory();
    Element root = factory.loadFile(file);
    factory.readWarehouse(root);

  }
}
