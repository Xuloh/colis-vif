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
    private State currentState;

    protected final InitialState initialState = new InitialState();
    protected final CityMapLoadedState cityMapLoadedState = new CityMapLoadedState();
    protected final DeliveryMapLoadedState deliveryMapLoadedState = new DeliveryMapLoadedState();

    public Controller() {
        this.map = null;
        this.factory = new CityMapFactory();
        this.currentState = initialState;
    }

    public void openFile(File file) {
        try {
            this.map = this.factory.createCityMapFromXMLFile(file);
            this.mainController.clearMap();
            this.mainController.drawMap(this.map);
        } catch (IOException | SAXException | ParserConfigurationException | IdError e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void loadCityMap() { this.currentState.loadCityMap(this, mainController); }

    public void loadDeliveryMap() { this.currentState.loadDeliveryMap(this, mainController); }
}
