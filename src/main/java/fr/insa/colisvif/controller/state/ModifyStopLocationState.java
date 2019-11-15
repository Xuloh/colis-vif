package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.controller.command.CommandModifyLocation;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "modify
 * location". It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ModifyStopLocationState implements State {

    private Step stepToChange;

    /**
     * Used when the user want to switch back to the
     * {@link fr.insa.colisvif.controller.state.ItineraryCalculatedState}
     * where no modifications can be done.
     * @param controller
     */
    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.getUIController().enableButtons();
        controller.getUIController().setShowCityMapNodesOnHover(false);
        controller.getUIController().printStatus("Annulation de "
                + "l'opération en cours.");
        controller.setCurrentState(ItineraryCalculatedState.class);
    }

    protected void entryToState(Step stepToModify) {
        this.stepToChange = stepToModify;
    }

    @Override
    public void leftClick(Controller controller, UIController uiController,
                          CommandList commandList, long nodeId, Vertex vertex) {
        commandList.doCommand(new CommandModifyLocation(stepToChange, nodeId,
                controller.getRound(), controller.getCityMap()));
        controller.createVertexList();
        uiController.updateDeliveryMap();
        uiController.updateRound();
        uiController.getMapCanvas().redraw();
        controller.setButtons();
        uiController.setShowCityMapNodesOnHover(false);
        controller.setCurrentState(ItineraryCalculatedState.class);

    }
}
