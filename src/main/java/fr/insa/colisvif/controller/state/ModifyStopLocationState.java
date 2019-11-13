package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.controller.command.CommandModifyLocation;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.view.UIController;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "modify location".
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ModifyStopLocationState implements State {

    private Step stepToChange;

    /**
     * When in location change mode, allow the user to edit the node location.
     */
    @Override
    public void leftClick(Controller controller, UIController uiController, CommandList commandList) {
        Long nodeIdSelected = null; //todo : récup avec la méthode @Pacome
        if (nodeIdSelected != null) {
            long nodeId = nodeIdSelected;
            commandList.doCommand(new CommandModifyLocation(stepToChange, nodeId, controller.getRound(), controller.getCityMap()));
        }
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

    protected void entryToState(Step stepToModify) {
        this.stepToChange = stepToModify;
    }
}
