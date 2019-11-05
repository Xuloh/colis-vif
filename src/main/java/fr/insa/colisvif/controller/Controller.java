package fr.insa.colisvif.controller;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.CityMapFactory;
import fr.insa.colisvif.view.MainController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Controller {

    private CityMap map;

    private CityMapFactory factory;

    private MainController mainController;

    public Controller() {
        this.map = null;
        this.factory = new CityMapFactory();
    }

    public void openFile(File file) {
        try {
            this.map = this.factory.createCityMapFromXMLFile(file);
            this.mainController.setCityMap(map);
            this.mainController.clearMap();
            this.mainController.drawMap();
        } catch (IOException | SAXException | ParserConfigurationException | IdError e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
