package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.controller.command.Command;

import java.util.LinkedList;

public class CommandList {

    private LinkedList<Command> pastCommands;

    private LinkedList<Command> currentCommands;

    public CommandList() {
        pastCommands = new LinkedList<>();
        currentCommands = new LinkedList<>();
    }

    public void addCommand(Command command) {
        pastCommands.clear();
        currentCommands.add(command);
        command.doCommand();
    }

    public void undoCommand() {
        if (!currentCommands.isEmpty()) {
            Command commandToUndo = currentCommands.pop();
            commandToUndo.undoCommand();
            pastCommands.add(commandToUndo);
        }
    }

    public void redoCommand() {
        if (!pastCommands.isEmpty()) {
            Command commandToRedo = pastCommands.pop();
            commandToRedo.doCommand();
            currentCommands.add(commandToRedo);
        }
    }

    public void resetCommand() {
        currentCommands.clear();
        pastCommands.clear();
    }
}
