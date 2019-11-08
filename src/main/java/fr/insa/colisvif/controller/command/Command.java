package fr.insa.colisvif.controller.command;

public interface Command {

    void undoCommand();

    void doCommand();
}
