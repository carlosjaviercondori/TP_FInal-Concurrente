import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Monitor implements MonitorInterface {
    private final PetriNet petriNet;
    private FileWriter logWriter;

    public Monitor(PetriNet petriNet) {
        this.petriNet = petriNet;
        try {
            logWriter = new FileWriter("petri_log.txt", true); // Append mode
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean fireTransition(int transitionId) {
        Transition transition = petriNet.getTransition(transitionId);
        if (transition == null) return false;
        // Check if the transition is enabled
        for (int placeId : transition.getInputPlaces()) {
            Place place = petriNet.getPlace(placeId);
            if (place.getTokens() == 0) {
                return false; // Not enabled
            }
        }
        // Fire the transition: remove input tokens
        for (int placeId : transition.getInputPlaces()) {
            petriNet.getPlace(placeId).removeToken();
        }
        // Add tokens to output places
        for (int placeId : transition.getOutputPlaces()) {
            petriNet.getPlace(placeId).addToken();
        }
        // Log the event
        logTransition(transitionId);
        return true;
    }

    private void logTransition(int transitionId) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(new Date()).append(" | Thread: ").append(Thread.currentThread().getName());
            sb.append(" | T").append(transitionId).append(" fired | Places state: ");
            for (Place place : petriNet.getPlaces().values()) {
                sb.append("P").append(place.getId()).append(":").append(place.getTokens()).append(" ");
            }
            sb.append("\n");
            logWriter.write(sb.toString());
            logWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // New method to check if a transition is enabled
    public synchronized boolean isEnabled(int transitionId) {
        Transition transition = petriNet.getTransition(transitionId);
        if (transition == null) return false;
        for (int placeId : transition.getInputPlaces()) {
            Place place = petriNet.getPlace(placeId);
            if (place.getTokens() == 0) {
                return false;
            }
        }
        return true;
    }
}
