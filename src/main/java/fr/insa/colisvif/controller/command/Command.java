package fr.insa.colisvif.controller;

public interface Command {

    void undoCommand();

    void doCommand();
}
