package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.InvalidFilePermissionException;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.util.Quadruplet;
import java.rmi.server.ExportException;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryMapFactory {
    private File xmlFile;

    public DeliveryMapFactory() {
    }

    public DeliveryMap createDeliveryMapFromXML(File file, CityMap cityMap) throws IOException, SAXException, ParserConfigurationException {
        try {
            Element root = loadFile(file);
            DeliveryMap deliveryMap = new DeliveryMap();
            List<Quadruplet<Long, Long, Integer, Integer>> deliveryList = readDelivery(root);
            Pair<Long, Integer> warehouse = readWarehouse(root);
            for (Quadruplet<Long, Long, Integer, Integer> delivery : deliveryList) {
                if (cityMap.getMapNode().containsKey(delivery.getFirst()) && cityMap.getMapNode()
                    .containsKey(delivery.getSecond())) {
                    deliveryMap.createDelivery(delivery.getFirst(), delivery.getSecond(),
                        delivery.getThird(), delivery.getFourth());
                } else {
                    deliveryMap.createImpossibleDelivery(delivery.getFirst(), delivery.getSecond(),
                        delivery.getThird(), delivery.getFourth());
                }
            }
            deliveryMap.createWarehouse(warehouse.getKey(), warehouse.getValue());
            return deliveryMap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Element loadFile(File file) throws IOException, ParserConfigurationException, SAXException  {
        this.xmlFile = file;

        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + " not found.");
        }

        if (!file.canRead()) {
            throw new InvalidFilePermissionException(file.getAbsolutePath() + " : file not readable");
        }

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(this.xmlFile);
        Element root = document.getDocumentElement();

        return root;
    }


    public List<Quadruplet<Long, Long, Integer, Integer>> readDelivery(Element root)
        throws XMLException {
        List<Quadruplet<Long, Long, Integer, Integer>> result = new ArrayList<>();

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
            } else {
                throw new XMLException("Document non conforme");
            }

        return result;
    }

    public Pair<Long, Integer> readWarehouse(Element root) throws XMLException {

            if (root.getNodeName().equals("demandeDeLivraisons")) {
                NodeList warehouseList = root.getElementsByTagName("entrepot");
                Element warehouse = (Element) warehouseList.item(0);
                long positionId = Long.parseLong(warehouse.getAttribute("adresse"));
                String startDateString = warehouse.getAttribute("heureDepart");
                int startDate = transformStartDateToSeconds(startDateString);
                return new Pair(positionId, startDate);
            } else {
                throw new XMLException("Document non conforme");
            }

    }

    private int transformStartDateToSeconds(String startDate) {
        String[] timeComponents = startDate.split(":");
        int startInSeconds = Integer.parseInt(timeComponents[0]) * 3600
                             + Integer.parseInt(timeComponents[1]) * 60
                             + Integer.parseInt(timeComponents[2]);
        return startInSeconds;
    }
}
