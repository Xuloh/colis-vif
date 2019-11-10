package fr.insa.colisvif.controller;

import fr.insa.colisvif.controller.state.*;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.CityMapFactory;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.DeliveryMap;
import fr.insa.colisvif.model.DeliveryMapFactory;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The MVC controller used by the app.
 * It handles all the different actions the view can trigger
 * and when they can be triggered.
 * @see State
 */
public class Controller {

    private Map<Class, State> stateMap = new HashMap<>();

    private CityMap cityMap;

    private CityMapFactory cityMapFactory;

    private DeliveryMapFactory deliveryMapFactory;

    private UIController uiController;

    private State currentState;

    private DeliveryMap deliveryMap;

    private ObservableList<Vertex> vertexList;

    /**
     * Creates a new {@link Controller}.
     */
    public Controller() {
        this.cityMap = null;
        this.cityMapFactory = new CityMapFactory();
        this.deliveryMapFactory = new DeliveryMapFactory();

        InitialState initialState = new InitialState();
        this.currentState = initialState;

        this.stateMap.put(InitialState.class, initialState);
        this.stateMap.put(CityMapLoadedState.class, new CityMapLoadedState());
        this.stateMap.put(DeliveryMapLoadedState.class, new DeliveryMapLoadedState());
        this.stateMap.put(PickUpNodeAddedState.class, new PickUpNodeAddedState());
        this.stateMap.put(LocalItineraryModificationState.class, new LocalItineraryModificationState());
        this.stateMap.put(ModifyStopLocationState.class, new ModifyStopLocationState());
        this.stateMap.put(ModifyOrderState.class, new ModifyOrderState());
        this.stateMap.put(ModeAddState.class, new ModeAddState());
        this.stateMap.put(PropertiesPrintedState.class, new PropertiesPrintedState());
        this.stateMap.put(SuppressionModeState.class, new SuppressionModeState());
        this.stateMap.put(ItineraryCalculatedState.class, new ItineraryCalculatedState());
        this.stateMap.put(NonOptimizedItineraryState.class, new NonOptimizedItineraryState());
    }

    /**
     * Loads a {@link CityMap} from the given {@link File}.
     * If the current {@link State} doesn't handle loading a {@link CityMap},
     * nothing happens.
     *
     * @param file the {@link File} to load a {@link CityMap} from
     */
    public void loadCityMap(File file) {
        this.currentState.loadCityMap(this, uiController, file);
    }

    /**
     * Loads a {@link DeliveryMap} from the given {@link File}.
     * If the current {@link State} doesn't handle loading a {@link DeliveryMap},
     * nothing happens.
     *
     * @param file the {@link File} to load a {@link DeliveryMap} from
     */
    public void loadDeliveryMap(File file) {
        this.currentState.loadDeliveryMap(this, uiController, file, this.cityMap);
    }

    /**
     * Assigns the given {@link UIController} to this {@link Controller}.
     * It must be assigned before attempting any action on the {@link Controller}.
     *
     * @param uiController the {@link UIController} to assign to this {@link Controller}
     */
    public void setUIController(UIController uiController) {
        this.uiController = uiController;
    }

    /**
     * Changes the current {@link State} of this {@link Controller}
     * to the given one.
     *
     * @param stateName The type of {@link State} to switch to
     */
    public <T extends State> void setCurrentState(Class<T> stateName) {
        if (stateMap.containsKey(stateName)) {
            this.currentState = stateMap.get(stateName);
        }
    }

    /**
     * Assigns the given {@link CityMap} to this {@link Controller}
     *
     * @param cityMap the {@link CityMap} to assign to this {@link Controller}
     */
    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    /**
     * Returns the associated {@link CityMap}
     * @return the associated {@link CityMap}
     */
    public CityMap getCityMap() {
        return this.cityMap;
    }

    /**
     * Assigns the given {@link DeliveryMap} to this {@link Controller}
     * and creates a {@link Vertex} {@link List} from all the {@link Delivery} instances.
     *
     * @param deliveryMap the {@link DeliveryMap} to assign to this {@link Controller}
     */
    public void setDeliveryMap(DeliveryMap deliveryMap) {
        this.deliveryMap = deliveryMap;
        this.createVertexList();
    }

    /**
     * Returns the associated {@link DeliveryMap}
     * @return the associated {@link DeliveryMap}
     */
    public DeliveryMap getDeliveryMap() {
        return this.deliveryMap;
    }

    /**
     * Returns the associated {@link CityMapFactory}
     * @return the associated {@link CityMapFactory}
     */
    public CityMapFactory getCityMapFactory() {
        return this.cityMapFactory;
    }

    /**
     * Returns the associated {@link DeliveryMapFactory}
     * @return the associated {@link DeliveryMapFactory}
     */
    public DeliveryMapFactory getDeliveryMapFactory() {
        return this.deliveryMapFactory;
    }

    /**
     * Returns the associated {@link Vertex} {@link List}
     * @return the associated {@link Vertex} {@link List}
     */
    public ObservableList<Vertex> getVertexList() {
        return this.vertexList;
    }

    /**
     * Switch to "add delivery" mode
     */
    public void addDelivery() {
        this.currentState.switchToAddMode();
    }

    /**
     * Switch to "delete delivery" mode
     */
    public void deleteDelivery() {
        this.currentState.switchToSuppressionMode();
    }

    /**
     * Switch to "change delivery order" mode
     */
    public void editSequenceDelivery() {
        this.currentState.switchToOrderChangeMode();
    }

    /**
     * Switch to "change location" mode
     */
    public void editLocationDelivery() {
        this.currentState.switchToLocationChange();
    }

    private void createVertexList() {
        this.vertexList = FXCollections.observableArrayList();

        for (Delivery d : this.deliveryMap.getDeliveryList()) {
            vertexList.add(d.getPickUp());
            vertexList.add(d.getDropOff());
        }
    }

}
