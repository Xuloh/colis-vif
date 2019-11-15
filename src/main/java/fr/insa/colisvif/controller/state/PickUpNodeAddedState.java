package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
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

    /**
     * Selects the step that will happen after adding the pick-up node added previously.
     * @param controller controller of the application
     * @param uiController UIController of the application
     * @param commandList command list of the controller
     * @param nodeId id of the node clicked
     * @param vertex Vertex clicked
     */
    @Override
    public void leftClick(Controller controller, UIController uiController, CommandList commandList, long nodeId, Vertex vertex) {
        Step stepOfVertex = null;
        for (Step step : controller.getRound().getSteps()) {
            if (step.getDeliveryID() == vertex.getDeliveryId() && step.isPickUp() != vertex.getType()) {
                stepOfVertex = step;
            }
        }
        int pickUpDuration = uiController.getTimeFromPicker() * 60;
        Vertex pickUpVertex = new Vertex(pickUpNodeId, true, pickUpDuration);
        uiController.clearTimePicker();
        uiController.setShowCityMapNodesOnHover(true);
        controller.getMADOState().entryToState(pickUpVertex, stepOfVertex);
        uiController.addDropOff();
        uiController.printStatus("Sélection la position du noeud de dépôt.");
        controller.setCurrentState(ModeAddDropOffState.class);
    }

    /**
     * Used when the user want to switch back to the
     * {@link fr.insa.colisvif.controller.state.ItineraryCalculatedState}
     * where no modifications can be done.
     * @param controller
     */
    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.getUIController().enableButtons();
        controller.getUIController().clearTimePicker();
        controller.getUIController().printStatus("Annulation de l'opération en cours.");
        controller.setCurrentState(ItineraryCalculatedState.class);
    }

    /**
     * Allows the setting of the attributes through another step
     * @param nodeId the id of the pick-up node added
     */
    protected void entryToState(long nodeId) {
        this.pickUpNodeId = nodeId;
    }

    /**
     * Calls the leftClick action (click on the canvas) from a click on the textual View
     * {@link fr.insa.colisvif.view.StepView}
     * @param controller controller of the application
     * @param uiController UIController of the application
     * @param commandList command list of the controller
     * @param vertex vertex selected from the stepView
     */
    @Override
    public void selectedStepFromStepView(Controller controller, UIController uiController, CommandList commandList, Vertex vertex) {
        this.leftClick(controller, uiController, commandList, 0, vertex);
    }

}
