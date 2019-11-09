package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

public class ModifyOrderState implements State {

    @Override
    public void selectStopToPass() {

    }

    @Override
    public void getBackToPreviousState(Controller c) {
        c.setCurrentState(PropertiesPrintedState.class);
    }
}
