package fr.insa.colisvif.controller.state;

//todo: Je ne pense pas qu'on gardera cet etat !!
/**
 * Class that implements State interface.
 * This class represents the state when the {@link fr.insa.colisvif.model.Round} is calculated but not optimized.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class NonOptimizedItineraryState implements State {

    /**
     * Continue the calculation of the best itinerary.
     */
    @Override
    public void continueCalculation() {

    }

    /**
     * Stop the calculation of the best itinerary.
     */
    @Override
    public void stopCalculation() {

    }
}
