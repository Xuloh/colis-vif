package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.view.UIController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Class that implements State interface.
 * This class represents the initial state of the application.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class InitialState implements State {

    private static final Logger LOGGER = LogManager.getLogger(InitialState.class);

    /**
     * Creates a {@link fr.insa.colisvif.model.CityMap} that will be stocked in the <code>controller</code> from a {@link File}.
     * @param controller controller of the application
     * @param uiController controller of the user interface
     * @param file an xml file that contains the map to load
     *
     * @see Controller
     */
    @Override
    public void loadCityMap(Controller controller, UIController uiController, File file) {
        try {
            controller.setCityMap(controller.getCityMapFactory().createCityMapFromXMLFile(file));
        } catch (IOException | SAXException | ParserConfigurationException | XMLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        controller.setCurrentState(CityMapLoadedState.class);
    }
}
