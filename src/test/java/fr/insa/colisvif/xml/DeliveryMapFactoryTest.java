package fr.insa.colisvif.xml;

import static org.junit.Assert.assertEquals;

import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.model.CityMapFactory;
import fr.insa.colisvif.model.DeliveryMapFactory;
import fr.insa.colisvif.util.Quadruplet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DeliveryMapFactoryTest {

  @Test
  public void testCreateDeliveryMapFromXML() {
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
  public void testReadWarehouse() {
  }
}
