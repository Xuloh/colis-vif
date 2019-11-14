package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;

public class DropOffNodeAddedStepAdded implements State {

    long dropOffNodeId;

    Vertex pickUpVertex;

    Step step;

    protected void entryToState(long dropOffNodeId, Vertex pickUpVertex, Step step) {
        this.dropOffNodeId = dropOffNodeId;
        this.pickUpVertex = pickUpVertex;
        this.step = step;
    }


}
