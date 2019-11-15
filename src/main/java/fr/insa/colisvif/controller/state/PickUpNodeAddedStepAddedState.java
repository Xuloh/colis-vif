package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;

public class PickUpNodeAddedStepAddedState implements State {

    private long pickUpNodeId;

    private Step stepBefore;

    protected void entryToState(long pickUpNodeId, Step stepBefore) {
        this.pickUpNodeId = pickUpNodeId;
        this.stepBefore = stepBefore;
    }

    @Override
    public void addPickUpNode(Controller controller, UIController uiController) {
    }
}
