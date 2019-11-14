package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.controller.command.CommandRemove;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.model.CityMap;
import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.view.ExportView;
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
     * Saves the road map associated to a {@link fr.insa.colisvif.model.Round} in a text file.
     */
    @Override
    public void saveRoadMap(Controller controller, File file) {
        Round deliveryRound = controller.getRound();
        ExportView ev = new ExportView(controller.getUIController());
        ev.exportRound(deliveryRound, file);
    }


    /**
     * Enters the {@link fr.insa.colisvif.controller.state.ModeAddState}
     * to allow the user to add more deliveries
     */
    @Override
    public void switchToAddMode(Controller controller, UIController uiController) {
        uiController.setShowCityMapNodesOnHover(true);
        controller.setCurrentState(ModeAddState.class);
        uiController.addPickUp();
    }

    /**
     * Show the properties of a node selected from the textual view or from the canvas.
     */
    // todo : dégager ça et déplacer les méthodes de PropertiesPrintedState dans cet état
    // todo : utiliser les méthodes que Sophie filera
    @Override
    public void showNodeProperties() {

    }

    /**
     * Calculates a {@link fr.insa.colisvif.model.Round}.
     */
    // todo : prendre en compte les 30 secondes, pour le moment osef on n'a pas de demandes si longues à calculer
    // todo : ajouter le calcul d'itinéraire quand il fera pas vomir la console
    @Override
    public void calculateItinerary(Controller controller, UIController uiController) {
        controller.setRound(controller.getCityMap().shortestRound(controller.getDeliveryMap()));
        controller.setCurrentState(ItineraryCalculatedState.class);
    }

    @Override
    public void deleteDelivery(Controller controller, UIController uiController, CommandList commandList, Step step) {
        if (controller.getDeliveryMap().getSize() == 1) {
            uiController.printError("Vous ne pouvez pas supprimer la dernière livraison.");
        } else {
            Step stepSelected = step;
            Step otherDeliveyStep = null;
            int deliveryId = stepSelected.getDeliveryID();
            for (Step step1 : controller.getStepList()) {
                if (step1.getDeliveryID() == deliveryId && step != step1) {
                    otherDeliveyStep = step1;
                    break;
                }
            }
            commandList.doCommand(new CommandRemove(stepSelected, otherDeliveyStep, controller.getRound(), controller.getCityMap(),
                    controller.getStepList().indexOf(step), controller.getStepList().indexOf(otherDeliveyStep)));
            controller.createVertexList();
            uiController.updateDeliveryMap();
            uiController.updateRound();
            uiController.getMapCanvas().redraw();
            controller.setButtons();
        }
    }

    @Override
    public void undo(Controller controller, UIController uiController, CommandList commandList) {
        commandList.undoCommand();
        controller.createVertexList();
        uiController.updateDeliveryMap();
        uiController.updateRound();
        uiController.getMapCanvas().redraw();
        controller.setButtons();
    }

    @Override
    public void redo(Controller controller, UIController uiController, CommandList commandList) {
        commandList.redoCommand();
        controller.createVertexList();
        uiController.updateDeliveryMap();
        uiController.updateRound();
        uiController.getMapCanvas().redraw();
        controller.setButtons();
    }

    @Override
    public void switchToLocationChange(Controller controller, UIController uiController, Step step) {
        uiController.setShowCityMapNodesOnHover(true);
        controller.getMSLState().entryToState(step);
        controller.setCurrentState(ModifyStopLocationState.class);
    }
}
