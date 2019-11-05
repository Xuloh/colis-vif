package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.MainController;

import java.io.File;

public interface State {

    default void loadCityMap(Controller c, MainController mc, File file) {

    }


    default void loadDeliveryMap(Controller c, MainController mc) {

    }

}
