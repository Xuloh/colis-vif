package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.state.State;
import fr.insa.colisvif.view.MainController;

import java.io.File;

public class InitialState implements State {

    @Override
    public void loadCityMap(Controller c, MainController mc, File file) {
        c.openFile(file);
        mc.getMapCanvas().drawMap();
        c.setCurrentState(CityMapLoadedState.class);
    }
}
