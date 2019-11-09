package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

public class DropOffNodeAdding implements State {

    @Override
    public void addDropOffNode() {

    }

    @Override
    public void getBackToPreviousState(Controller c) {
        c.setCurrentState(ItineraryCalculatedState.class);
    }

}
