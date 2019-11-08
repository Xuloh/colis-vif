package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;

import java.io.File;

public class CityMapLoadedState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
        mc.getMapCanvas().clearMap();
        c.openFile(file);
        mc.getMapCanvas().drawMap();
        c.setCurrentState(CityMapLoadedState.class);
    }

    @Override
    public void loadDeliveryMap(Controller c, UIController mc, File file, CityMap cityMap) {
        c.openDeliveryMap(file, cityMap);
        c.setCurrentState(DeliveryMapLoadedState.class);
    }
}
