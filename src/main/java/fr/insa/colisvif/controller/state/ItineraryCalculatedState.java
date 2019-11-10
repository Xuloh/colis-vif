package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.IdException;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

//TODO : Ajouter Etat Cas ou trajet non optimal

public class ItineraryCalculatedState implements State {

    @Override
    public void loadCityMap(Controller controller, UIController uiController, File file) {
        uiController.clearCanvas();
        try {
            controller.setMap(controller.getCityMapFactory().createCityMapFromXMLFile(file));
            uiController.getMapCanvas().setCityMap(controller.getMap());
        } catch (IOException | SAXException | ParserConfigurationException | IdException e) {
            e.printStackTrace();
        }
        uiController.getMapCanvas().setDeliveryMap(null);
        uiController.drawCanvas();
        controller.setCurrentState(CityMapLoadedState.class);
    }

    @Override
    public void loadDeliveryMap(Controller controller, UIController uiController, File file, CityMap cityMap) {
        try {
            controller.setDeliveryMap(controller.getDeliveryMapFactory().createDeliveryMapFromXML(file, cityMap));
            //mc.writeDeliveries(c.getDeliveryMap());
            uiController.getMapCanvas().setDeliveryMap(controller.getDeliveryMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        uiController.clearCanvas();
        uiController.drawCanvas();
        controller.setCurrentState(DeliveryMapLoadedState.class);
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
