package fr.insa.colisvif.xml;

import fr.insa.colisvif.util.Quadruplet;
import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DeliveryRequestParser {

    List<Quadruplet<Long, Long, Integer, Integer>> readDelivery();
    List<Pair<Long,Integer>> readWarehouse();

    void loadFile(File file) throws ParserConfigurationException, SAXException, IOException;
}
