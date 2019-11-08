package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.view.UIController;

import java.io.File;

public class InitialState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
        c.openFile(file);
        mc.drawCanvas();
        c.setCurrentState(CityMapLoadedState.class);
    }
}
