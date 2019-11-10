package fr.insa.colisvif.controller;

import fr.insa.colisvif.controller.command.Command;
import fr.insa.colisvif.controller.command.CommandList;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandListTest {

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

    public static LinkedList<Command> getCurrentCommands(CommandList commandList) throws NoSuchFieldException, IllegalAccessException {
        Class<CommandList> clazz = CommandList.class;
        Field currentCommandsField = clazz.getDeclaredField("currentCommands");
        currentCommandsField.setAccessible(true);
        return (LinkedList<Command>) currentCommandsField.get(commandList);
    }

    public static LinkedList<Command> getPastCommands(CommandList commandList) throws NoSuchFieldException, IllegalAccessException {
        Class<CommandList> clazz = CommandList.class;
        Field pastCommandsField = clazz.getDeclaredField("pastCommands");
        pastCommandsField.setAccessible(true);
        return (LinkedList<Command>) pastCommandsField.get(commandList);
    }

    @Test
    public void createCommandList() throws NoSuchFieldException, IllegalAccessException {
        CommandList cl = new CommandList();

        assertTrue(getCurrentCommands(cl).isEmpty());
        assertTrue(getPastCommands(cl).isEmpty());
    }

    @Test
    public void addCommand() throws NoSuchFieldException, IllegalAccessException {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();
        cl.doCommand(c);

        assertEquals(getCurrentCommands(cl).size(), 1);
        assertEquals(getCurrentCommands(cl).get(0), c);
    }

    @Test
    public void undoCommandEmpty() throws NoSuchFieldException, IllegalAccessException {
        CommandList cl = new CommandList();
        cl.undoCommand();

        assertEquals(getCurrentCommands(cl).size(), 0);
        assertEquals(getPastCommands(cl).size(), 0);
    }

    @Test
    public void undoCommand() throws NoSuchFieldException, IllegalAccessException {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();

        cl.doCommand(c);

        assertEquals(getCurrentCommands(cl).size(), 1);
        assertEquals(getPastCommands(cl).size(), 0);

        cl.undoCommand();

        assertEquals(getCurrentCommands(cl).size(), 0);
        assertEquals(getPastCommands(cl).size(), 1);
        assertEquals(getPastCommands(cl).get(0), c);
    }

    @Test
    public void redoCommandEmpty() throws NoSuchFieldException, IllegalAccessException {
        CommandList cl = new CommandList();
        cl.redoCommand();

        assertEquals(getCurrentCommands(cl).size(), 0);
        assertEquals(getPastCommands(cl).size(), 0);
    }

    @Test
    public void redoCommand() throws NoSuchFieldException, IllegalAccessException {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();

        cl.doCommand(c);
        cl.undoCommand();
        cl.redoCommand();

        assertEquals(getCurrentCommands(cl).size(), 1);
        assertEquals(getPastCommands(cl).size(), 0);
        assertEquals(getCurrentCommands(cl).get(0), c);
    }

    @Test
    public void resetCommand() throws NoSuchFieldException, IllegalAccessException {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();

        cl.doCommand(c);
        cl.undoCommand();
        cl.redoCommand();
        cl.resetCommand();

        assertEquals(getCurrentCommands(cl).size(), 0);
        assertEquals(getPastCommands(cl).size(), 0);
    }
}
