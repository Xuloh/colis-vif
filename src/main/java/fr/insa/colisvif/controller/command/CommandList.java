package fr.insa.colisvif.controller.command;

import java.util.LinkedList;

/**
 * This class helps in the implementations of the Command Pattern.
 * <br><br>
 * Contains two stacks in the form of two linked lists. One of them, <b>currentCommands</b>
 * represents the current done commands. The other, <b>pastCommands</b> contains the previously
 * undone commands
 */
public class CommandList {

    private LinkedList<Command> pastCommands;

    private LinkedList<Command> currentCommands;

    /**
     * Constructs a new <b>CommandList</b> object with both stacks being empty
     */
    public CommandList() {
        pastCommands = new LinkedList<>();
        currentCommands = new LinkedList<>();
    }

    /**
     * Add command to the current commands stack and execute it. Will clear the undone commands
     * stack at the same time
     * @param command A command you want to execute
     */
    public void addCommand(Command command) {

        pastCommands.clear();
        currentCommands.add(command);
        command.doCommand();
    }

    /**
     * Undo the command at the top of the current commands stack and will put it on top of the
     * undone commands stack
     */
    public void undoCommand() {
        if (!currentCommands.isEmpty()) {
            Command commandToUndo = currentCommands.pop();
            commandToUndo.undoCommand();
            pastCommands.add(commandToUndo);
        }
    }

    /**
     * Executes the command at the top of the undone commands stack and puts in on top of the current
     * commands stack
     */
    public void redoCommand() {
        if (!pastCommands.isEmpty()) {
            Command commandToRedo = pastCommands.pop();
            commandToRedo.doCommand();
            currentCommands.add(commandToRedo);
        }
    }

    /**
     * Clears both stacks of command. They will be lost !
     */
    public void resetCommand() {
        currentCommands.clear();
        pastCommands.clear();
    }

    /**
     *
     * @return the undone command stack
     */
    public LinkedList<Command> getPastCommands() {
        return pastCommands;
    }

    /**
     *
     * @return the current commands stack
     */
    public LinkedList<Command> getCurrentCommands() {
        return currentCommands;
    }
}
