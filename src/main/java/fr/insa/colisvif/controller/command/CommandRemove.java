package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandRemove implements Command {

    private static final Logger LOGGER = LogManager.getLogger(CommandRemove.class);

    private Round round;

    private CityMap cityMap;

    private Step stepToRemove;

    private Step otherDeliveryStep;

    private int indexPickUp;

    private int indexDropOff;

    private int newDeliveryId;

    private int oldDeliveryId;

    private Round oldRound;

    public CommandRemove(Step stepToRemove, Step otherDeliveryStep, Round round, CityMap cityMap, int indexFirst, int indexSecond) {
        this.round = round;
        this.oldRound = round;
        this.cityMap = cityMap;
        this.stepToRemove = stepToRemove;
        this.otherDeliveryStep = otherDeliveryStep;

        if (stepToRemove.isPickUp()) {
            indexPickUp = indexFirst;
            indexDropOff = indexSecond;
        } else {
            indexPickUp = indexSecond;
            indexDropOff = indexFirst;
        }
    }

    @Override
    public void undoCommand() {
        Delivery oldDelivery;
        LOGGER.debug(otherDeliveryStep.getArrivalDate() + " / " + stepToRemove.getArrivalDate());
        if (stepToRemove.isPickUp()) {
            oldDelivery = round.addDelivery(stepToRemove.getArrivalNodeId(), otherDeliveryStep.getArrivalNodeId(),
                    stepToRemove.getDuration(), otherDeliveryStep.getDuration(), cityMap);
            newDeliveryId = oldDelivery.getId();
            LOGGER.debug("Pick Up : " + indexPickUp);
            LOGGER.debug("Drop Off : " + indexDropOff);
            for (Step step : round.getSteps()) {
                if (step.getDeliveryID() == newDeliveryId) {
                    if (step.isPickUp() == stepToRemove.isPickUp()) {
                        stepToRemove = step;
                    } else {
                        otherDeliveryStep = step;
                    }
                }
            }
            if (indexPickUp == 0) {
                round.changeOrderStep(stepToRemove, null, cityMap);
            } else if (indexPickUp > round.getSteps().size()) {
                round.changeOrderStep(stepToRemove, round.getSteps().get(round.getSteps().size() - 1), cityMap);
            } else {
                round.changeOrderStep(stepToRemove, round.getSteps().get(indexPickUp - 1), cityMap);
            }

            if (indexDropOff >= round.getSteps().size()) {
                round.changeOrderStep(otherDeliveryStep, round.getSteps().get(round.getSteps().size() - 1), cityMap);
            } else {
                round.changeOrderStep(otherDeliveryStep, round.getSteps().get(indexDropOff - 1), cityMap);
            }

        } else {
            oldDelivery = round.addDelivery(otherDeliveryStep.getArrivalNodeId(), stepToRemove.getArrivalNodeId(),
                    otherDeliveryStep.getDuration(), stepToRemove.getDuration(), cityMap);
            newDeliveryId = oldDelivery.getId();
            LOGGER.debug("Pick Up : " + indexPickUp);
            LOGGER.debug("Drop Off : " + indexDropOff);
            if (indexPickUp == 0) {
                round.changeOrderStep(otherDeliveryStep, null, cityMap);
            } else if (indexPickUp > round.getSteps().size()) {
                round.changeOrderStep(otherDeliveryStep, round.getSteps().get(round.getSteps().size() - 1), cityMap);
            } else {
                round.changeOrderStep(otherDeliveryStep, round.getSteps().get(indexPickUp - 1), cityMap);
            }

            if (indexDropOff >= round.getSteps().size()) {
                round.changeOrderStep(stepToRemove, round.getSteps().get(round.getSteps().size() - 1), cityMap);
            } else {
                round.changeOrderStep(stepToRemove, round.getSteps().get(indexDropOff - 1), cityMap);
            }
        }
        for (Step step : round.getSteps()) {
            if (step.getDeliveryID() == newDeliveryId) {
                stepToRemove = step;
                break;
            }
        }
    }

    @Override
    public void doCommand() {
        try {
            round.removeDelivery(stepToRemove, cityMap);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
