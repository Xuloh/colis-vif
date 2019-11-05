package fr.insa.colisvif.controller;

import fr.insa.colisvif.view.MainController;

public interface State {

    public default void loadCityMap(Controller c, MainController mc) {};

    public default void loadDeliveryMap(Controller c, MainController mc) {};
}
