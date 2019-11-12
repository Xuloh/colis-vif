package fr.insa.colisvif.model;

import javax.management.ImmutableDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the round to do all the deliveries that needs to be made, as a {@link List} of {@link Step}.
 */
public class Round {
    private List<Step> steps;

    private DeliveryMap deliveryMap;

    /**
     * Constructor of Round.
     *
     * @param deliveries the deliveries that contains the {@link List} of {@link Delivery}.
     */
    public Round(DeliveryMap deliveries) {
        steps = new ArrayList<>();
        this.deliveryMap = deliveries;
        int n = deliveries.getSize();
    }

    /**
     * Returns the warehouse {@link Node} id of the {@link DeliveryMap}.
     *
     * @return the warehouse {@link Node} id of the {@link DeliveryMap}.
     */
    public long getWarehouseNodeId() {
        return this.deliveryMap.getWarehouseNodeId();
    }

    /**
     * Returns the start date of the {@link DeliveryMap}.
     *
     * @return the start date of the {@link DeliveryMap}.
     */
    public int getStartDate() {
        return this.deliveryMap.getStartDateInSeconds();
    }

    /**
     * Returns the {@link List} of {@link Step}.
     *
     * @return the {@link List} of {@link Step}.
     */
    public List<Step> getSteps() {
        return steps;
    }

    private void optimize(){

    }

    private boolean areAssociated(Step step1, Step step2) {
        return step1 != step2 && step1.getDeliveryID() == step2.getDeliveryID();
    }

    private int associatedStepIndex(Step step) throws Exception {
        for (int i = 0; i < steps.size(); ++i) {
            if (areAssociated(step, steps.get(i))) {
                return i;
            }
        }
        throw new Exception("Associated step not found");
    }

    private void removeIthStep(int i, CityMap map) throws IllegalArgumentException {
        if (i == 0) {
            throw new IllegalArgumentException("Cannot remove warehouse");
        }
        if (i < 0 || i >= steps.size()) {
            throw new IllegalArgumentException("Index " + i + " out of bounds (size : " + steps.size() + ")");
        }

        if (i != steps.size() - 1) {
            long prevNode = steps.get(i - 1).getArrivalNodeId();
            long nextNode = steps.get(i + 1).getArrivalNodeId();
            steps.get(i + 1).setSections(map.getPath(prevNode, nextNode));
        }

        double length = map.getLength(steps.get(i - 1).getArrivalNodeId(), steps.get(i + 1).getArrivalNodeId());
        int arrivalDate = steps.get(i - 1).getArrivalDate();
        int duration = steps.get(i - 1).getDuration();
        int newArrivalDate = arrivalDate + duration+ (int)(length / ModelConstants.CYCLIST_SPEED);
        int deltaTime = steps.get(i + 1).getArrivalDate() - newArrivalDate;

        for(int j = i + 1; j < steps.size(); ++j){
            arrivalDate = steps.get(j).getArrivalDate();
            steps.get(j).setArrivalDate(arrivalDate - deltaTime);
        }
        steps.remove(i);
    }

    public void removeDelivery(Step step, CityMap map) throws Exception {
        if (!steps.contains(step)) {
            throw new IllegalArgumentException("The step to remove does not belong to this round");
        }
        removeIthStep(steps.indexOf(step), map);
        removeIthStep(associatedStepIndex(step), map);
        optimize();
    }

    /**
     * Add a new delivery at the end of the round. Add two steps, one for the pickup and one for the delivery.
     *
     * @param pickUpNode The pick up node
     * @param dropOffNode The drop off node
     */
    public void addDelivery(long pickUpNode, long dropOffNode, CityMap map) {
        map.dijkstra(pickUpNode);
        map.dijkstra(dropOffNode);
    }

    /**
     * Change the order of stepChangeOrder to be just before stepJustAfter.
     *
     * @param stepChangeOrder the step that the order will be changed.
     * @param stepJustAfter   the step that will follow stepChangeOrder.
     */
    public void changeOrderStep(Step stepChangeOrder, Step stepJustAfter) {
        if (!(steps.contains(stepChangeOrder) && steps.contains(stepJustAfter))) { // The steps are in the list steps
            throw new IllegalArgumentException();
        }

        int start = 0;
        int end = 0;

        if (stepChangeOrder.isPickUp()) {           // stepChangeOrder is a pickup, check that the delivery is not before
            end = steps.indexOf(stepJustAfter);
        } else {                                    // stepChangeOrder is a delivery, check that the pickup is not after
            start = steps.indexOf(stepJustAfter);
            end = steps.size();
        }

        for (int i = start; i < end; i++) {
            if (steps.get(i).getDeliveryID() == stepChangeOrder.getDeliveryID()) {
                throw new IllegalArgumentException();    // One of the previous assessment is false
            }
        }

        steps.remove(stepChangeOrder);
        steps.add(steps.indexOf(stepJustAfter), stepChangeOrder);

        // /!\ CHANGE THE LIST OF STEPS FOR stepJustAfter

        // Change all hours
        for (int i = steps.indexOf(stepChangeOrder); i < steps.size(); i++) {

        }
    }

    /**
     * Add a {@link Step} to the {@link List} of {@link Step}.
     *
     * @param step that needs to be added.
     */
    public void addStep(Step step) {
        steps.add(step);
    }
}
