package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.IdException;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class CityMapLoadedState implements State {

    @Override
    public void loadCityMap(Controller c, UIController mc, File file) {
        mc.clearCanvas();
        try {
            c.setCityMap(c.getCityMapFactory().createCityMapFromXMLFile(file));
            mc.getMapCanvas().setCityMap(c.getCityMap());
        } catch (IOException | SAXException | ParserConfigurationException | IdException e) {
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
            mc.getMapCanvas().setDeliveryMap(c.getDeliveryMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mc.clearCanvas();
        mc.drawCanvas();
        mc.printTextualView();
        c.setCurrentState(DeliveryMapLoadedState.class);
    }
}
