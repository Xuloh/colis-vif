package fr.insa.colisvif.model;

import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.xml.DeliveryMapParserXML;
import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DeliveryMapFactory {
    private DeliveryMapParserXML deliveryRequestParserXML;

    public DeliveryMapFactory(DeliveryMapParserXML deliveryRequestParserXML) {
        this.deliveryRequestParserXML = deliveryRequestParserXML;
    }

    public DeliveryMap createDeliveryMapFromXML(File file) throws IOException, SAXException, ParserConfigurationException {
        this.deliveryRequestParserXML.loadFile(file);
        DeliveryMap deliveryMap = new DeliveryMap();
        List<Quadruplet<Long, Long, Integer, Integer>> deliveryList = this.deliveryRequestParserXML.readDelivery();
        Pair<Long,Integer> warehouse = this.deliveryRequestParserXML.readWarehouse();
        for (Quadruplet<Long, Long, Integer, Integer> delivery: deliveryList){
            deliveryMap.createDelivery(delivery.getFirst(), delivery.getSecond(), delivery.getThird(), delivery.getFourth());
        }
        deliveryMap.createWarehouse(warehouse.getKey(), warehouse.getValue());
        return deliveryMap;
    }

    /*
        public CityMap createCityMapFromXMLFile(File file) throws IOException, SAXException, ParserConfigurationException, IdError {
        this.cityMapParserXML.loadFile(file);
        CityMap cityMap = new CityMap();
        List<Triplet<Long, Double, Double>> nodes = this.cityMapParserXML.readNodes();
        List<Quadruplet<Double, String, Long, Long>> sections = this.cityMapParserXML.readSections();
        for (Triplet<Long, Double, Double> node : nodes) {
            cityMap.createNode(node.getFirst(), node.getSecond(), node.getThird());
        }
        for(Quadruplet<Double, String, Long, Long> section : sections) {
            cityMap.createSection(section.getFirst(), section.getSecond(), section.getThird(), section.getFourth());
        }
        return cityMap;
    }
     */
}
