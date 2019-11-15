package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Step;

public class CommandModifyLocation implements Command {

    private Step modifiedStep;

    private long oldNodeId;

    private long newNodeId;

    private Round round;

    private CityMap cityMap;

    public CommandModifyLocation(Step modifiedStep, long newNodeId, Round round,
                                 CityMap cityMap) {
        this.modifiedStep = modifiedStep;
        this.newNodeId = newNodeId;
        this.round = round;
        this.cityMap = cityMap;
    }

    /**
     * Undoes the last location modification thanks to the stored old position
     * of the vertex
     */
    @Override
    public void undoCommand() {
        round.changeLocationStep(modifiedStep, oldNodeId, cityMap);
    }

    /**
     * Changes the location of a {@link fr.insa.colisvif.model.Step} by
     * changing the location of its {@link fr.insa.colisvif.model.Vertex} to
     * another {@link fr.insa.colisvif.model.Node}
     */
    @Override
    public void doCommand() {
        oldNodeId = modifiedStep.getArrivalNodeId();
        round.changeLocationStep(modifiedStep, newNodeId, cityMap);
    }
}
