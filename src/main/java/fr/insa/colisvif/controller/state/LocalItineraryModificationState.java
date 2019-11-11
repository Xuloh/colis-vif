package fr.insa.colisvif.controller.state;

/**
 * Class that implements State interface.
 * This class represents the state when modification happened, and the {@link fr.insa.colisvif.model.Round}
 * need to be calculated locally.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class LocalItineraryModificationState implements State {

    /**
     * When a modification appears, calculates locally the best new itinerary.
     */
    @Override
    public void calculateItineraryLocally() {

    }

}
