package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

public class ModifyStopLocationState implements State {

    @Override
    public void changeNodeLocation() {

    }

    @Override
    public void getBackToPreviousState(Controller c) {
        c.setCurrentState(PropertiesPrintedState.class);
    }
}
