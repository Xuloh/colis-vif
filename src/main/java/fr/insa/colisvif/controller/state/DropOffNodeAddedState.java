package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;

public class DropOffNodeAddedState implements State {

    private Vertex pickUpVertex;

    private Step stepBefore;

    private long dropOffNodeId;

    protected void entryToState(Vertex pickUpVertex, Step stepBefore, long nodeId) {
        this.pickUpVertex = pickUpVertex;
        this.stepBefore = stepBefore;
        this.dropOffNodeId = nodeId;
    }

    @Override
    public void stepClicked(Controller controller, UIController uiController, CommandList commandList, Step step) {
        controller.getDONASAState().entryToState(dropOffNodeId, pickUpVertex, step);
        controller.setCurrentState(PickUpNodeAddedStepAddedState.class);
    }
}
