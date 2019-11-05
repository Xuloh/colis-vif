package fr.insa.colisvif.controller;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.CityMapFactory;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.DeliveryMapFactory;
import fr.insa.colisvif.util.Quadruplet;
import fr.insa.colisvif.view.MainController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Controller {

    protected final InitialState initialState = new InitialState();

    protected final CityMapLoadedState cityMapLoadedState = new CityMapLoadedState();

    protected final DeliveryMapLoadedState deliveryMapLoadedState = new DeliveryMapLoadedState();

    private CityMap map;

    private CityMapFactory cityMapFactory;

    private DeliveryMapFactory deliveryMapFactory;

    private MainController mainController;

    private State currentState;

    private DeliveryMap deliveryMap;

    public Controller() {
        this.map = null;
        this.cityMapFactory = new CityMapFactory();
        this.currentState = initialState;
        this.deliveryMapFactory = new DeliveryMapFactory();
    }

    public void openFile(File file) {
        try {
            this.map = this.cityMapFactory.createCityMapFromXMLFile(file);
            this.mainController.setCityMap(map);
            this.mainController.clearMap();
            this.mainController.drawMap();
        } catch (IOException | SAXException | ParserConfigurationException | IdError e) {
            e.printStackTrace();
        }
    }

    public void openDeliveryMap(File file) {
        try {
            this.deliveryMap = this.deliveryMapFactory.createDeliveryMapFromXML(file);
            this.mainController.writeDeliveries(this.deliveryMap);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public CityMap getMap() {
        return map;
    }

    public void loadCityMap(File file) {
        this.currentState.loadCityMap(this, mainController, file);
    }

    public void loadDeliveryMap() {
        this.currentState.loadDeliveryMap(this, mainController);
    }
}