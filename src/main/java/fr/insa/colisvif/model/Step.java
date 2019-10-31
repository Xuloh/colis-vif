package fr.insa.colisvif.model;

import java.util.List;

public class Step {
    private List<Section> sections;
    private boolean type; //0 if it is a pick up and 1 if it is a drop off
    private int arrivalDateInSeconds; //the date when the delivery man will get to delivery point
    private int durationInSeconds; //the duration of the pick up or the drop off, NOT THE TRAVEL TIME

    public List<Section> getSections() { return sections; }
    public int getArrivalDateInSeconds() { return arrivalDateInSeconds; }
    public void setArrivalDateInSeconds(int arrivalDateInSeconds) { this.arrivalDateInSeconds = arrivalDateInSeconds; }
    public int getDurationInSeconds() { return durationInSeconds; }
    public void setDurationInSeconds(int durationInSeconds) { this.durationInSeconds = durationInSeconds; }
    public boolean isPickUp() { return type; }
    public boolean isDropOff() { return !type; }

    public Step(List<Section> sections, Boolean type, int arrivalDateInSeconds, int durationInSeconds){
        this.sections = sections;
        this.type = type;
        this.arrivalDateInSeconds = arrivalDateInSeconds;
        this.durationInSeconds = durationInSeconds;
    }
}