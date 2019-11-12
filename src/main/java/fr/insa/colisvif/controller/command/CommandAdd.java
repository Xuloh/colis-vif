package fr.insa.colisvif.controller.command;

import fr.insa.colisvif.model.*;

public class CommandAdd implements Command {

    private Vertex pickUpVertex;

    private Vertex dropOffVertex;

    private Round round;

    private CityMap cityMap;

    public CommandAdd(Vertex pickUpVertex, Vertex dropOffVertex, Round round, CityMap cityMap) {
        this.pickUpVertex = pickUpVertex;
        this.dropOffVertex = dropOffVertex;
        this.round = round;
        this.cityMap = cityMap;
    }

    @Override
    public void undoCommand() {
        /*round.removeDelivery(signatureDeLaNouvelleMethode) #MethodeFelix*/
    }

    @Override
    public void doCommand() {
        round.addDelivery(pickUpVertex.getNodeId(), dropOffVertex.getNodeId(),
                          pickUpVertex.getDuration(), dropOffVertex.getDuration(), cityMap);
    }
}
