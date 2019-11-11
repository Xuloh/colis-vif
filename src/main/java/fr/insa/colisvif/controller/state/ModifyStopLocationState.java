package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "modify location".
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ModifyStopLocationState implements State {

    /**
     * When in location change mode, allow the user to edit the node location.
     */
    @Override
    public void changeNodeLocation() {

    }

    /**
     * Used when the user want to undo his/her modifications. //todo : pas sûre de moi
     * @param controller
     */
    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.setCurrentState(PropertiesPrintedState.class);
    }
}
