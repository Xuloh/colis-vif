package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.Node;
import fr.insa.colisvif.view.UIController;

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
     * When in add mode, allows the user to add a pick up node.
     */
    @Override
    public void addPickUpNode(Controller controller, UIController uiController, Node node) {

    }

    /**
     * Used when the user want to switch back to the
     * {@link fr.insa.colisvif.controller.state.ItineraryCalculatedState}
     * where no modifications can be done.
     * @param controller
     */
    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.setCurrentState(ItineraryCalculatedState.class);
    }

}
