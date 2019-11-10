package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdException;
import fr.insa.colisvif.exception.InvalidFilePermissionException;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that creates a {@link DeliveryMap} out of a file.
 */
public class DeliveryMapFactory {
    private File xmlFile;

    /**
     * Creates a {@link DeliveryMapFactory}.
     */
    public DeliveryMapFactory() {
    }

    /**
     * Reads and build a {@link DeliveryMap} from a XML File.
     *
     * @param file the file to read, must be XML.
     * @param cityMap the {@link CityMap} corresponding to the {@link DeliveryMap} to check that the {@link Node} id of the Delivery correspond to an existing {@link Node}.
     * @return a {@link DeliveryMap}.
     * @throws IOException if the file does not exists or is not readable
     * (permissions) or any IO errors occur.
     * @throws SAXException if the XML file is not well formed.
     * @throws ParserConfigurationException if a DocumentBuilder
     * cannot be created which satisfies the configuration requested.
     * @throws IdException if the delivery id does not correspond to any existing {@link Node} id.
     */
    public DeliveryMap createDeliveryMapFromXML(File file, CityMap cityMap) throws IdException, ParserConfigurationException, SAXException, IOException {
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
                    throw new IdException(file.getAbsolutePath() + " refers to nodes outside the current city map");
                }
            }
            deliveryMap.createWarehouse(warehouse.getKey(), warehouse.getValue());
            return deliveryMap;
        } catch (XMLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Loads a {@link File} to read it.
     *
     * @param file the file to read
     * @return an {@link Element} corresponding to the root of the XML file.
     * @throws IOException if the file does not exists or is not readable (permissions)
     * or any IO errors occur.
     * @throws ParserConfigurationException if a DocumentBuilder
     * cannot be created which satisfies the configuration requested.
     * @throws SAXException If any parse errors occur.
     */
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
        return document.getDocumentElement();
    }

    /**
     * Read all the {@link Delivery} out of an {@link Element} that corresponds to the
     * root of a XML document.
     *
     * @param root the root of the XML document.
     * @return a {@link List} of {@link Quadruplet} of long, long, integer, integer,
     * the four arguments of a {@link Delivery} (pickUpNodeId, dropOffNodeId, pickUpDuration, dropOffDuration).
     * @throws XMLException if the XML document is not well formed.
     */
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

    /**
     * Reads the warehouse that corresponds to the root of a XML document.
     * @param root the root of the XML document.
     * @return a {@link Pair} of long, integer, the two attributes of a warehouse ({@link Node} id, start date in seconds).
     * @throws XMLException if the XML document is not well formed.
     */
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

    /**
     * Transform a date in the format "hh:mm:ss' into seconds.
     * @param startDate the date to convert.
     * @return the conversion of the date in seconds.
     */
    private int transformStartDateToSeconds(String startDate) {
        // TODO: tester que startDate est bien formé avec regex
        String[] timeComponents = startDate.split(":");
        int startInSeconds = Integer.parseInt(timeComponents[0]) * 3600
                             + Integer.parseInt(timeComponents[1]) * 60
                             + Integer.parseInt(timeComponents[2]);
        return startInSeconds;
    }
}
