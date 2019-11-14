package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandModifyOrder implements Command {

    private static final Logger LOGGER = LogManager.getLogger(CommandRemove.class);

    private Round round;

    private Step modifiedStep;

    private Step previousStep;

    private int modifiedIndex;

    private CityMap cityMap;

    public CommandModifyOrder(Round round, Step modifiedStep, Step previousStep, CityMap cityMap) {
        this.round = round;
        this.modifiedStep = modifiedStep;
        this.previousStep = previousStep;
        this.cityMap = cityMap;
    }

    @Override
    public void undoCommand() {
        LOGGER.debug(modifiedIndex + " " + round.getSteps().indexOf(modifiedStep));
        if (modifiedIndex != 0) {
            round.changeOrderStep(modifiedStep, round.getSteps().get(modifiedIndex), cityMap);
        } else {
            round.changeOrderStep(modifiedStep, null, cityMap);
        }
    }

    @Override
    public void doCommand() {
        modifiedIndex = round.getSteps().indexOf(modifiedStep);
        round.changeOrderStep(modifiedStep, previousStep, cityMap);
    }
}
