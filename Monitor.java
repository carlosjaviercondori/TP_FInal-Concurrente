public class Monitor implements MonitorInterface {
    private final PetriNet petriNet;

    public Monitor(PetriNet petriNet) {
        this.petriNet = petriNet;
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
        return true;
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
