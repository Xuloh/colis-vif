package fr.insa.colisvif.xml;

import fr.insa.colisvif.model.CityMapFactory;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
    public List<Triplet<Integer, Double, Double>> readNodes() {
        ArrayList<Triplet<Integer, Double, Double>> result = new ArrayList<Triplet<Integer, Double, Double>>();
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(this.xmlFile);
            Element root = document.getDocumentElement();
            if (root.getNodeName().equals("plan")) {

            } else
                throw new XMLException("Document non conforme");
            NodeList nodeList = root.getElementsByTagName("noeud");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);
                int id = Integer.parseInt(node.getAttribute("id"));
                double latitude = Double.parseDouble(node.getAttribute("latitude"));
                double longitude = Double.parseDouble(node.getAttribute("longitude"));
                Triplet<Integer, Double, Double> newNode = new Triplet<>(id, latitude, longitude);
                result.add(newNode);
            }
            return result;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XMLException exceptionXML) {
            exceptionXML.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Quadruplet<Double, String, Integer, Integer>> readSections() {
        ArrayList<Quadruplet<Double, String, Integer, Integer>> result = new ArrayList<Quadruplet<Double, String, Integer, Integer>>();
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(this.xmlFile);
            Element root = document.getDocumentElement();
            if (root.getNodeName().equals("plan")) {
                NodeList nodeList = root.getElementsByTagName("troncon");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element node = (Element) nodeList.item(i);
                    double length = Double.parseDouble(node.getAttribute("longueur"));
                    String roadName = node.getAttribute("nomRue");
                    int origin = Integer.parseInt(node.getAttribute("origine"));
                    int destination = Integer.parseInt(node.getAttribute("destination"));
                    Quadruplet<Double, String, Integer, Integer> newSection = new Quadruplet<>(length, roadName, origin, destination);
                    result.add(newSection);
                }
            } else {
                throw new XMLException("Document non conforme");
            }
            return result;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (XMLException exceptionXML) {
            exceptionXML.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return null;
    }


}
