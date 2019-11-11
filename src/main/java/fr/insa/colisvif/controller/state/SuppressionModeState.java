package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "suppression".
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class SuppressionModeState implements State {

    /**
     * Delete the delivery where the stop selected by the user appear.
     */
    @Override
    public void selectStopToDelete() {

    }

    /**
     * Used when the user want to undo his/her modifications. //todo : pas sûre de moi
     * @param controller
     */
    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.setCurrentState(ItineraryCalculatedState.class);
    }
}
