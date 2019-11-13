package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandAdd implements Command {

    private static final Logger LOGGER = LogManager.getLogger(CommandRemove.class);

    private Vertex pickUpVertex;

    private Vertex dropOffVertex;

    private Step stepBefore;

    private Step stepAfter;

    private Round round;

    private CityMap cityMap;

    private Step pickUpStep;

    private Step dropOffStep;

    public CommandAdd(Vertex pickUpVertex, Vertex dropOffVertex, Step stepBefore, Step stepAfter, Round round, CityMap cityMap) {
        this.pickUpVertex = pickUpVertex;
        this.dropOffVertex = dropOffVertex;
        this.stepBefore = stepBefore;
        this.stepAfter = stepAfter;
        this.round = round;
        this.cityMap = cityMap;
    }

    @Override
    public void undoCommand() {
        try {
            round.removeDelivery(pickUpStep, cityMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void doCommand() {
        if (round.getSteps().indexOf(stepAfter) > round.getSteps().indexOf(stepBefore)) {
            throw new IllegalArgumentException("L'étape choisie après la livraison est antérieure à celle avant la récupération.");
        } else {
            int deliveryId = round.addDelivery(pickUpVertex.getNodeId(), dropOffVertex.getNodeId(),
                    pickUpVertex.getDuration(), dropOffVertex.getDuration(), cityMap);

            // Searching Steps created

            for (Step step : round.getSteps()) {
                if (step.getDeliveryID() == deliveryId) {
                    if (step.isPickUp()) {
                        pickUpStep = step;
                    } else {
                        dropOffStep = step;
                    }
                }
            }

            // Modifying Positions

            round.changeOrderStep(pickUpStep, stepBefore, cityMap);

            if (stepAfter == null) {
                round.changeOrderStep(dropOffStep, round.getSteps().get(round.getSteps().size() - 1), cityMap);
            } else {
                round.changeOrderStep(dropOffStep, stepAfter, cityMap);
            }


        }
    }
}
