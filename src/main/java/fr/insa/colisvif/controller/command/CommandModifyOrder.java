package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Step;

public class CommandModifyOrder implements Command {

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
        if (modifiedIndex != 0) {
            round.changeOrderStep(modifiedStep, round.getSteps().get(modifiedIndex - 1), cityMap);
        } else {
            round.getSteps().remove(modifiedStep);
            round.addStepAtFirst(modifiedStep, cityMap);
        }
    }

    @Override
    public void doCommand() {
        modifiedIndex = round.getSteps().indexOf(modifiedStep);
        round.changeOrderStep(modifiedStep, previousStep, cityMap);
    }
}
