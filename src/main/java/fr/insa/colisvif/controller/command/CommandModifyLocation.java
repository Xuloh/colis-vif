package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.Node;

public class CommandModifyLocation implements Command {

    private double oldLatitude;

    private double oldLongitude;

    private double newLatitude;

    private double newLongitude;

    private Node nodeModified;

    public CommandModifyLocation(Node nodeModified, double newLatitude, double newLongitude) {
        this.nodeModified = nodeModified;
        this.oldLatitude = nodeModified.getLatitude();
        this.oldLongitude = nodeModified.getLongitude();
        this.newLatitude = newLatitude;
        this.newLongitude = newLongitude;
    }

    @Override
    public void undoCommand() {
        this.nodeModified.setLatitude(oldLatitude);
        this.nodeModified.setLongitude(newLongitude);
    }

    @Override
    public void doCommand() {
        this.nodeModified.setLatitude(newLatitude);
        this.nodeModified.setLongitude(newLongitude);
    }
}
