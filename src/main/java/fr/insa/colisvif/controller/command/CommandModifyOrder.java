package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Step;

public class CommandModifyOrder implements Command {

    private Round round;

    private Step modifiedStep;

    private Step previousStep;

    private int modifiedIndex;

    public CommandModifyOrder(Round round, Step modifiedStep, Step previousStep) {
        this.round = round;
        this.modifiedStep = modifiedStep;
        this.previousStep = previousStep;
    }

    @Override
    public void undoCommand() {
        round.getSteps().remove(modifiedStep);
        round.getSteps().add(modifiedIndex, modifiedStep);
    }

    @Override
    public void doCommand() {
        modifiedIndex = round.getSteps().indexOf(modifiedStep);
        round.getSteps().remove(modifiedStep);
        round.getSteps().add(round.getSteps().indexOf(previousStep) + 1, modifiedStep);
    }
}
