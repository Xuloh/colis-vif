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

    public DeliveryMapFactory() {
        this.deliveryRequestParserXML =  new DeliveryMapParserXML();
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

}
