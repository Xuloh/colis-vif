package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.controller.command.CommandRemove;
import fr.insa.colisvif.model.Delivery;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;

/**
 * Class that implements State interface.
 * This class represents the state when the application is in mode "suppression".
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class SuppressionModeState implements State {

    /**
     * Delete the delivery where the stop selected by the user appear.
     */
    @Override
    public void selectStopToDelete() {

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

    @Override
    public void leftClick(Controller controller, UIController uiController, CommandList commandList) {
        /*Step stepToSuppress = uiController.getMapCanvas().getStepFromCoordinates(); n'existe pas, sera
         suivi de tests pour savoir si on a bien un bon vertex de cliqué*/
        /*Step stepToSuppress = null; // pour pas casser le build
        Step otherStep;
        Delivery deliveryToSuppress = controller.getDeliveryMap().getDeliveryPerId(stepToSuppress.getDeliveryID());
        if (stepToSuppress.isPickUp()) {
            //otherStep = new Step(deliveryToSuppress.getDropOff(), deliveryToSuppress.getId());
        } else {
            //otherStep = new Step(deliveryToSuppress.getPickUp(), deliveryToSuppress.getId());
        }
        //controller.getRound().removeDelivery(stepToSuppress, otherStep);*/

        Long nodeReturned = null;
        if (nodeReturned != null) {
            long nodeSelectedId = nodeReturned;
            for (Step step : controller.getStepList()) {
                if (step.getArrivalNodeId() == nodeSelectedId) {
                    Step stepSelected = step;
                    int deliveryId = stepSelected.getDeliveryID();
                    for (Step step1 : controller.getStepList()) {
                        if (step1.getDeliveryID() == deliveryId && step != step1) {
                            Step otherDeliveyStep = step1;
                            commandList.doCommand(new CommandRemove(stepSelected, otherDeliveyStep, controller.getRound(), controller.getCityMap()));

                        }
                    }
                }
            }
        }

    }
}
