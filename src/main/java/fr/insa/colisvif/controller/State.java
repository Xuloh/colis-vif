package fr.insa.colisvif.controller;

import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;

import java.io.File;

public interface State {

    default void loadCityMap(Controller c, UIController mc, File file) {

    }


    default void loadDeliveryMap(Controller c, UIController mc, File file, CityMap cityMap) {

    }

}
