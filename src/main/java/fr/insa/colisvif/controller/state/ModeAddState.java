package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
     * When in add mode, allows the user to select the position
     * of the pick-up and drop-off nodes
     * of the delivery the user wants to add.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     * @param nodeId node where the pick up node will be placed
     * @param vertex not used here
     */
    @Override
    public void leftClick(Controller controller, UIController uiController,
                          CommandList commandList, long nodeId, Vertex vertex) {
        controller.getPUNState().entryToState(nodeId);
        uiController.setShowCityMapNodesOnHover(false);
        uiController.printStatus("Sélectionnez l'étape "
                + "précédant l'enlèvement.");
        controller.setCurrentState(PickUpNodeAddedState.class);
    }

    /**
     * Used when the user want to switch back to the
     * {@link fr.insa.colisvif.controller.state.ItineraryCalculatedState}
     * where no modifications can be done.
     * <br><br>
     * Note that everything added will be lost.
     * @param controller {@link Controller} of the application
     */
    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.getUIController().enableButtons();
        controller.getUIController().clearTimePicker();
        controller.getUIController().printStatus("Annulation de "
                + "l'opération en cours.");
        controller.getUIController().setShowCityMapNodesOnHover(false);
        controller.setCurrentState(ItineraryCalculatedState.class);
    }

}
