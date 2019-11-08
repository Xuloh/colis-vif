package fr.insa.colisvif.controller;

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
