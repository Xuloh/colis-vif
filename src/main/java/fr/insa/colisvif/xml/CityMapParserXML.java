// TODO : vérifier IDs de noeuds uniques
// TODO : pas deux sections exactement identiques

package fr.insa.colisvif.xml;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.util.Triplet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CityMapParserXML implements CityMapParser{
    private File xmlFile;

    public CityMapParserXML() {

    }

    @Override
    public void loadFile() throws ParserConfigurationException, SAXException, IOException{
        try {
            this.xmlFile = XMLFileOpener.getInstance().openXML(true);
        } catch (XMLException e) {

        }
    }

    @Override
    public List<Triplet<Long, Double, Double>> readNodes() {
        ArrayList<Triplet<Long, Double, Double>> result = new ArrayList<Triplet<Long, Double, Double>>();
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XMLException exceptionXML) {
            exceptionXML.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Quadruplet<Double, String, Long, Long>> readSections() {
        ArrayList<Quadruplet<Double, String, Long, Long>> result = new ArrayList<Quadruplet<Double, String, Long, Long>>();
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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XMLException exceptionXML) {
            exceptionXML.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return result;
    }


}
