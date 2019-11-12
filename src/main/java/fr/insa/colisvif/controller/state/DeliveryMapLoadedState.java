package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.view.UIController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Class that implements State interface.
 * This class represents the state when the {@link fr.insa.colisvif.model.DeliveryMap} is loaded.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class DeliveryMapLoadedState implements State {

    private static final Logger LOGGER = LogManager.getLogger(DeliveryMapLoadedState.class);

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
            LOGGER.error(e.getMessage(), e);
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
            //mc.writeDeliveries(c.getDeliveryMap());
            uiController.getMapCanvas().setDeliveryMap(controller.getDeliveryMap());
        } catch (IOException | SAXException | ParserConfigurationException | XMLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        uiController.clearCanvas();
        uiController.drawCanvas();
        uiController.updateTable(false);
        controller.setCurrentState(DeliveryMapLoadedState.class);
    }

    /**
     * Calculates a {@link fr.insa.colisvif.model.Round}.
     */
    // todo : prendre en compte les 30 secondes, pour le moment osef on n'a pas de demandes si longues à calculer
    // todo : ajouter le calcul d'itinéraire quand il fera pas vomir la console
    @Override
    public void calculateItinerary(Controller controller, UIController uiController) {
        controller.setRound(controller.getCityMap().shortestRound(controller.getDeliveryMap()));
        uiController.updateTable(true);
        controller.setCurrentState(ItineraryCalculatedState.class);
    }
}
