package fr.insa.colisvif.controller.command;

public class SimplePrintCommand implements Command {

    @Override
    public void undoCommand() {
        System.out.println("UNDO");
    }

    @Override
    public void doCommand() {
        System.out.println("DO");
    }
}
