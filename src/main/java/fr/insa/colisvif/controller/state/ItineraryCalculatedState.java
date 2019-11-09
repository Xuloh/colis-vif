package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

//TODO : Ajouter Etat Cas ou trajet non optimal

public class ItineraryCalculatedState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
        mc.clearCanvas();
        try {
            c.setMap(c.getCityMapFactory().createCityMapFromXMLFile(file));
            mc.getMapCanvas().setCityMap(c.getMap());
        } catch (IOException | SAXException | ParserConfigurationException | IdError e) {
            e.printStackTrace();
        }
        mc.getMapCanvas().setDeliveryMap(null);
        mc.drawCanvas();
        c.setCurrentState(CityMapLoadedState.class);
    }

    @Override
    public void loadDeliveryMap(Controller c, UIController mc, File file, CityMap cityMap) {
        try {
            c.setDeliveryMap(c.getDeliveryMapFactory().createDeliveryMapFromXML(file, cityMap));
            mc.writeDeliveries(c.getDeliveryMap());
            mc.getMapCanvas().setDeliveryMap(c.getDeliveryMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mc.clearCanvas();
        mc.drawCanvas();
        c.setCurrentState(DeliveryMapLoadedState.class);
    }

    @Override
    public void saveRoadMap() {

    }

    @Override
    public void switchToSuppressionMode() {

    }

    @Override
    public void switchToAddMode() {

    }

    @Override
    public void showNodeProperties() {

    }
}
