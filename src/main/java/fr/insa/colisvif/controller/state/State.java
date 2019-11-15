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
     * @param controller controller of the application
     * @param uiController controller of the user interface
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
     * @param controller controller of the application
     * @param uiController controller of the user interface
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
     */
    default void calculateItinerary(Controller controller,
                                    UIController uiController, boolean naive) {

    }

    /**
     * Save the road map associated to a {@link fr.insa.colisvif.model.Round}
     * in a text file.
     */
    default void saveRoadMap(Controller controller, File file) {

    }


    /**
     * Enter in add mode to allow the user to ad a new delivery to the map
     *
     * @see ModeAddState
     */
    default void switchToAddMode(Controller controller,
                                 UIController uiController) {

    }


    /**
     * Enter in order change mode to allow the user to changer the delivery
     * order
     */
    default void switchToOrderChangeMode(Controller controller,
                                         UIController uiController, Step step) {

    }

    /**
     * Enter in location change mode to allow the user to change the position
     * of a vertex
     */
    default void switchToLocationChange(Controller controller,
                                        UIController uiController, Step step) {

    }

    /**
     * Used when the user wants to get back to a stable state where no
     * modifications are happening.
     * @param controller
     */
    default void getBackToPreviousState(Controller controller) {

    }

    default void deleteDelivery(Controller controller,
                                UIController uiController,
                                CommandList commandList, Step step) {

    }

    default void undo(Controller controller, UIController uiController,
                      CommandList commandList) {

    }

    default void redo(Controller controller, UIController uiController,
                      CommandList commandList) {

    }

    default void leftClick(Controller controller, UIController uiController,
                           CommandList commandList, long nodeId,
                           Vertex vertex) {

    }

    default void selectedStepFromStepView(Controller controller,
                                          UIController uiController,
                                          CommandList commandList,
                                          Vertex vertex) {

    }
}
