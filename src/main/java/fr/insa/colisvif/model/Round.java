package fr.insa.colisvif.model;

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
        return this.deliveryMap.getStartDate();
    }

    /**
     * Returns the {@link List} of {@link Step}.
     *
     * @return the {@link List} of {@link Step}.
     */
    public List<Step> getSteps() {
        return steps;
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

    private void removeFirstStep(CityMap map) {
        long node1 = deliveryMap.getWarehouseNodeId();
        long node2 = steps.get(1).getArrivalNodeId();
        steps.get(1).setSections(map.getPath(node1, node2));

        double length = map.getLength(node1, node2);
        int newArrivalDate = deliveryMap.getStartDate() + (int) (length / ModelConstants.CYCLIST_SPEED);
        int deltaTime = steps.get(1).getArrivalDate() - newArrivalDate;
        for (int i = 1; i < steps.size(); ++i) {
            steps.get(i).setArrivalDate(steps.get(i).getArrivalDate() - deltaTime);
        }
        steps.remove(0);
    }

    private void removeIthStep(int i, CityMap map) throws IllegalArgumentException {
        if (i < 0 || i >= steps.size()) {
            throw new IllegalArgumentException("Index " + i + " out of bounds (size : " + steps.size() + ")");
        }
        if (i == 0) {
            removeFirstStep(map);
        } else if (i == steps.size() - 1) {
            steps.remove(i);
        } else {
            long node1 = steps.get(i - 1).getArrivalNodeId();
            long node2 = steps.get(i + 1).getArrivalNodeId();
            steps.get(i + 1).setSections(map.getPath(node1, node2));

            double length = map.getLength(node1, node2);
            int newArrivalDate = steps.get(i - 1).getArrivalDate() + (int) (length / ModelConstants.CYCLIST_SPEED);
            int deltaTime = steps.get(i + 1).getArrivalDate() - newArrivalDate;
            for (int j = i + 1; j < steps.size(); ++j) {
                steps.get(j).setArrivalDate(steps.get(j).getArrivalDate() - deltaTime);
            }
            steps.remove(i);
        }
    }

    private void addStepAtFirst(Step step, CityMap map) {
        long node1 = deliveryMap.getWarehouseNodeId();
        long node2 = step.getArrivalNodeId();
        long node3 = steps.get(0).getArrivalNodeId();
        step.setSections(map.getPath(node1, node2));
        steps.get(0).setSections(map.getPath(node2, node3));

        double length = map.getLength(node1, node2);
        step.setArrivalDate(deliveryMap.getStartDate() + (int) (length / ModelConstants.CYCLIST_SPEED));
        length = map.getLength(node2, node3);
        int newArrivalDate = step.getArrivalDate() + step.getDuration() + (int) (length / ModelConstants.CYCLIST_SPEED);
        int deltaTime = steps.get(0).getArrivalDate() - newArrivalDate;
        for (Step value : steps) {
            value.setArrivalDate(value.getArrivalDate() - deltaTime);
        }
        steps.add(0, step);
    }

    private void addStepAtLast(Step step, CityMap map) {
        Step lastStep = steps.get(steps.size() - 1);
        long node1 = lastStep.getArrivalNodeId();
        long node2 = step.getArrivalNodeId();
        step.setSections(map.getPath(node1, node2));
        double length = map.getLength(node1, node2);
        int time = lastStep.getArrivalDate() + lastStep.getDuration();
        step.setDuration(time + (int) (length / ModelConstants.CYCLIST_SPEED));
        steps.add(step);
    }

    private void addStepInIthPlace(Step step, int i, CityMap map) {
        if (i < 0 || i >= steps.size()) {
            throw new IllegalArgumentException("Index " + i + " out of bounds (size : " + steps.size() + ")");
        }
        if (i == 0) {
            addStepAtFirst(step, map);
        } else if (i == steps.size() - 1) {
            addStepAtLast(step, map);
        } else {
            long node1 = steps.get(i - 1).getArrivalNodeId();
            long node2 = step.getArrivalNodeId();
            long node3 = steps.get(i).getArrivalNodeId();
            step.setSections(map.getPath(node1, node2));
            steps.get(i).setSections(map.getPath(node2, node3));

            int time = steps.get(i - 1).getArrivalDate() + steps.get(i - 1).getDuration();
            double length = map.getLength(node1, node2) + map.getLength(node2, node3);
            time += (int) (length / ModelConstants.CYCLIST_SPEED);
            int deltaTime = time - steps.get(i).getArrivalDate();
            for (int j = i; j < steps.size(); ++j) {
                steps.get(j).setArrivalDate(steps.get(j).getArrivalDate() + deltaTime);
            }
            steps.add(i, step);
        }
    }

    public void removeDelivery(Step step, CityMap map) throws Exception {
        if (!steps.contains(step)) {
            throw new IllegalArgumentException("The step to remove does not belong to this round");
        }
        removeIthStep(steps.indexOf(step), map);
        removeIthStep(associatedStepIndex(step), map);
        deliveryMap.removeDeliveryById(step.getDeliveryID());
    }

    /**
     * Add a new delivery at the end of the round. Add two steps, one for the pickup and one for the delivery.
     *
     * @param pickUpNode  The pick up node
     * @param dropOffNode The drop off node
     */
    public void addDelivery(long pickUpNode, long dropOffNode, int pickUpDuration, int dropOffDuration, CityMap map) {
        map.dijkstra(pickUpNode);
        map.dijkstra(dropOffNode);
        int deliveryId = deliveryMap.createDelivery(pickUpNode, dropOffNode, pickUpDuration, dropOffDuration).getId();
        int time = steps.get(steps.size() - 1).getArrivalDate() + steps.get(steps.size() - 1).getDuration();

        double lengthToPickUp = map.getLength(steps.get(steps.size() - 1).getArrivalNodeId(), pickUpNode);
        time += (int) (lengthToPickUp / ModelConstants.CYCLIST_SPEED);
        Vertex pickUpVertex = new Vertex(pickUpNode, Vertex.PICK_UP, pickUpDuration);
        Step pickUpStep = new Step(pickUpVertex, deliveryId, time);
        pickUpStep.setSections(map.getPath(steps.get(steps.size() - 1).getArrivalNodeId(), pickUpNode));
        time += pickUpStep.getDuration();

        double lengthToDropOff = map.getLength(pickUpNode, dropOffNode);
        time += (int) (lengthToDropOff / ModelConstants.CYCLIST_SPEED);
        Vertex dropOffVertex = new Vertex(dropOffNode, Vertex.DROP_OFF, dropOffDuration);
        Step dropOffStep = new Step(dropOffVertex, deliveryId, time);
        dropOffStep.setSections(map.getPath(pickUpNode, dropOffNode));
        addStep(pickUpStep);
        addStep(dropOffStep);
    }

    /**
     * Change the order of stepChangeOrder to be just before stepJustAfter.
     *
     * @param stepToChange   the step that the order will be changed.
     * @param stepJustBefore the step that will follow stepChangeOrder,
     *                       null if we want to place stepChageOrder at the beginning of the round.
     */
    public void changeOrderStep(Step stepToChange, Step stepJustBefore, CityMap map) {
        int i = steps.indexOf(stepToChange);
        removeIthStep(i, map);
        if (stepJustBefore == null) {
            addStepAtFirst(stepToChange, map);
        } else {
            i = steps.indexOf(stepJustBefore);
            addStepInIthPlace(stepToChange, i + 1, map);
        }
    }

    public void changeLocationStep(Step stepToChange, long nodeId, CityMap map) {
        int index = steps.indexOf(stepToChange);
        removeIthStep(index, map);
        stepToChange.setArrivalNodeId(nodeId);
        addStepInIthPlace(stepToChange, index, map);
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
