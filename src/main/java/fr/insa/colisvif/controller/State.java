package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.MainController;

import java.io.File;

public interface State {

    public default void loadCityMap(Controller c, MainController mc, File file) {};

    public default void loadDeliveryMap(Controller c, MainController mc) {};
}
