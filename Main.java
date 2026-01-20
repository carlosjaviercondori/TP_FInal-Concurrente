/**
 * Main class to run the Petri net simulation.
 */
public class Main {
    public static void main(String[] args) {
        PetriNet petriNet = PetriNetInitializer.createPetriNet();
        Monitor monitor = new Monitor(petriNet);

        System.out.println("Estado inicial de las plazas:");
        for (Place place : petriNet.getPlaces().values()) {
            System.out.println("P" + place.getId() + ": " + place.getTokens() + " tokens");
        }

        // Intentar disparar algunas transiciones
        int[] transicionesAProbar = {0, 1, 2}; // T0, T1, T2
        for (int t : transicionesAProbar) {
            boolean fired = monitor.fireTransition(t);
            System.out.println("Intentando disparar T" + t + ": " + (fired ? "Éxito" : "No habilitada"));
        }

        System.out.println("\nEstado final de las plazas:");
        for (Place place : petriNet.getPlaces().values()) {
            System.out.println("P" + place.getId() + ": " + place.getTokens() + " tokens");
        }
    }
}
