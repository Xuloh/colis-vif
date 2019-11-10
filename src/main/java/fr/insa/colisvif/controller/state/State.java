package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;

import java.io.File;

/**
 * The state interface allow to implement a state-specific behaviour for each state.
 *
 *
 * <br>The classes that implements the interface {@link State} are :
 * <ul>
 *     <li>{@link InitialState}</li>
 *     <li>{@link CityMapLoadedState}</li>
 *     <li>{@link DeliveryMapLoadedState}</li>
 *     <li>{@link ItineraryCalculatedState}</li>
 *     <li>{@link LocalItineraryModificationState}</li>
 *     <li>{@link ModifyOrderState}</li>
 *     <li>{@link ModifyStopLocationState}</li>
 *     <li>{@link NonOptimizedItineraryState}</li>
 *     <li>{@link ModeAddState}</li>
 *     <li>{@link PropertiesPrintedState}</li>
 *     <li>{@link SuppressionModeState}</li>
 *     <li>{@link PickUpNodeAddedState}</li>
 * </ul>
 *
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public interface State {

    /**
     * Creates a {@link CityMap} that will be stocked in the <code>controller</code> from a {@link File}.
     * @param controller controller of the application
     * @param uiController controller of the user interface
     * @param file an xml file that contains the map to load
     *
     * @see Controller
     */
    default void loadCityMap(Controller controller, UIController uiController, File file) {

    }


    /**
     * Creates a {@link fr.insa.colisvif.model.DeliveryMap} that will be stocked in the <code>controller</code> from a {@link File}.
     * @param controller controller of the application
     * @param uiController controller of the user interface
     * @param file an xml file that contains the deliveries to load
     * @param cityMap the map of the city
     *
     * @see Controller
     */
    default void loadDeliveryMap(Controller controller, UIController uiController, File file, CityMap cityMap) {

    }

    /**
     * Calculate a {@link fr.insa.colisvif.model.Round}
     * when a {@link CityMap} and a {@link fr.insa.colisvif.model.DeliveryMap} are loaded.
     */
    default void calculateItinerary() {

    }

    /**
     * Save the road map associated to a {@link fr.insa.colisvif.model.Round} in a text file.
     */
    default void saveRoadMap() {

    }

    /**
     * Enter in suppression mode //todo : Ca fait quoi reellement ?
     */
    default void switchToSuppressionMode() {

    }

    /**
     * Enter in add mode //todo : Ca fait quoi reellement ?
     */
    default void switchToAddMode() {

    }

    /**
     * When in add mode, allow the user to add a pick up node.
     */
    default void addPickUpNode() {

    }

    /**
     * When in add mode, allow the user to add a drop off node.
     */
    default void addDropOffNode() {

    }

    /**
     * Show the properties of a node selected from the textual view or from the canvas.
     */
    default void showNodeProperties()  {

    }

    /**
     * Enter in order change mode //todo : Ca fait quoi reellement ?
     */
    default void switchToOrderChangeMode() {

    }

    /**
     * Enter in location change mode //todo : Ca fait quoi reellement ?
     */
    default void switchToLocationChange() {

    }

    /**
     * When in location change mode, allow the user to edit the node location.
     */
    default void changeNodeLocation() {

    }

    /**
     * Continue the calculation of the best itinerary.
     */
    default void continueCalculation() {

    }

    /**
     * Stop the calculation of the best itinerary.
     * // todo : ça sera surement à supprimer (voir {@link NonOptimizedItineraryState}
     */
    default void stopCalculation() {

    }

    /**
     * Edit the order of the node to edit right after the picked stop
     */
    default void selectStopToPass() {

    }

    /**
     * When a modification appear, calculate locally the best new itinerary.
     */
    default void calculateItineraryLocally() {

    }

    /**
     * Delete the delivery where the stop selected by the user appear.
     */
    default void selectStopToDelete() {

    }

    /**
     * Used when the user want to undo his/her modifications. //todo : pas sûre de moi
     * @param controller
     */
    default void getBackToPreviousState(Controller controller) {

    }

}
