package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;

public class ModeAddDropOffState implements State {

    private Vertex pickUpVertex;

    private Step stepBefore;

    protected void entryToState(Vertex pickUpVertex, Step stepBefore) {
        this.pickUpVertex = pickUpVertex;
        this.stepBefore = stepBefore;
    }

    @Override
    public void leftClick(Controller controller, UIController uiController, CommandList commandList, long nodeId, Vertex vertex) {
        controller.getDONAState().entryToState(pickUpVertex, stepBefore, nodeId);
        uiController.setShowCityMapNodesOnHover(false);
        uiController.printStatus("Sélection l'étape suivant le noeud d'enlèvement.");
        controller.setCurrentState(DropOffNodeAddedState.class);
    }

    @Override
    public void getBackToPreviousState(Controller controller) {
        controller.getUIController().enableButtons();
        controller.getUIController().clearTimePicker();
        controller.getUIController().setShowCityMapNodesOnHover(false);
        controller.getUIController().printStatus("Annulation de l'opération en cours.");
        controller.setCurrentState(ItineraryCalculatedState.class);
    }
}
