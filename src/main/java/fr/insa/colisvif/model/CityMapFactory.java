package fr.insa.colisvif.model;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.util.Triplet;
import fr.insa.colisvif.xml.CityMapParser;
import fr.insa.colisvif.xml.CityMapParserXML;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CityMapFactory {
    private CityMapParser cityMapParserXML;

    public CityMapFactory() {
        this.cityMapParserXML = new CityMapParserXML();
    }

    //TODO on doit pouvoir passer le fichier en paramètre !
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
}
