package fr.insa.colisvif.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

/**
 * A class that handles {@link Command} instances. <br/>
 * It manages executing commands, undoing previously
 * executed command and redoing previously undone commands.
 */
public class CommandList {

    private static final Logger LOGGER = LogManager.getLogger(CommandList.class);

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
        try {
            LOGGER.info("Performing Command : {}", command.getClass().getSimpleName());
            pastCommands.clear();
            currentCommands.add(command);
            command.doCommand();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Undo the last executed {@link Command}.
     * It can be redone by calling {@link #redoCommand()}.
     * If no {@link Command} was previously called, nothing happens.
     */
    public void undoCommand() {
        if (!currentCommands.isEmpty()) {
            Command commandToUndo = currentCommands.pop();
            LOGGER.info("Undoing Command : {}", commandToUndo.getClass().getSimpleName());
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
        try {
            if (!pastCommands.isEmpty()) {
                Command commandToRedo = pastCommands.pop();
                LOGGER.info("Redoing Command : {}", commandToRedo.getClass().getSimpleName());
                commandToRedo.doCommand();
                currentCommands.add(commandToRedo);
            } else {
                LOGGER.info("No Command to Redo");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Clears the {@link Command} history.<br/>
     * <b>All executed and undone commands will be lost.</b>
     */
    public void resetCommand() {
        LOGGER.info("Clearing command history");
        currentCommands.clear();
        pastCommands.clear();
    }
}
