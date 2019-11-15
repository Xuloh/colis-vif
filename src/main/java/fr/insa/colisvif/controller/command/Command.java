package fr.insa.colisvif.controller.command;

/**
 * Represents an command that can perform some actions
 * and can be undone to cancel the actions it performed
 */
public interface Command {

    /**
     * Undo the {@link Command}.
     * This should cancel any action performed
     * by {@link #doCommand()}
     *
     * @see #undoCommand()
     */
    void undoCommand();

    /**
     * Perform the {@link Command}.
     * The {@link Command} should only perform actions
     * that can be canceled by calling {@link #undoCommand()}.
     *
     * @throws Exception exception that can be thrown through some
     * {@link fr.insa.colisvif.model.Round} method
     *
     * @see #doCommand()
     */
    void doCommand() throws Exception;
}
