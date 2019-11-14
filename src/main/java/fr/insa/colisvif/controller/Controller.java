package fr.insa.colisvif.controller;

import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.controller.state.*;
import fr.insa.colisvif.model.*;
import fr.insa.colisvif.view.UIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
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

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    private CommandList commandList = new CommandList();

    private Map<Class, State> stateMap = new HashMap<>();

    private CityMap cityMap;

    private CityMapFactory cityMapFactory;

    private DeliveryMapFactory deliveryMapFactory;

    private UIController uiController;

    private State currentState;

    private DeliveryMap deliveryMap;

    private Round round;

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
        this.stateMap.put(PickUpNodeAddedStepAddedState.class, new PickUpNodeAddedStepAddedState());
        this.stateMap.put(DropOffNodeAddedState.class, new DropOffNodeAddedState());
        this.stateMap.put(ModeAddDropOffState.class, new ModeAddDropOffState());


        LOGGER.info("Initial Controller state : {}", this.currentState.getClass().getSimpleName());
    }

    /**
     * Loads a {@link CityMap} from the given {@link File}.
     * If the current {@link State} doesn't handle loading a {@link CityMap},
     * nothing happens.
     *
     * @param file the {@link File} to load a {@link CityMap} from
     */
    public void loadCityMap(File file) {
        if (file == null) {
            LOGGER.info("Aucun fichier choisi");
        } else {
            LOGGER.info("Loading new CityMap from : " + file.getAbsolutePath());
            this.currentState.loadCityMap(this, uiController, file);
        }
    }

    /**
     * Loads a {@link DeliveryMap} from the given {@link File}.
     * If the current {@link State} doesn't handle loading a {@link DeliveryMap},
     * nothing happens.
     *
     * @param file the {@link File} to load a {@link DeliveryMap} from
     */
    public void loadDeliveryMap(File file) {
        if (file == null) {
            LOGGER.info("Aucun fichier choisi");
        } else {
            LOGGER.info("Loading new DeliveryMap from : " + file.getAbsolutePath());
            this.currentState.loadDeliveryMap(this, uiController, file, this.cityMap);
        }
    }

    public void exportRound(File file) {
        if (file == null) {
            LOGGER.info("Aucun fichier choisi");
        } else {
            LOGGER.info("Exportation du fichier à l'emplacement " + file.getAbsolutePath());
            this.currentState.saveRoadMap(this, file);
        }
    }

    /**
     * Assigns the given {@link UIController} to this {@link Controller}.
     * It must be assigned before attempting any action on the {@link Controller}.
     *
     * @param uiController the {@link UIController} to assign to this {@link Controller}
     */
    public void setUIController(UIController uiController) {
        this.uiController = uiController;
        uiController.getMapCanvas().addNodeMouseClickHandler((nodeId, vertex) -> {
            this.currentState.leftClick(this, uiController, commandList, nodeId, vertex);
        });
    }


    public UIController getUIController() {
        return this.uiController;
    }

    /**
     * Changes the current {@link State} of this {@link Controller}
     * to the given one.
     *
     * @param stateName The type of {@link State} to switch to
     */
    public <T extends State> void setCurrentState(Class<T> stateName) {
        if (this.stateMap.containsKey(stateName)) {
            LOGGER.info(
                "Switching Controller state from {} to {}",
                this.currentState.getClass().getSimpleName(),
                stateName.getSimpleName()
            );
            this.currentState = stateMap.get(stateName);
        } else {
            LOGGER.warn(
                "Tried to switch Controller state from {} to invalid {}",
                this.currentState.getClass().getSimpleName(),
                stateName.getSimpleName()
            );
        }
    }

    /**
     * Assigns the given {@link CityMap} to this {@link Controller}
     *
     * @param cityMap the {@link CityMap} to assign to this {@link Controller}
     */
    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
        this.uiController.updateCityMap();
        this.setDeliveryMap(null);
    }

    /**
     * Assigns the given {@link Round} to this {@link Controller}
     *
     * @param round the {@link Round} to assign to this {@link Controller}
     */
    public void setRound(Round round) {
        this.round = round;
        this.uiController.updateRound();
        this.uiController.setButtons();
    }

    /**
     * Returns the associated {@link Round}
     * @return the associated {@link Round}
     */
    public Round getRound() {
        return round;
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
        this.uiController.updateDeliveryMap();
        this.setRound(null);
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
     * Returns the associated {@link Step} {@link List}
     * @return the associated {@link Step} {@link List}
     */
    public List<Step> getStepList() {
        if (round != null) {
            return this.round.getSteps();
        } else {
            return new ArrayList<>();
        }
    }

    public PickUpNodeAddedState getPUNState() {
        return (PickUpNodeAddedState) stateMap.get(PickUpNodeAddedState.class);
    }

    public PickUpNodeAddedStepAddedState getPUNASAState() {
        return (PickUpNodeAddedStepAddedState) stateMap.get(PickUpNodeAddedStepAddedState.class);
    }

    public ModifyStopLocationState getMSLState() {
        return (ModifyStopLocationState) stateMap.get(ModifyStopLocationState.class);
    }

    public ModeAddDropOffState getMADOState() {
        return (ModeAddDropOffState) stateMap.get(ModeAddDropOffState.class);
    }

    public DropOffNodeAddedState getDONAState() {
        return (DropOffNodeAddedState) stateMap.get(DropOffNodeAddedState.class);
    }

    public DropOffNodeAddedStepAdded getDONASAState() {
        return (DropOffNodeAddedStepAdded) stateMap.get(DropOffNodeAddedStepAdded.class);
    }

    public ModifyOrderState getMOState() {
        return (ModifyOrderState) stateMap.get(ModifyOrderState.class);
    }

    public ModifyStopLocationState getMSLSState() {
        return (ModifyStopLocationState) stateMap.get(ModifyStopLocationState.class);
    }

    public void computeRound() {
        this.currentState.calculateItinerary(this, this.uiController);
    }

    /**
     * Switch to "add delivery" mode
     */
    public void addDelivery() {
        this.currentState.switchToAddMode(this, this.uiController);
    }

    /**
     * Switch to "delete delivery" mode
     */
    public void deleteDelivery(Step step) {
        this.currentState.deleteDelivery(this, this.uiController, this.commandList, step);
    }

    /**
     * Switch to "change delivery order" mode
     */
    public void editSequenceDelivery(Step step) {
        this.currentState.switchToOrderChangeMode(this, this.uiController, step);
    }


    public void createVertexList() {
        this.vertexList = FXCollections.observableArrayList();

        if (this.deliveryMap != null) {
            LOGGER.trace("Creating vertex list : {}", this.deliveryMap.getDeliveryList());
            for (Delivery d : this.deliveryMap.getDeliveryList()) {
                vertexList.add(d.getPickUp());
                vertexList.add(d.getDropOff());
            }
        }
    }

    public void saveRoadMap(File file) {
        this.currentState.saveRoadMap(this, file);
    }

    public void setButtons() {
        this.uiController.setButtons();
    }

    public void undo() {
        this.currentState.undo(this, this.uiController, commandList);
    }

    public void redo() {
        this.currentState.redo(this, this.uiController, commandList);
    }

    /**
     * Switch to "change location" mode
     */
    public void editLocationDelivery(Step step) {
        this.currentState.switchToLocationChange(this, this.uiController, step);
    }
}
