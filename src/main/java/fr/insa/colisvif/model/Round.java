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
     * @param deliveries the deliveries that contains the {@link List} of {@link Delivery}.
     */
    public Round(DeliveryMap deliveries) {
        steps = new ArrayList<>();
        this.deliveryMap = deliveries;
    }

    /**
     * Returns the warehouse {@link Node} id of the {@link DeliveryMap}.
     * @return the warehouse {@link Node} id of the {@link DeliveryMap}.
     */
    public long getWarehouseNodeId() {
        return this.deliveryMap.getWarehouseNodeId();
    }

    /**
     * Returns the start date of the {@link DeliveryMap}.
     * @return the start date of the {@link DeliveryMap}.
     */
    public int getStartDate() {
        return this.deliveryMap.getStartDateInSeconds();
    }

    /**
     * Returns the {@link List} of {@link Step}.
     * @return the {@link List} of {@link Step}.
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Remove the delivery composed by 2 steps from the list of steps.
     *
     * @param stepPickup the pickup from the delivery.
     * @param stepDelivery the dropoff from the delivery.
     */
    public void removeDelivery(Step stepPickup, Step stepDelivery) {            // Make callable with Delivery or one step
        if (!(steps.contains(stepPickup) && steps.contains(stepDelivery))) {    // The steps are in the list steps
            throw new IllegalArgumentException();
        }
        if (stepPickup.getDeliveryID() != stepDelivery.getDeliveryID()) {       // The steps correspond to the same delivery
            throw new IllegalArgumentException();
        }

        // Change the list section of the next step
        Step stepAfterPickup = steps.get(steps.indexOf(stepPickup) + 1);
        Step stepAfterDelivery = null;

        if (steps.indexOf(stepDelivery) != steps.size() - 1) {
            stepAfterDelivery = steps.get(steps.indexOf(stepDelivery) + 1);
        }

        // Remove the steps
        steps.remove(stepPickup);
        steps.remove(stepDelivery);

        // Change all hours
        for (int i = steps.indexOf(stepAfterPickup); i < steps.size(); i++) {

        }
    }

    /**
     * Add a new delivery to the round. Add two steps, one for the pickup and one for the delivery.
     *
     * @param stepPickup step to go from the previous location to the pickup location.
     * @param stepDelivery step to go from the previous location to the delivery pickup.
     */
    public void addDelivery(Step stepPickup, Step stepDelivery) {
        // /!\ calculer le chemin !!!!!!!!!!
        //
        if (stepPickup.getDeliveryID() != stepDelivery.getDeliveryID()) {   // The steps are in the list steps
            throw new IllegalArgumentException();
        }

        /*
            MANQUE LE CALCUL DE TEMPS
            MODIFIER LES LISTES DES SECTIONS EMPRUNTÉES
         */
        steps.add(stepPickup);
        steps.add(stepDelivery);

        // Change all hours
        for (int i = steps.indexOf(stepPickup); i < steps.size(); i++) {

        }
    }

    /**
     * Change the order of stepChangeOrder to be just before stepJustAfter.
     *
     * @param stepChangeOrder the step that the order will be changed.
     * @param stepJustAfter the step that will follow stepChangeOrder.
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
