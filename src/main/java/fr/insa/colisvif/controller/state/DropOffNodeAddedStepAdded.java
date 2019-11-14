package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandAdd;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;

public class DropOffNodeAddedStepAdded implements State {

    long dropOffNodeId;

    Vertex pickUpVertex;

    Step stepBefore;

    Step stepAfter;

    protected void entryToState(long dropOffNodeId, Vertex pickUpVertex, Step stepBefore, Step step) {
        this.dropOffNodeId = dropOffNodeId;
        this.pickUpVertex = pickUpVertex;
        this.stepBefore = stepBefore;
        this.stepAfter = step;
    }

    @Override
    public void addDropOffNode(Controller controller, UIController uiController, CommandList commandList) {
        int dropOffDuration = uiController.getTimeFromPicker();
        Vertex dropOffVertex = new Vertex(dropOffNodeId, false, dropOffDuration);
        commandList.doCommand(new CommandAdd(pickUpVertex, dropOffVertex, stepBefore, stepAfter, controller.getRound(), controller.getCityMap()));
    }
}
