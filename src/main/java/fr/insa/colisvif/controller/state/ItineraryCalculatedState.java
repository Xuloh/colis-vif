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

//TODO : Ajouter Etat Cas ou trajet non optimal
/**
 * Class that implements State interface.
 * This class represents the state when the {@link fr.insa.colisvif.model.Round} is calculated.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ItineraryCalculatedState implements State {

    private static final Logger LOGGER = LogManager.getLogger(ItineraryCalculatedState.class);

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
        controller.setCurrentState(DeliveryMapLoadedState.class);
    }

    /**
     * Saves the road map associated to a {@link fr.insa.colisvif.model.Round} in a text file.
     */
    @Override
    public void saveRoadMap() {

    }

    /**
     * Enters in suppression mode to enable actions like selecting a vertex to suppress
     * the linked delivery.
     *
     * @see SuppressionModeState
     */
    @Override
    public void switchToSuppressionMode() {

    }

    /**
     * Enters the {@link fr.insa.colisvif.controller.state.ModeAddState}
     * to allow the user to add more deliveries
     */
    @Override
    public void switchToAddMode() {

    }

    /**
     * Show the properties of a node selected from the textual view or from the canvas.
     */
    @Override
    public void showNodeProperties() {

    }
}
