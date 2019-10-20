package fr.insa.colisvif.model;

public class CityMapFactory {
    private CityMap cityMap;
    private CityMapParser cityMapParserXML;
    private String fileName;

    public CityMapFactory() {
        this.cityMap = new CityMap();
        this.cityMapParserXML = new CityMapParserXML();
    }

    public CityMap createCityMapFromXMLFile(String fileName){
        this.fileName = fileName;
        this.cityMapParserXML.readFile(this.fileName);
        return this.cityMap;
    }

    public void makeNode(Integer id, Integer latitude, Integer longitude) {
        this.cityMap.createNode(id, latitude, longitude);
    }

    public void makeSection(Integer length, String roadName, Integer destination, Integer origine) {
        this.cityMap.createSection(length, roadName, destination, origine);
    }

    public CityMap getCityMap() {
        return this.cityMap;
    }
}
