/**
 * Main class to run the Petri net simulation.
 */
public class Main {
    public static void main(String[] args) {
        PetriNet petriNet = PetriNetInitializer.createPetriNet();
        System.out.println("Red de Petri inicializada.");
        System.out.println("Plazas:");
        for (Place place : petriNet.getPlaces().values()) {
            System.out.println("P" + place.getId() + ": " + place.getTokens() + " tokens");
        }
        System.out.println("Transiciones:");
        for (Transition transition : petriNet.getTransitions().values()) {
            System.out.println("T" + transition.getId() + ": entradas " + transition.getInputPlaces() + ", salidas " + transition.getOutputPlaces());
        }
    }
}
