package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.util.Triplet;
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

public class CityMapFactory {
    private File xmlFile;


    public CityMapFactory() {

    }

    //TODO on doit pouvoir passer le fichier en paramètre !
    public CityMap createCityMapFromXMLFile(File file) throws IOException, SAXException, ParserConfigurationException, IdError {
        //this.cityMapParserXML.loadFile(file);
        loadFile(file);
        CityMap cityMap = new CityMap();
        List<Triplet<Long, Double, Double>> nodes = readNodes();
        List<Quadruplet<Double, String, Long, Long>> sections = readSections();
        //List<Triplet<Long, Double, Double>> nodes = this.cityMapParserXML.readNodes();
        //List<Quadruplet<Double, String, Long, Long>> sections = this.cityMapParserXML.readSections();
        for (Triplet<Long, Double, Double> node : nodes) {
            cityMap.createNode(node.getFirst(), node.getSecond(), node.getThird());
        }
        for(Quadruplet<Double, String, Long, Long> section : sections) {
            cityMap.createSection(section.getFirst(), section.getSecond(), section.getThird(), section.getFourth());
        }
        return cityMap;
    }



    public void loadFile(File file) {
        this.xmlFile = file;
    }


    public List<Triplet<Long, Double, Double>> readNodes() {
        ArrayList<Triplet<Long, Double, Double>> result = new ArrayList<>();
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(this.xmlFile);
            Element root = document.getDocumentElement();
            if (root.getNodeName().equals("reseau")) {
                NodeList nodeList = root.getElementsByTagName("noeud");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element node = (Element) nodeList.item(i);
                    long id = Long.parseLong(node.getAttribute("id"));
                    double latitude = Double.parseDouble(node.getAttribute("latitude"));
                    double longitude = Double.parseDouble(node.getAttribute("longitude"));
                    Triplet<Long, Double, Double> newNode = new Triplet<>(id, latitude, longitude);
                    result.add(newNode);
                }
            } else
                throw new XMLException("Document non conforme");
        } catch (ParserConfigurationException | XMLException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Quadruplet<Double, String, Long, Long>> readSections() {
        ArrayList<Quadruplet<Double, String, Long, Long>> result = new ArrayList<>();
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(this.xmlFile);
            Element root = document.getDocumentElement();
            if (root.getNodeName().equals("reseau")) {
                NodeList nodeList = root.getElementsByTagName("troncon");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element node = (Element) nodeList.item(i);
                    double length = Double.parseDouble(node.getAttribute("longueur"));
                    String roadName = node.getAttribute("nomRue");
                    long origin = Long.parseLong(node.getAttribute("origine"));
                    long destination = Long.parseLong(node.getAttribute("destination"));
                    Quadruplet<Double, String, Long, Long> newSection = new Quadruplet<>(length, roadName, origin, destination);
                    result.add(newSection);
                }
            } else {
                throw new XMLException("Document non conforme");
            }
        } catch (ParserConfigurationException | XMLException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return result;
    }
}
