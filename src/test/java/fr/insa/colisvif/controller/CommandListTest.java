package fr.insa.colisvif.controller;

import fr.insa.colisvif.controller.command.*;
import org.junit.Test;

import static org.junit.Assert.*;

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

    @Test
    public void createCommandList() {
        CommandList cl = new CommandList();
        assertTrue(cl.getPastCommands().isEmpty());
        assertTrue(cl.getCurrentCommands().isEmpty());
    }

    @Test
    public void addCommand() {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();
        cl.addCommand(c);
        assertEquals(cl.getCurrentCommands().size(), 1);
        assertEquals(cl.getCurrentCommands().get(0), c);
    }

    @Test
    public void undoCommandEmpty() {
        CommandList cl = new CommandList();
        cl.undoCommand();
        assertEquals(cl.getCurrentCommands().size(), 0);
        assertEquals(cl.getPastCommands().size(), 0);
    }

    @Test
    public void undoCommand() {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();

        cl.addCommand(c);
        assertEquals(cl.getCurrentCommands().size(), 1);
        assertEquals(cl.getPastCommands().size(), 0);

        cl.undoCommand();

        assertEquals(cl.getCurrentCommands().size(), 0);
        assertEquals(cl.getPastCommands().size(), 1);


        assertEquals(cl.getPastCommands().get(0), c);
    }

    @Test
    public void redoCommandEmpty() {
        CommandList cl = new CommandList();
        cl.redoCommand();
        assertEquals(cl.getCurrentCommands().size(), 0);
        assertEquals(cl.getPastCommands().size(), 0);
    }

    @Test
    public void redoCommand() {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();

        cl.addCommand(c);
        cl.undoCommand();
        cl.redoCommand();

        assertEquals(cl.getCurrentCommands().size(), 1);
        assertEquals(cl.getPastCommands().size(), 0);

        assertEquals(cl.getCurrentCommands().get(0), c);
    }

    @Test
    public void resetCommand() {
        CommandList cl = new CommandList();
        Command c = new SimplePrintCommand();

        cl.addCommand(c);
        cl.undoCommand();
        cl.redoCommand();

        cl.resetCommand();
        assertEquals(cl.getCurrentCommands().size(), 0);
        assertEquals(cl.getPastCommands().size(), 0);
    }
}