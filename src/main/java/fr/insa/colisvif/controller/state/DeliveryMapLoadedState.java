package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.App;
import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.view.UIController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
     *
     * @param controller   controller of the application
     * @param uiController controller of the user interface
     * @param file         an xml file that contains the map to load
     * @see Controller
     */
    @Override
    public void loadCityMap(Controller controller, UIController uiController, File file) {
        try {
            controller.setCityMap(controller.getCityMapFactory().createCityMapFromXMLFile(file));
            controller.setCurrentState(CityMapLoadedState.class);
            uiController.printStatus("La carte a bien été chargée.\nVous pouvez désormais charger un plan de livraison.");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XMLException e) {
            LOGGER.error(e.getMessage(), e);
            uiController.printError("Le fichier chargé n'est pas un fichier correct.");
        }
    }

    /**
     * Creates a {@link fr.insa.colisvif.model.DeliveryMap} that will be stocked in the <code>controller</code> from a {@link File}.
     *
     * @param controller   controller of the application
     * @param uiController controller of the user interface
     * @param file         an xml file that contains the deliveries to load
     * @param cityMap      the map of the city
     * @see Controller
     */
    @Override
    public void loadDeliveryMap(Controller controller, UIController uiController, File file, CityMap cityMap) {
        try {
            controller.setDeliveryMap(controller.getDeliveryMapFactory().createDeliveryMapFromXML(file, cityMap));
            controller.setCurrentState(DeliveryMapLoadedState.class);
            uiController.printStatus("Le plan de livraison a bien été chargé.\nVous pouvez désormais calculer un itinéraire.");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XMLException e) {
            LOGGER.error(e.getMessage(), e);
            uiController.printError("Le fichier chargé n'est pas un fichier correct.");
        }
    }

    /**
     * Calculates a {@link fr.insa.colisvif.model.Round}.
     */
    // todo : prendre en compte les 30 secondes, pour le moment osef on n'a pas de demandes si longues à calculer
    // todo : ajouter le calcul d'itinéraire quand il fera pas vomir la console
    @Override
    public void calculateItinerary(Controller controller, UIController uiController) {
        Service<Round> itineraryComputation = new Service<>() {
            @Override
            protected Task<Round> createTask() {
                return new Task<>() {
                    @Override
                    protected Round call() throws Exception {
                        LOGGER.debug("Started worker thread");
                        try {
                            return controller.getCityMap().shortestRound(controller.getDeliveryMap());
                        } catch (InterruptedException e) {
                            LOGGER.warn("Interrupted shortest path computation", e);
                        }
                        return null;
                    }
                };
            }
        };

        Service<Void> timeout = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        LOGGER.debug("Started interrupt thread");
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException ignored) {
                        }
                        return null;
                    }
                };
            }
        };

        itineraryComputation.setOnSucceeded(event -> {
            Round round = itineraryComputation.getValue();
            controller.setRound(round);
            controller.setCurrentState(ItineraryCalculatedState.class);
            uiController.printStatus("L'itinéraire a bien été calculé.");
            timeout.cancel();
        });

        timeout.setOnSucceeded(event -> {
            if (itineraryComputation.cancel()) {
                LOGGER.warn("Interrupted itinerary calculation");
                LOGGER.info("Calculates approximate itinerary instead");
                Round round = controller.getCityMap().naiveRound(controller.getDeliveryMap());
                controller.setRound(round);
                controller.setCurrentState(ItineraryCalculatedState.class);
                uiController.printStatus("L'itinéraire a bien été calculé.");
            } else {
                LOGGER.warn("Could not interrupt itinerary calculation");
            }
        });

        itineraryComputation.start();
        timeout.start();
    }
}
