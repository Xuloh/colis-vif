package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

public class PickUpNodeAddingState implements State {

    @Override
    public void addPickUpNode() {

    }

    @Override
    public void getBackToPreviousState(Controller c) {
        c.setCurrentState(ItineraryCalculatedState.class);
    }

}
