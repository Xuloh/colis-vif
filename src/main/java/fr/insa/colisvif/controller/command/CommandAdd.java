package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandAdd implements Command {

    private static final Logger LOGGER = LogManager.getLogger(CommandRemove.class);

    private Vertex pickUpVertex;

    private Vertex dropOffVertex;

    private Round round;

    private CityMap cityMap;

    private int deliveryId;

    public CommandAdd(Vertex pickUpVertex, Vertex dropOffVertex, Round round, CityMap cityMap) {
        this.pickUpVertex = pickUpVertex;
        this.dropOffVertex = dropOffVertex;
        this.round = round;
        this.cityMap = cityMap;
    }

    @Override
    public void undoCommand() {
        for (Step step : round.getSteps()) {
            if (step.getDeliveryID() == deliveryId) {
                Step toRemove = step;
                try {
                    round.removeDelivery(step, cityMap);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void doCommand() {
        int deliveryId = round.addDelivery(pickUpVertex.getNodeId(), dropOffVertex.getNodeId(),
                          pickUpVertex.getDuration(), dropOffVertex.getDuration(), cityMap);
        this.deliveryId = deliveryId;
    }
}
