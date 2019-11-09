package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

public class SuppressionModeState implements State {

    @Override
    public void selectStopToDelete() {

    }

    @Override
    public void getBackToPreviousState(Controller c) {
        c.setCurrentState(ItineraryCalculatedState.class);
    }
}
