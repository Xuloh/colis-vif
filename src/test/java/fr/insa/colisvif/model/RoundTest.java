package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.XMLException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.Assert.assertEquals;


public class RoundTest {

    @Test
    public void addStepInIthPlace() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);

        round.addStepInIthPlace(step1, 1, cityMap);

        long node0 = round.getSteps().get(0).getArrivalNodeId();
        long node1 = round.getSteps().get(1).getArrivalNodeId();
        long node2 = round.getSteps().get(2).getArrivalNodeId();

        double length = cityMap.getLength(node0, node1) + cityMap.getLength(node1, node2);
        int time = (int) (length / ModelConstants.CYCLIST_SPEED);
        time += round.getSteps().get(0).getDuration() + round.getSteps().get(1).getDuration() + round.getStartDate();

        assertEquals(step1, round.getSteps().get(1));
        assertEquals(time, round.getSteps().get(2).getArrivalDate());
    }

    @Test (expected = IllegalArgumentException.class)
    public void addStepInIthPlacePickUpAfter() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);
        Step step2 = new Step(new Vertex(2, Vertex.DROP_OFF, 5), 3, 280);

        round.addStepInIthPlace(step2, 0, cityMap);
        round.addStepInIthPlace(step1, 1, cityMap);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addStepInIthPlaceDropOffBefore() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);
        Step step2 = new Step(new Vertex(2, Vertex.DROP_OFF, 5), 3, 280);

        round.addStepInIthPlace(step1, 1, cityMap);
        round.addStepInIthPlace(step2, 0, cityMap);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addStepInIthPlaceWrongIndex1() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);

        round.addStepInIthPlace(step1, 10, cityMap);
    }

    @Test (expected = IllegalArgumentException.class)
    public void addStepInIthPlaceWrongIndex2() throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);

        round.addStepInIthPlace(step1, -1, cityMap);
    }

    @Test
    public void removeDelivery() {
        // Passe un step, remove lui et son autre step
    }

    @Test
    public void addDelivery() {
        // Ajoute que à la fin
    }

    @Test
    public void changeOrderStep() {
    }

    @Test
    public void changeLocationStep() {
    }

    @Test
    public void addStep() {
    }
}
