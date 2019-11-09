package fr.insa.colisvif.model;

import java.util.LinkedList;
import java.util.Objects;

public class Step {
    private LinkedList<Section> sections;
    private int deliveryID;
    private boolean type; //0 if it is a pick up and 1 if it is a drop off
    private int arrivalDate; //the date when the delivery man will get to delivery point
    private int duration; //the duration of the pick up or the drop off, NOT THE TRAVEL TIME

    public LinkedList<Section> getSections() { return sections; }
    public int getArrivalDate() { return arrivalDate; }
    public void setArrivalDate(int arrivalDate) { this.arrivalDate = arrivalDate; }
    public int getDuration() { return duration; }
    public int getDeliveryID() {
        return deliveryID;
    }
    public void setDuration(int durationInSeconds) { this.duration = durationInSeconds; }
    public boolean isPickUp() { return type; }
    public boolean isDropOff() { return !type; }

    public Step(Vertex vertex, int deliveryID){
        sections = new LinkedList<>();
        this.type = vertex.isPickUp();
        this.duration = vertex.getDuration();
        this.deliveryID = deliveryID;
    }

    public void addSection(Section section){
        sections.addFirst(section);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Step step = (Step) o;
        return deliveryID == step.deliveryID
                && type == step.type
                && arrivalDate == step.arrivalDate
                && duration == step.duration
                && Objects.equals(sections, step.sections);
    }
}