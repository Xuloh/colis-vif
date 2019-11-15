package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;

import java.io.File;

/**
 * The state interface allow to implement a state-specific behaviour for each
 * state.
 *
 *
 * <br>The classes that implements the interface {@link State} are :
 * <ul>
 *     <li>{@link InitialState}</li>
 *     <li>{@link CityMapLoadedState}</li>
 *     <li>{@link DeliveryMapLoadedState}</li>
 *     <li>{@link ItineraryCalculatedState}</li>
 *     <li>{@link ModifyOrderState}</li>
 *     <li>{@link ModifyStopLocationState}</li>
 *     <li>{@link ModeAddState}</li>
 *     <li>{@link ItineraryCalculatedState}</li>
 *     <li>{@link PickUpNodeAddedState}</li>
 * </ul>
 *
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public interface State {

    /**
     * Creates a {@link CityMap} that will be stocked in the
     * <code>controller</code> from a {@link File}.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param file an xml file that contains the map to load
     *
     * @see Controller
     */
    default void loadCityMap(Controller controller, UIController uiController,
                             File file) {

    }


    /**
     * Creates a {@link fr.insa.colisvif.model.DeliveryMap} that will be
     * stocked in the <code>controller</code> from a {@link File}.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param file an xml file that contains the deliveries to load
     * @param cityMap the map of the city
     *
     * @see Controller
     */
    default void loadDeliveryMap(Controller controller,
                                 UIController uiController,
                                 File file, CityMap cityMap) {

    }

    /**
     * Calculate a {@link fr.insa.colisvif.model.Round}
     * when a {@link CityMap} and a {@link fr.insa.colisvif.model.DeliveryMap}
     * are loaded.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param naive true if we want to use the naive algorithm to calculate
     *              the {@link fr.insa.colisvif.model.Round} itinerary
     */
    default void calculateItinerary(Controller controller,
                                    UIController uiController, boolean naive) {

    }


    /**
     * Save the road map associated to a {@link fr.insa.colisvif.model.Round}
     * in a text file.
     * @param controller {@link Controller} of the application
     * @param file the file where the road map will be saved
     */
    default void saveRoadMap(Controller controller, File file) {

    }

    /**
     * Enter in add mode to allow the user to ad a new delivery to the map
     *
     * @see ModeAddState
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     */
    default void switchToAddMode(Controller controller,
                                 UIController uiController) {

    }

    /**
     * Enter in order change mode to allow the user to changer the delivery
     * order
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param step the step the user wants to modify
     */
    default void switchToOrderChangeMode(Controller controller,
                                         UIController uiController, Step step) {

    }

    /**
     * Enter in location change mode to allow the user to change the position
     * of a vertex
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param step the step the user wants to modify
     */
    default void switchToLocationChange(Controller controller,
                                        UIController uiController, Step step) {

    }

    /**
     * Used when the user wants to get back to a stable state where no
     * modifications are happening.
     * @param controller {@link Controller} of the application
     */
    default void getBackToPreviousState(Controller controller) {

    }

    /**
     * An implementation of this should delete a delivery with a given
     * {@link Step}
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     * @param step One of the two {@link Step} in the
     *             {@link fr.insa.colisvif.model.Delivery} the user wants
     *             to suppress
     */
    default void deleteDelivery(Controller controller,
                                UIController uiController,
                                CommandList commandList, Step step) {

    }

    /**
     * An implementation of this method should undo a change.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     */
    default void undo(Controller controller, UIController uiController,
                      CommandList commandList) {

    }

    /**
     * An implementation of this method should redo a change.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     */
    default void redo(Controller controller, UIController uiController,
                      CommandList commandList) {

    }

    /**
     * An implementation of this should get a
     * {@link fr.insa.colisvif.model.Node} or a {@link Vertex} and use them
     * for modifications
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     * @param nodeId the id of the node or vertex clicked
     * @param vertex the vertex clicked or null
     */
    default void leftClick(Controller controller, UIController uiController,
                           CommandList commandList, long nodeId,
                           Vertex vertex) {

    }

    /**
     * An implementation of this should call a leftClick() function but
     * will not get its parameters the same way
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     * @param vertex the vertex clicke on the stepView
     */
    default void selectedStepFromStepView(Controller controller,
                                          UIController uiController,
                                          CommandList commandList,
                                          Vertex vertex) {

    }
}
