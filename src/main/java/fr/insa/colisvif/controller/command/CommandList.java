package fr.insa.colisvif.controller.command;

import java.util.LinkedList;

/**
 * A class that handles {@link Command} instances. <br/>
 * It manages executing commands, undoing previously
 * executed command and redoing previously undone commands.
 */
public class CommandList {

    private LinkedList<Command> pastCommands;

    private LinkedList<Command> currentCommands;

    /**
     * Create a new {@link CommandList} containing no command.
     */
    public CommandList() {
        pastCommands = new LinkedList<>();
        currentCommands = new LinkedList<>();
    }

    /**
     * Adds the given {@link Command} to the current commands stack and execute it.
     * This will clear any previously undone command, making it impossible to redo them.
     * @param command The command to execute
     */
    public void doCommand(Command command) {
        pastCommands.clear();
        currentCommands.add(command);
        command.doCommand();
    }

    /**
     * Undo the last executed {@link Command}.
     * It can be redone by calling {@link #redoCommand()}.
     * If no {@link Command} was previously called, nothing happens.
     */
    public void undoCommand() {
        if (!currentCommands.isEmpty()) {
            Command commandToUndo = currentCommands.pop();
            commandToUndo.undoCommand();
            pastCommands.add(commandToUndo);
        }
    }

    /**
     * Executes the last undone {@link Command}.
     * It can be undone again by calling {@link #undoCommand()}.
     * If no {@link Command} was previously undone, nothing happens.
     */
    public void redoCommand() {
        if (!pastCommands.isEmpty()) {
            Command commandToRedo = pastCommands.pop();
            commandToRedo.doCommand();
            currentCommands.add(commandToRedo);
        }
    }

    /**
     * Clears the {@link Command} history.<br/>
     * <b>All executed and undone commands will be lost.</b>
     */
    public void resetCommand() {
        currentCommands.clear();
        pastCommands.clear();
    }
}
