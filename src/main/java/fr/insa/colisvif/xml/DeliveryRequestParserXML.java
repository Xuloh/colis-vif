package fr.insa.colisvif.xml;


import fr.insa.colisvif.util.Quadruplet;
import javafx.util.Pair;

import java.io.File;
import java.util.List;

public class DeliveryRequestParserXML implements DeliveryRequestParser {

    private File xmlFile;

    public DeliveryRequestParserXML() {

    }

    @Override
    public void loadFile(File file) {
        this.xmlFile = file;
    }

    @Override
    public List<Quadruplet<Long, Long, Integer, Integer>> readDelivery() {

        return null;
    }

    @Override
    public List<Pair<Long,Integer>> readWarehouse() {

        return null;
    }

}
