package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.MainController;

public class CityMapLoadedState implements State{

    @Override
    public void loadDeliveryMap(Controller c, MainController mc) {
        c.setCurrentState(c.deliveryMapLoadedState);
    }

    @Override
    public void loadCityMap(Controller c, MainController mc) {
        c.setCurrentState(c.cityMapLoadedState);
    }
}
