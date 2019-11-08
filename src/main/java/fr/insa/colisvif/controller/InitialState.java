package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.MainController;

import java.io.File;

public class InitialState implements State {

    @Override
    public void loadCityMap(Controller c, MainController mc, File file) {
        c.openFile(file);
        mc.getMapCanvas();
        c.setCurrentState(c.cityMapLoadedState);
    }
}
