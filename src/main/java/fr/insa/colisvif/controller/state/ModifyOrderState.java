package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "modify order".
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ModifyOrderState implements State {

    /**
     * Edit the order of the node to edit right after the picked stop
     */
    @Override
    public void selectStopToPass() {

    }

    /**
     * Used when the user want to switch back to the
     * {@link fr.insa.colisvif.controller.state.PropertiesPrintedState}
     * where no modifications can be done.
     * @param controller
     */
    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.setCurrentState(PropertiesPrintedState.class);
    }
}
