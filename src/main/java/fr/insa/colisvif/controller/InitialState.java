package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.MainController;

public class InitialState implements State {

    @Override
    public void loadCityMap(Controller c, MainController mc) {
        c.setCurrentState(c.cityMapLoadedState);
    }
}
