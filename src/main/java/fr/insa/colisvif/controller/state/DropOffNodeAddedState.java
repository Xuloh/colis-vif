package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandAdd;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import fr.insa.colisvif.view.UIController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DropOffNodeAddedState implements State {

    private static final Logger LOGGER = LogManager.getLogger(ItineraryCalculatedState.class);

    private Vertex pickUpVertex;

    private Step stepBefore;

    private long dropOffNodeId;

    protected void entryToState(Vertex pickUpVertex, Step stepBefore, long nodeId) {
        this.pickUpVertex = pickUpVertex;
        this.stepBefore = stepBefore;
        this.dropOffNodeId = nodeId;
    }

    @Override
    public void leftClick(Controller controller, UIController uiController, CommandList commandList, long nodeId, Vertex vertex) {
        Step stepOfVertex = null;
        LOGGER.debug("Vertex id : " + vertex.getDeliveryId());
        for (Step step : controller.getRound().getSteps()) {
            LOGGER.debug("Step id : " + step.getDeliveryID());
            if (step.getDeliveryID() == vertex.getDeliveryId() && step.isPickUp() == vertex.getType()) {
                LOGGER.debug("On passe bien là");
                stepOfVertex = step;
            }
        }
        LOGGER.debug("ID : " + stepOfVertex.getDeliveryID());
        LOGGER.debug("ID SB : " + stepBefore.getDeliveryID());
        int dropOffDuration = uiController.getTimeFromPicker();
        Vertex dropOffVertex = new Vertex(dropOffNodeId, false, dropOffDuration);
        uiController.clearTimePicker();
        commandList.doCommand(new CommandAdd(pickUpVertex, dropOffVertex, stepBefore, stepOfVertex, controller.getRound(), controller.getCityMap()));
        controller.createVertexList();
        uiController.updateDeliveryMap();
        uiController.updateRound();
        uiController.getMapCanvas().redraw();
        controller.setButtons();
        controller.setCurrentState(ItineraryCalculatedState.class);
    }
}
