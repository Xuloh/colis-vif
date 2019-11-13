package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.view.UIController;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "add" to add
 * a new delivery, by secondly picking up a drop off node.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class PickUpNodeAddedState implements State {

    private long pickUpNodeId;

    @Override
    public void stepClicked(Controller controller, UIController uiController, CommandList commandList, Step step) {
        controller.getPUNASState().entryToState(pickUpNodeId, step);
        controller.setCurrentState(PickUpNodeAddedStepAddedState.class);
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

    protected void entryToState(long nodeId) {
        this.pickUpNodeId = nodeId;
    }

}
