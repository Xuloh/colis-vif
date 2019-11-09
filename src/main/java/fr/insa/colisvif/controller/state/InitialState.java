package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.IdError;
import fr.insa.colisvif.view.UIController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InitialState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
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
}
