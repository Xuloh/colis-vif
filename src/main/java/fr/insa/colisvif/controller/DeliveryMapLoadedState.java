package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.MainController;

import java.io.File;

public class DeliveryMapLoadedState implements State {

    @Override
    public void loadCityMap(Controller c, MainController mc, File file) {
        mc.clearMap();
        c.openFile(file);
        mc.drawMap(c.getMap());
        c.setCurrentState(c.cityMapLoadedState);
    }

    @Override
    public void loadDeliveryMap(Controller c, MainController mc) {
        c.setCurrentState(c.deliveryMapLoadedState);
    }
}
