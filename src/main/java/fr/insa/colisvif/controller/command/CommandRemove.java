package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandRemove implements Command {

    private static final Logger LOGGER =
            LogManager.getLogger(CommandRemove.class);

    private Round round;

    private CityMap cityMap;

    private Step stepToRemove;

    private Step otherDeliveryStep;

    private int indexPickUp;

    private int indexDropOff;

    private int oldDeliveryId;
    
    public CommandRemove(Step stepToRemove, Step otherDeliveryStep,
                         Round round, CityMap cityMap, int indexFirst,
                         int indexSecond, int deliveryId) {
        this.round = round;
        this.cityMap = cityMap;
        this.stepToRemove = stepToRemove;
        this.otherDeliveryStep = otherDeliveryStep;
        LOGGER.error("delivery id : " + deliveryId);
        this.oldDeliveryId = deliveryId;
        LOGGER.error("old delivery id : " + oldDeliveryId);
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
        LOGGER.debug(otherDeliveryStep.getArrivalDate() + " / "
                    + stepToRemove.getArrivalDate());
        if (stepToRemove.isPickUp()) {
            oldDelivery =
                    round.getDeliveryMap()
                         .createDelivery(stepToRemove.getArrivalNodeId(),
                                 otherDeliveryStep.getArrivalNodeId(),
                    stepToRemove.getDuration(),
                                 otherDeliveryStep.getDuration());
        } else {
            oldDelivery = round.getDeliveryMap().createDelivery(
                    otherDeliveryStep.getArrivalNodeId(),
                    stepToRemove.getArrivalNodeId(),
                    otherDeliveryStep.getDuration(),
                    stepToRemove.getDuration());
        }

        oldDelivery.setId(oldDeliveryId);
        stepToRemove.setDeliveryID(oldDeliveryId);
        otherDeliveryStep.setDeliveryID(oldDeliveryId);
        round.addStepInIthPlace(otherDeliveryStep, indexPickUp, cityMap);
        round.addStepInIthPlace(stepToRemove, indexDropOff, cityMap);
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
