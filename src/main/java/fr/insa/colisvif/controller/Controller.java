package fr.insa.colisvif.controller;

import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.model.*;
import fr.insa.colisvif.view.MainController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Controller {

    protected final InitialState initialState = new InitialState();

    protected final CityMapLoadedState cityMapLoadedState = new CityMapLoadedState();

    protected final DeliveryMapLoadedState deliveryMapLoadedState = new DeliveryMapLoadedState();

    private CityMap map;

    private CityMapFactory cityMapFactory;

    private DeliveryMapFactory deliveryMapFactory;

    private MainController MainController;

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
            this.MainController.getMapCanvas().setCityMap(map);
        } catch (IOException | SAXException | ParserConfigurationException | IdError e) {
            e.printStackTrace();
        }
    }

    public void openDeliveryMap(File file, CityMap cityMap) {
        try {
            this.deliveryMap = this.deliveryMapFactory.createDeliveryMapFromXML(file, cityMap);
            this.MainController.writeImpossibleDelivery(this.deliveryMap);
            this.MainController.writeDeliveries(this.deliveryMap);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.MainController = mainController;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public CityMap getMap() {
        return map;
    }

    public void loadCityMap(File file) {
        this.currentState.loadCityMap(this, MainController, file);
    }

    public void loadDeliveryMap(File file, CityMap cityMap) {
        this.currentState.loadDeliveryMap(this, MainController, file, cityMap);
    }
}
