package fr.insa.colisvif.controller.state;

// todo: je pense que cet etat viendra a disparaitre vu qu'il y a eu du changement dans l'IHM
/**
 * Class that implements State interface.
 * This class represents the state when the properties of a stop are printed.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class PropertiesPrintedState implements State {

    /**
     * Enter in order change mode to enable new actions.
     *
     * @see ModifyOrderState
     */
    @Override
    public void switchToOrderChangeMode() {

    }

    /**
     * Enter in location change mode to enable new actions.
     *
     * @see ModifyStopLocationState}.
     */
    @Override
    public void switchToLocationChange() {

    }
}
