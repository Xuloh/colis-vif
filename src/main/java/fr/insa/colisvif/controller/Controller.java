package fr.insa.colisvif.controller;

import fr.insa.colisvif.controller.state.CityMapLoadedState;
import fr.insa.colisvif.controller.state.DeliveryMapLoadedState;
import fr.insa.colisvif.controller.state.InitialState;
import fr.insa.colisvif.controller.state.State;
import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.model.*;
import fr.insa.colisvif.view.UIController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    protected final InitialState initialState = new InitialState();

    protected final CityMapLoadedState cityMapLoadedState = new CityMapLoadedState();

    protected final DeliveryMapLoadedState deliveryMapLoadedState = new DeliveryMapLoadedState();

    private Map<Class, State> stateMap = new HashMap<>();

    private CityMap map;

    private CityMapFactory cityMapFactory;

    private DeliveryMapFactory deliveryMapFactory;

    private UIController uiController;

    private State currentState;

    private DeliveryMap deliveryMap;

    public Controller() {
        this.map = null;
        this.cityMapFactory = new CityMapFactory();
        this.currentState = initialState;
        this.deliveryMapFactory = new DeliveryMapFactory();

        this.stateMap.put(InitialState.class, initialState);
        this.stateMap.put(CityMapLoadedState.class, cityMapLoadedState);
        this.stateMap.put(DeliveryMapLoadedState.class, deliveryMapLoadedState);
    }

    public void openFile(File file) {
        try {
            this.map = this.cityMapFactory.createCityMapFromXMLFile(file);
            this.uiController.getMapCanvas().setCityMap(map);
        } catch (IOException | SAXException | ParserConfigurationException | IdError e) {
            e.printStackTrace();
        }
    }

    public void openDeliveryMap(File file, CityMap cityMap) {
        try {
            this.deliveryMap = this.deliveryMapFactory.createDeliveryMapFromXML(file, cityMap);
            this.uiController.writeImpossibleDelivery(this.deliveryMap);
            this.uiController.writeDeliveries(this.deliveryMap);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void setUIController(UIController uiController) {
        this.uiController = uiController;
    }

    public void setCurrentState(Class stateName) {
        if (stateMap.containsKey(stateName)) {
            this.currentState = stateMap.get(stateName);
        }
    }

    public CityMap getMap() {
        return map;
    }

    public void loadCityMap(File file) {
        this.currentState.loadCityMap(this, UIController, file);
    }

    public void loadDeliveryMap(File file, CityMap cityMap) {
        this.currentState.loadDeliveryMap(this, UIController, file, cityMap);
    }
}
