import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Monitor implements MonitorInterface {
    private final PetriNet petriNet;
    private FileWriter logWriter;

    public Monitor(PetriNet petriNet) {
        this.petriNet = petriNet;
        try {
            logWriter = new FileWriter("petri_log.txt", true); // Modo append
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean fireTransition(int transitionId) {
        Transition transition = petriNet.getTransition(transitionId);
        if (transition == null) return false;
        // Verificar si la transición está habilitada
        for (int placeId : transition.getInputPlaces()) {
            Place place = petriNet.getPlace(placeId);
            if (place.getTokens() == 0) {
                return false; // No habilitada
            }
        }
        // Disparar la transición: quitar tokens de entrada
        for (int placeId : transition.getInputPlaces()) {
            petriNet.getPlace(placeId).removeToken();
        }
        // Agregar tokens a las plazas de salida
        for (int placeId : transition.getOutputPlaces()) {
            petriNet.getPlace(placeId).addToken();
        }
        // Registrar en el log
        logTransition(transitionId);
        return true;
    }

    private void logTransition(int transitionId) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(new Date()).append(" | Hilo: ").append(Thread.currentThread().getName());
            sb.append(" | T").append(transitionId).append(" disparada | Estado plazas: ");
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

    // Nuevo método para verificar si una transición está habilitada
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
