package fr.insa.colisvif.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import fr.insa.colisvif.util.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public interface CityMapParser {
    List<Triplet<Long, Double, Double>> readNodes();
    List<Quadruplet<Double, String, Long, Long>> readSections();
    void loadFile(File file) throws ParserConfigurationException, SAXException, IOException;
}
