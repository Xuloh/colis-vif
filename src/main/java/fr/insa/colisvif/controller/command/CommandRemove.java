package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.*;

public class CommandRemove implements Command {

    private Vertex pickUpVertex;

    private Vertex dropOffVertex;

    private Round round;

    public CommandRemove(Vertex pickUpVertex, Vertex dropOffVertex, Round round) {
        this.pickUpVertex = pickUpVertex;
        this.dropOffVertex = dropOffVertex;
        this.round = round;
    }

    @Override
    public void undoCommand() {
        /*round.addDelivery(pickUpVertex.getNodeId(), dropOffVertex.getNodeId(),
                          pickUpVertex.getDuration(), dropOffVertex.getDuration()); #MethodeFelix*/
    }

    @Override
    public void doCommand() {
        /*round.removeDelivery(signatureDeLaNouvelleMethode) #MethodeFelix*/
    }
}
