package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.InvalidFilePermissionException;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.util.Triplet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * Class that creates a {@link CityMap} out of a file.
 */
public class CityMapFactory {

    private static final Logger LOGGER = LogManager.getLogger(
            CityMapFactory.class);

    /**
     * Creates a {@link CityMapFactory}.
     */
    public CityMapFactory() {
    }

    /**
     * Reads and build a {@link CityMap} from a XML File.
     *
     * @param file the file to read, must be XML.
     * @return a {@link CityMap} corresponding to the XML file.
     * @throws IOException if the file does not exists or is not readable
     * (permissions) or any IO errors occur.
     * @throws SAXException if the XML file is not well formed.
     * @throws ParserConfigurationException if a DocumentBuilder
     * cannot be created which satisfies the configuration requested.
     * @throws XMLException if the XML file is not valid.
     */
    public CityMap createCityMapFromXMLFile(File file)
            throws IOException, SAXException,
                    ParserConfigurationException, XMLException {
        Element root = loadFile(file);
        CityMap cityMap = new CityMap();
        List<Triplet<Long, Double, Double>> nodes = readNodes(root);
        List<Quadruplet<Double, String, Long, Long>> sections = readSections(
                                                                root);

        for (Triplet<Long, Double, Double> node : nodes) {
            cityMap.createNode(node.getFirst(), node.getSecond(),
                               node.getThird());
        }
        for (Quadruplet<Double, String, Long, Long> section : sections) {
            cityMap.createSection(section.getFirst(), section.getSecond(),
                                  section.getFourth(), section.getThird());
        }
        return cityMap;
    }

    /**
     * Loads a {@link File} to read it.
     *
     * @param file the file to read
     * @return an {@link Element} corresponding to the root of the XML file.
     * @throws IOException if the file does not exists or is not readable
     * (permissions) or any IO errors occur.
     * @throws ParserConfigurationException if a DocumentBuilder
     * cannot be created which satisfies the configuration requested.
     * @throws SAXException If any parse errors occur.
     */
    public Element loadFile(File file)
            throws IOException, ParserConfigurationException, SAXException {

        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath()
                                            + " not found.");
        }

        if (!file.canRead()) {
            throw new InvalidFilePermissionException(file.getAbsolutePath()
                                                    + " : file not readable");
        }

        DocumentBuilder docBuilder =
                DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(file);
        Element root = document.getDocumentElement();

        return root;
    }

    /**
     * Read all the {@link Node} out of an {@link Element} that corresponds
     * to the root of a XML document.
     *
     * @param root the root of the XML document.
     * @return a {@link List} of {@link Triplet} of long double double,
     * the three arguments of a {@link Node} (id, latitude, longitude).
     * @throws XMLException if the XML document is not well formed.
     */
    public List<Triplet<Long, Double, Double>> readNodes(Element root)
            throws XMLException {
        ArrayList<Triplet<Long, Double, Double>> result = new ArrayList<>();

        if (root.getNodeName().equals("reseau")) {
            NodeList nodeList = root.getElementsByTagName("noeud");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);
                long id = Long.parseLong(node.getAttribute("id"));
                double latitude = Double.parseDouble(
                                        node.getAttribute("latitude"));
                double longitude = Double.parseDouble(
                                        node.getAttribute("longitude"));
                Triplet<Long, Double, Double> newNode =
                        new Triplet<>(id, latitude, longitude);
                result.add(newNode);
            }
        } else {
            throw new XMLException("Document non conforme");
        }

        return result;
    }

    /**
     * Read all the {@link Section} out of an {@link Element} that
     * corresponds to the root of a XML document.
     *
     * @param root the root of the XML document.
     * @return a {@link List} of {@link Quadruplet} of double, String, long,
     * long, the four arguments of a {@link Section} (lenght, road name,
     * destination, origin).
     * @throws XMLException if the XML document is not well formed.
     */
    public List<Quadruplet<Double, String, Long, Long>>
            readSections(Element root)
            throws XMLException {
        ArrayList<Quadruplet<Double, String, Long, Long>> result =
                new ArrayList<>();

        if (root.getNodeName().equals("reseau")) {
            NodeList nodeList = root.getElementsByTagName("troncon");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);
                double length =
                        Double.parseDouble(node.getAttribute("longueur"));
                String roadName = node.getAttribute("nomRue");
                long origin =
                        Long.parseLong(node.getAttribute("origine"));
                long destination =
                        Long.parseLong(node.getAttribute("destination"));
                Quadruplet<Double, String, Long, Long> newSection =
                        new Quadruplet<>(length, roadName, destination, origin);
                result.add(newSection);
            }
        } else {
            throw new XMLException("Document non conforme");
        }

        return result;
    }
}
