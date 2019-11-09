package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;

import java.io.File;

public interface State {

    default void loadCityMap(Controller c, UIController mc, File file) {

    }


    default void loadDeliveryMap(Controller c, UIController mc, File file, CityMap cityMap) {

    }

    default void calculateItinerary() {

    }

    default void saveRoadMap() {

    }

    default void switchToSuppressionMode() {

    }

    default void suppressStop() {

    }

    default void switchToAddMode() {

    }

    default void addPickUpNode() {

    }

    default void addDropOffNode() {

    }

    default void showNodeProperties()  {

    }

    default void switchToOrderChangeMode() {

    }

    default void switchToLocationChange() {

    }

    default void changeNodeLocation() {

    }

    default void continueCalculation() {

    }

    default void stopCalculation() {

    }

    default void selectStopToPass() {

    }

    default void calculateItineraryLocally() {

    }

    default void selectStopToDelete() {

    }

    default void getBackToPreviousState(Controller c) {

    }

}
