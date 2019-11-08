package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.MainController;

import java.io.File;

public interface State {

    default void loadCityMap(Controller c, MainController mc, File file) {

    }


    default void loadDeliveryMap(Controller c, MainController mc, File file, CityMap cityMap) {

    }

}
