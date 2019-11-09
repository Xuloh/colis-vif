package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;

import java.io.File;

public class DeliveryMapLoadedState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
        mc.clearCanvas();
        c.openFile(file);
        mc.getMapCanvas().setDeliveryMap(null);
        mc.drawCanvas();
        c.setCurrentState(CityMapLoadedState.class);
    }

    @Override
    public void loadDeliveryMap(Controller c, UIController mc, File file, CityMap cityMap) {
        c.openDeliveryMap(file, cityMap);
        mc.clearCanvas();
        mc.drawCanvas();
        c.setCurrentState(DeliveryMapLoadedState.class);
    }

    @Override
    public void calculateItinerary() {

    }
}
