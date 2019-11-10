package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;

/**
 * Class that implements State interface.
 * This class represents the state when, in add mode, the pick up node is already chosen.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class DropOffNodeAdding implements State {

    @Override
    public void addDropOffNode() {

    }

    @Override
    public void getBackToPreviousState(Controller c) {
        c.setCurrentState(ItineraryCalculatedState.class);
    }

}
