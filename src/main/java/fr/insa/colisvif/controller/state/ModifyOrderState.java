package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.controller.command.CommandModifyOrder;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "modify order".
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ModifyOrderState implements State {

    private static final Logger LOGGER = LogManager.getLogger(ItineraryCalculatedState.class);

    private Step stepToChange;

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

    @Override
    public void leftClick(Controller controller, UIController uiController, CommandList commandList, long nodeId, Vertex vertex) {
        try {
            // Clic sur l'entrepôt
            if (vertex == null) {
                if (stepToChange.isPickUp()) {
                    commandList.doCommand(new CommandModifyOrder(controller.getRound(), stepToChange, null, controller.getCityMap()));
                } else {
                    uiController.printError("Le changement demandé rentrerait en conflit avec la livraison (dépôt avant enlèvement)");
                }
                // Clic autre part
            } else {
                int indexOfAssociatedStep = -1;
                Step stepBefore = null;
                for (Step step : controller.getRound().getSteps()) {
                    if (step.getDeliveryID() == vertex.getDeliveryId() && step.isPickUp() != vertex.getType()) {
                        stepBefore = step;
                    } else if (step.getDeliveryID() == stepToChange.getDeliveryID() && step != stepToChange) {
                        indexOfAssociatedStep = controller.getStepList().indexOf(step);
                    }
                }
                int indexOfStepChanged = controller.getStepList().indexOf(stepToChange);
                int indexOfTarget = controller.getStepList().indexOf(stepBefore);
                LOGGER.error(indexOfStepChanged + " " + indexOfAssociatedStep + " " + indexOfTarget);
                if (indexOfAssociatedStep > indexOfStepChanged && indexOfTarget < indexOfAssociatedStep) {
                    commandList.doCommand(new CommandModifyOrder(controller.getRound(), stepToChange, stepBefore, controller.getCityMap()));
                    controller.createVertexList();
                    uiController.updateDeliveryMap();
                    uiController.updateRound();
                    uiController.getMapCanvas().redraw();
                    controller.setButtons();
                } else if (indexOfAssociatedStep < indexOfStepChanged && indexOfTarget > indexOfAssociatedStep) {
                    commandList.doCommand(new CommandModifyOrder(controller.getRound(), stepToChange, stepBefore, controller.getCityMap()));
                    controller.createVertexList();
                    uiController.updateDeliveryMap();
                    uiController.updateRound();
                    uiController.getMapCanvas().redraw();
                    controller.setButtons();
                } else {
                    uiController.printError("Le changement demandé rentrerait en conflit avec la livraison (dépôt avant enlèvement)");
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        controller.setCurrentState(ItineraryCalculatedState.class);
    }

    protected void entryToState(Step step) {
        stepToChange = step;
    }
}
