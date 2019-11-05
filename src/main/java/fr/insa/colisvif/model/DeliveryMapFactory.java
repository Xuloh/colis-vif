package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.util.Quadruplet;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryMapFactory {
    private File xmlFile;

    public DeliveryMapFactory() {
    }

    public DeliveryMap createDeliveryMapFromXML(File file) throws IOException, SAXException, ParserConfigurationException {
        loadFile(file);
        DeliveryMap deliveryMap = new DeliveryMap();
        List<Quadruplet<Long, Long, Integer, Integer>> deliveryList = readDelivery();
        Pair<Long,Integer> warehouse = readWarehouse();
        for (Quadruplet<Long, Long, Integer, Integer> delivery: deliveryList){
            deliveryMap.createDelivery(delivery.getFirst(), delivery.getSecond(), delivery.getThird(), delivery.getFourth());
        }
        deliveryMap.createWarehouse(warehouse.getKey(), warehouse.getValue());
        return deliveryMap;
    }

    public void loadFile(File file) {
        this.xmlFile = file;
    }


    public List<Quadruplet<Long, Long, Integer, Integer>> readDelivery() {
        List<Quadruplet<Long, Long, Integer, Integer>> result = new ArrayList<>();
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(this.xmlFile);
            Element root = document.getDocumentElement();
            if (root.getNodeName().equals("demandeDeLivraisons")) {
                NodeList deliveryList = root.getElementsByTagName("livraison");
                for (int i = 0; i < deliveryList.getLength(); i++) {
                    Element delivery = (Element) deliveryList.item(i);
                    long pickUpNodeId = Long.parseLong(delivery.getAttribute("adresseEnlevement"));
                    long deliveryNodeId = Long.parseLong(delivery.getAttribute("adresseLivraison"));
                    int pickUpDuration = Integer.parseInt(delivery.getAttribute("dureeEnlevement"));
                    int deliveryDuration = Integer.parseInt(delivery.getAttribute("dureeLivraison"));
                    Quadruplet<Long, Long, Integer, Integer> newDelivery = new Quadruplet<>(pickUpNodeId, deliveryNodeId, pickUpDuration, deliveryDuration);
                    result.add(newDelivery);
                }
            } else
                throw new XMLException("Document non conforme");
        } catch (ParserConfigurationException | XMLException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return result;

    }

    public Pair<Long,Integer> readWarehouse() {

        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(this.xmlFile);
            Element root = document.getDocumentElement();
            if (root.getNodeName().equals("demandeDeLivraisons")) {
                NodeList warehouseList = root.getElementsByTagName("entrepot");
                Element warehouse = (Element) warehouseList.item(0);
                long positionId = Long.parseLong(warehouse.getAttribute("adresse"));
                int startDate = Integer.parseInt(warehouse.getAttribute("heureDepart"));
                return new Pair(positionId,startDate);

            } else {
                throw new XMLException("Document non conforme");
            }
        } catch (ParserConfigurationException | XMLException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return new Pair(0,0);

    }

}