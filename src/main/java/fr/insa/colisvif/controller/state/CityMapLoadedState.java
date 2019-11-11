package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.IdException;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Class that implements State interface.
 * This class represents the state when the {@link CityMap} is loaded.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class CityMapLoadedState implements State {

    /**
     * Creates a {@link CityMap} that will be stocked in the <code>controller</code> from a {@link File}.
     * @param controller controller of the application
     * @param uiController controller of the user interface
     * @param file an xml file that contains the map to load
     *
     * @see Controller
     */
    @Override
    public void loadCityMap(Controller controller, UIController uiController, File file) {
        uiController.clearCanvas();
        try {
            controller.setCityMap(controller.getCityMapFactory().createCityMapFromXMLFile(file));
            uiController.getMapCanvas().setCityMap(controller.getCityMap());
        } catch (IOException | SAXException | ParserConfigurationException | XMLException e) {
            e.printStackTrace();
        }
        uiController.getMapCanvas().setDeliveryMap(null);
        uiController.drawCanvas();
        controller.setCurrentState(CityMapLoadedState.class);
    }

    /**
     * Creates a {@link fr.insa.colisvif.model.DeliveryMap} that will be stocked in the <code>controller</code> from a {@link File}.
     * @param controller controller of the application
     * @param uiController controller of the user interface
     * @param file an xml file that contains the deliveries to load
     * @param cityMap the map of the city
     *
     * @see Controller
     */
    @Override
    public void loadDeliveryMap(Controller controller, UIController uiController, File file, CityMap cityMap) {
        try {
            controller.setDeliveryMap(controller.getDeliveryMapFactory().createDeliveryMapFromXML(file, cityMap));
            uiController.getMapCanvas().setDeliveryMap(controller.getDeliveryMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        uiController.clearCanvas();
        uiController.drawCanvas();
        uiController.printTextualView();
        controller.setCurrentState(DeliveryMapLoadedState.class);
    }
}
