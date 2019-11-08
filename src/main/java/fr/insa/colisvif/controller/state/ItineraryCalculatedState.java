package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;

import java.io.File;

//TODO : Ajouter Etat Cas ou trajet non optimal

public class ItineraryCalculatedState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
        mc.clearCanvas();
        c.openFile(file);
        mc.drawCanvas();
        c.setCurrentState(CityMapLoadedState.class);
    }

    @Override
    public void loadDeliveryMap(Controller c, UIController mc, File file, CityMap cityMap) {
        c.openDeliveryMap(file, cityMap);
        c.setCurrentState(DeliveryMapLoadedState.class);
    }

    @Override
    public void saveRoadMap() {

    }

    @Override
    public void switchToSuppressionMode() {

    }

    @Override
    public void switchToAddMode() {

    }

    @Override
    public void showNodeProperties() {

    }
}
