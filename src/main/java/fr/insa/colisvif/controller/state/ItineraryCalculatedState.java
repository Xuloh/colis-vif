package fr.insa.colisvif.controller.state;

import fr.insa.colisvif.controller.Controller;
import fr.insa.colisvif.controller.command.CommandList;
import fr.insa.colisvif.controller.command.CommandRemove;
import fr.insa.colisvif.exception.XMLException;
import fr.insa.colisvif.model.*;
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
 * This class represents the state when the {@link fr.insa.colisvif.model.Round}
 * is calculated.
 * It overrides all the actions that can be done during this state.
 *
 * @see State
 * @see <a href="https://en.wikipedia.org/wiki/State_pattern">State pattern</a>
 */
public class ItineraryCalculatedState implements State {

    private static final Logger LOGGER = LogManager
            .getLogger(ItineraryCalculatedState.class);

    /**
     * Creates a {@link CityMap} that will be stocked in the
     * <code>controller</code> from a {@link File}.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param file an xml file that contains the map to load
     *
     * @see Controller
     */
    @Override
    public void loadCityMap(Controller controller, UIController uiController,
                            File file) {
        try {
            controller.setCityMap(controller.getCityMapFactory()
                    .createCityMapFromXMLFile(file));
            controller.setCurrentState(CityMapLoadedState.class);
            uiController.printStatus("La carte a bien été chargée.\n"
                    + "Vous pouvez désormais charger un plan de livraison.");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XMLException e) {
            LOGGER.error(e.getMessage(), e);
            uiController.printError("Le fichier chargé "
                    + "n'est pas un fichier correct.");
        }
    }

    /**
     * Creates a {@link fr.insa.colisvif.model.DeliveryMap} that will be
     * stocked in the <code>controller</code> from a {@link File}.
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param file an xml file that contains the deliveries to load
     * @param cityMap the map of the city
     *
     * @see Controller
     */
    @Override
    public void loadDeliveryMap(Controller controller,
                                UIController uiController,
                                File file, CityMap cityMap) {
        try {
            controller.setDeliveryMap(controller.getDeliveryMapFactory()
                    .createDeliveryMapFromXML(file, cityMap));
            controller.setCurrentState(DeliveryMapLoadedState.class);
            uiController.printStatus("Le plan de livraison a bien été "
                    + "chargé.\nVous pouvez désormais calculer un itinéraire.");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (XMLException e) {
            LOGGER.error(e.getMessage(), e);
            uiController.printError("Le fichier chargé n'est "
                    + "pas un fichier correct.");
        }
    }

    /**
     * Saves the road map associated to a
     * {@link fr.insa.colisvif.model.Round} in a text file.
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
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     */
    @Override
    public void switchToAddMode(Controller controller,
                                UIController uiController) {
        uiController.setShowCityMapNodesOnHover(true);
        controller.setCurrentState(ModeAddState.class);
        uiController.disableButtons();
        uiController.printStatus("Sélection la position "
                + "du noeud d'enlèvement.");
        uiController.addPickUp();
    }

    /**
     * Deletes a selected delivery
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     * @param step the selected step that is in the delivery the user
     *            wants to suppress
     */
    @Override
    public void deleteDelivery(Controller controller, UIController uiController,
                               CommandList commandList, Step step) {
        if (controller.getDeliveryMap().getSize() == 1) {
            uiController.printError("Vous ne pouvez pas supprimer la"
                    + " dernière livraison.");
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
            commandList.doCommand(new CommandRemove(stepSelected,
                    otherDeliveyStep, controller.getRound(),
                    controller.getCityMap(),
                    controller.getStepList().indexOf(step),
                    controller.getStepList().indexOf(otherDeliveyStep),
                    deliveryId));
            controller.createVertexList();
            uiController.updateDeliveryMap();
            uiController.updateRound();
            uiController.getMapCanvas().redraw();
            controller.setButtons();
        }
    }

    /**
     * Undoes the last modification
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     */
    @Override
    public void undo(Controller controller, UIController uiController,
                     CommandList commandList) {
        commandList.undoCommand();
        uiController.updateDeliveryMap();
        controller.createVertexList();
        uiController.updateDeliveryMap();
        uiController.updateRound();
        uiController.getMapCanvas().redraw();
        controller.setButtons();
    }

    /**
     * Redoes the last modification
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param commandList {@link CommandList} of the controller
     */
    @Override
    public void redo(Controller controller, UIController uiController,
                     CommandList commandList) {
        commandList.redoCommand();
        uiController.updateDeliveryMap();
        uiController.updateRound();
        controller.createVertexList();
        uiController.getMapCanvas().redraw();
        controller.setButtons();
    }

    /**
     * Changes state of the controller and passes the selected step
     * to allow the modification of the location
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param step the step that the user wants to switch
     */
    @Override
    public void switchToLocationChange(Controller controller,
                                       UIController uiController, Step step) {
        uiController.setShowCityMapNodesOnHover(true);
        uiController.disableButtons();
        controller.getMSLState().entryToState(step);
        controller.setCurrentState(ModifyStopLocationState.class);
    }

    /**
     *
     * @param controller {@link Controller} of the application
     * @param uiController {@link UIController} of the application
     * @param step the step the user wants to modify
     */
    @Override
    public void switchToOrderChangeMode(Controller controller,
                                        UIController uiController, Step step) {
        uiController.disableButtons();
        controller.getMOState().entryToState(step);
        controller.setCurrentState(ModifyOrderState.class);
    }
}
