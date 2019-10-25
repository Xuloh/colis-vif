package fr.insa.colisvif.model;

import fr.insa.colisvif.xml.DeliveryMapParserXML;

public class DeliveryMapFactory {
    private DeliveryMapParserXML deliveryRequestParserXML;

    public DeliveryMapFactory(DeliveryMapParserXML deliveryRequestParserXML) {
        this.deliveryRequestParserXML = deliveryRequestParserXML;
    }

    /*
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
     */
}
