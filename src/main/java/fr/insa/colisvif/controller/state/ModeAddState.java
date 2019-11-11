package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "add" to add
 * a new delivery, by first picking up a pick up node.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ModeAddState implements State {

    /**
     * When in add mode, allow the user to add a pick up node.
     */
    @Override
    public void addPickUpNode() {

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