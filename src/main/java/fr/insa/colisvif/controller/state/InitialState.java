package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.IdException;
import fr.insa.colisvif.view.UIController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InitialState implements State {

    @Override
    public void loadCityMap(Controller controller, UIController uiController, File file) {
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
}
