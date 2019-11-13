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
    public void nodeClicked(Controller controller, UIController uiController, CommandList commandList, Long nodeId) {
        if (nodeId != null) {
            long nodeIdSelected = nodeId;
            controller.getDONAState().entryToState(pickUpVertex, stepBefore, nodeIdSelected);
            controller.setCurrentState(PickUpNodeAddedState.class);
        }
    }
}
