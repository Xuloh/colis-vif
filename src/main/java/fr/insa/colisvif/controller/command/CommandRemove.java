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

    public CommandRemove(Step stepToRemove, Step otherDeliveryStep, Round round, CityMap cityMap) {
        this.round = round;
        this.cityMap = cityMap;
        this.stepToRemove = stepToRemove;
        this.otherDeliveryStep = otherDeliveryStep;
    }

    @Override
    public void undoCommand() {
        if (stepToRemove.isPickUp()) {
            round.addDelivery(stepToRemove.getArrivalNodeId(), otherDeliveryStep.getArrivalNodeId(),
                    stepToRemove.getArrivalDate(), otherDeliveryStep.getArrivalDate(), cityMap);
        } else {
            round.addDelivery(otherDeliveryStep.getArrivalNodeId(), stepToRemove.getArrivalNodeId(),
                    otherDeliveryStep.getArrivalDate(), stepToRemove.getArrivalDate(), cityMap);
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
