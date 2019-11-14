package fr.insa.colisvif.view;

import fr.insa.colisvif.model.Round;
import fr.insa.colisvif.model.Section;
import fr.insa.colisvif.model.Step;
import fr.insa.colisvif.model.Vertex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * This view is used to export a round in a file chosen by the user, in text format.
 */
public class ExportView  {

    private static final Logger LOGGER = LogManager.getLogger(ExportView.class);

    private UIController uiController;

    /**
     * Constructor of {@link ExportView}.
     * @param uiController the {@link UIController} instance of the application.
     */
    public ExportView(UIController uiController) {
        this.uiController = uiController;
    }

    private void exportStep(Step step, PrintStream printStream) { // Export one step
        double length = 0;
        String roadName = step.getSections().get(0).getRoadName();

        for (Section s : step.getSections()) { // Print only when new roadname, calculate the length of the road
            if (!s.getRoadName().equals(roadName)) {
                printStream.println("Prendre la rue " + roadName + " sur " + (int) length + "m.");
                roadName = s.getRoadName();
                length = s.getLength();
            } else {
                length += s.getLength();
            }
        }
        printStream.println("Prendre la rue " + roadName + " sur " + (int) length + "m.");

        String msg = step.getType() == Vertex.DROP_OFF ? "livrer le colis n°" : " récupérer le colis n°";
        printStream.println("Arrivée prévu à " + formatHour(step.getArrivalDate()) + " pour " + msg + step.getDeliveryID() + ".");
        printStream.println("Estimation du temps de passage : " + step.getDuration() / 60 + "mins.\n");
    }

    private String formatHour(int amountSecond) { // Format the hour given in second in hh:mm
        int minutes = amountSecond / 60;
        int heures = minutes / 60;
        minutes = heures % 60;

        String minutesStr = minutes < 10 ? "0" + minutes : "" + minutes;
        String heuresStr = heures < 10 ? "0" + heures : "" + heures;

        return  heuresStr + ":" + minutesStr;
    }

    /**
     * Export a {@link Round} round in a file chosen by the user.
     * @param round the {@link Round to export}.
     */
    public void exportRound(Round round, File file) {
        try {
            if (!file.createNewFile()) {
                this.uiController.printError("Le fichier existe déjà");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (!file.canWrite()) {
            this.uiController.printError("Impossible d'écrire dans le fichier");
        }

        try (PrintStream PRINT_STREAM = new PrintStream(file)) {
            PRINT_STREAM.println("Début de la tournée (prévue à " + formatHour(round.getStartDate()) + ").");

            round.getSteps().forEach(step -> this.exportStep(step, PRINT_STREAM));

            Step lastItem = round.getSteps().get(round.getSteps().size() - 1);
            int arrivalDate = lastItem.getArrivalDate() + lastItem.getDuration();

            System.out.println("Fin de la tournée (prévue à " + formatHour(arrivalDate) + ").");
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
