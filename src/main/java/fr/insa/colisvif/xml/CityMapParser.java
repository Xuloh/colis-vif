package fr.insa.colisvif.xml;

import java.io.IOException;
import java.util.List;
import fr.insa.colisvif.util.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public interface CityMapParser {
    List<Triplet<Integer, Double, Double>> readNodes();
    List<Quadruplet<Double, String, Integer, Integer>> readSections();
    void loadFile() throws ParserConfigurationException, SAXException, IOException;
}
