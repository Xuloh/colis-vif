package fr.insa.colisvif.model;

import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.util.Triplet;
import fr.insa.colisvif.xml.CityMapParser;
import fr.insa.colisvif.xml.CityMapParserXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class CityMapFactory {
    private static CityMapFactory instance;
    private CityMap cityMap;
    private CityMapParser cityMapParserXML;

    private CityMapFactory() {
        this.cityMap = new CityMap();
        this.cityMapParserXML = new CityMapParserXML();
    }

    public CityMap createCityMapFromXMLFile() throws IOException, SAXException, ParserConfigurationException {
        this.cityMapParserXML.loadFile();
        List<Triplet<Integer, Double, Double>> nodes = this.cityMapParserXML.readNodes();
        List<Quadruplet<Double, String, Integer, Integer>> sections = this.cityMapParserXML.readSections();
        for (Triplet<Integer, Double, Double> node : nodes) {
            this.makeNode(node.getFirst(), node.getSecond(), node.getThird());
        }
        for(Quadruplet<Double, String, Integer, Integer> section : sections) {
            this.makeSection(section.getFirst(), section.getSecond(), section.getThird(), section.getFourth());
        }
        return this.cityMap;
    }

    public void makeNode(int id, double latitude, double longitude) {
        this.cityMap.createNode(id, latitude, longitude);
    }

    public void makeSection(double length, String roadName, int destination, int origine) {
        this.cityMap.createSection(length, roadName, destination, origine);
    }

    public CityMap getCityMap() {
        return this.cityMap;
    }

    public static CityMapFactory getInstance() {
        if (instance == null)
            instance = new CityMapFactory();
        return instance;
    }
}
