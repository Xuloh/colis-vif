package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.XMLException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;


public class RoundTest {

    public static Step check(List<Step> steps, int deliveryID, boolean type) {
        Step res = null;
        for (Step step : steps) {
            if (step.getDeliveryID() == deliveryID && step.getType() == type) {
                res = step;
            }
        }

        return res;
    }

    @Test
    public void addStepInIthPlace()
        throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);

        round.addStepInIthPlace(step1, 1, cityMap);

        long node0 = round.getSteps().get(0).getArrivalNodeId();
        long node1 = round.getSteps().get(1).getArrivalNodeId();
        long node2 = round.getSteps().get(2).getArrivalNodeId();

        double length =
            cityMap.getLength(node0, node1) + cityMap.getLength(node1, node2);
        int time = (int) (length / ModelConstants.CYCLIST_SPEED);
        time += round.getSteps().get(0).getDuration() + round.getSteps().get(1)
            .getDuration() + round.getStartDate();

        assertEquals(step1, round.getSteps().get(1));
        assertEquals(time, round.getSteps().get(2).getArrivalDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addStepInIthPlaceWrongIndex1()
        throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);

        round.addStepInIthPlace(step1, 10, cityMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addStepInIthPlaceWrongIndex2()
        throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = new Step(new Vertex(2, Vertex.PICK_UP, 5), 3, 280);

        round.addStepInIthPlace(step1, -1, cityMap);
    }

    @Test
    public void removeDelivery() throws Exception {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step step1 = round.getSteps().get(0);
        Step step2 = check(round.getSteps(), step1.getDeliveryID(),
            !step1.getType());

        int initial = round.getSteps().size();

        round.removeDelivery(step1, cityMap);
        assertEquals(initial - 2, round.getSteps().size());
        assertFalse(round.getSteps().contains(step1) && round.getSteps()
            .contains(step2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeDeliveryWrong1() throws Exception {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        round.removeDelivery(new Step(new Vertex(1, Vertex.PICK_UP, 10), 1, 20),
            cityMap);
    }

    @Test
    public void addDelivery()
        throws URISyntaxException, XMLException, ParserConfigurationException, SAXException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        int before = round.getSteps().size();

        round.addDelivery(1, 2, 5, 5, cityMap);
        assertEquals(before + 2, round.getSteps().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDeliveryWrong1()
        throws URISyntaxException, XMLException, ParserConfigurationException, SAXException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        int before = round.getSteps().size();

        round.addDelivery(7, 2, 5, 5, cityMap);

    }

    @Test(expected = IllegalArgumentException.class)
    public void addDeliveryWrong2()
        throws URISyntaxException, XMLException, ParserConfigurationException, SAXException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        int before = round.getSteps().size();

        round.addDelivery(1, 8, 5, 5, cityMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDeliveryWrong3()
        throws URISyntaxException, XMLException, ParserConfigurationException, SAXException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        int before = round.getSteps().size();

        round.addDelivery(1, 1, -1, 5, cityMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDeliveryWrong4()
        throws URISyntaxException, XMLException, ParserConfigurationException, SAXException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory.createCityMapFromXMLFile(
            new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory.createDeliveryMapFromXML(
            new File(getClass().getResource(urlDelivery).toURI()), cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        int before = round.getSteps().size();

        round.addDelivery(1, 1, 1, -1, cityMap);
    }

    @Test
    public void changeOrderStepSafeCall()
        throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory
            .createCityMapFromXMLFile(
                new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory
            .createDeliveryMapFromXML(
                new File(getClass().getResource(urlDelivery).toURI()),
                cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step stepMoving = round.getSteps().get(1);
        Step stepBefore = round.getSteps().get(2);

        round.changeOrderStep(stepMoving, stepBefore, cityMap);
    }

    @Test
    public void changeOrderStepChangeWithWarehouse()
        throws URISyntaxException, SAXException, ParserConfigurationException, XMLException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory
            .createCityMapFromXMLFile(
                new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory
            .createDeliveryMapFromXML(
                new File(getClass().getResource(urlDelivery).toURI()),
                cityMap);

        Round round = cityMap.shortestRound(deliveryMap);

        Step stepMoving = round.getSteps().get(1);
        Step stepBefore = round.getSteps().get(0);

        round.changeOrderStep(stepMoving, stepBefore, cityMap);

        assertEquals(stepMoving, round.getSteps().get(1));
    }

    @Test
    public void changeLocationStepGood()
        throws URISyntaxException, XMLException, ParserConfigurationException, SAXException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory
            .createCityMapFromXMLFile(
                new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory
            .createDeliveryMapFromXML(
                new File(getClass().getResource(urlDelivery).toURI()),
                cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        Step stepMoving = round.getSteps().get(1);
        round.changeLocationStep(stepMoving, 3, cityMap);

        assertEquals(3, stepMoving.getArrival().getNodeId());
    }

    @Test(expected = NullPointerException.class)
    public void changeLocationStepNullStep()
        throws URISyntaxException, XMLException, ParserConfigurationException, SAXException, IOException {
        String urlCityMap = "/testModifTrajet/test1Algo_plan.xml";
        String urlDelivery = "/testModifTrajet/test1Algo_demande.xml";

        CityMapFactory cityMapFactory = new CityMapFactory();
        CityMap cityMap = cityMapFactory
            .createCityMapFromXMLFile(
                new File(getClass().getResource(urlCityMap).toURI()));

        DeliveryMapFactory deliveryMapFactory = new DeliveryMapFactory();
        DeliveryMap deliveryMap = deliveryMapFactory
            .createDeliveryMapFromXML(
                new File(getClass().getResource(urlDelivery).toURI()),
                cityMap);

        Round round = cityMap.shortestRound(deliveryMap);
        round.changeLocationStep(null, 3, cityMap);
    }

}
