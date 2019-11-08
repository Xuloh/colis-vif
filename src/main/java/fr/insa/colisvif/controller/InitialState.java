package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.UIController;

import java.io.File;

public class InitialState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
        c.openFile(file);
        mc.drawMap();
        c.setCurrentState(c.cityMapLoadedState);
    }
}
