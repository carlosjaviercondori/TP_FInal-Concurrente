import java.util.*;
/**
 * Utility class to initialize a Petri net with predefined places and transitions.
 * This class provides a method to create and return a PetriNet instance
 * configured with specific places and transitions.
 */
public class PetriNetInitializer {
    public static PetriNet createPetriNet() {
        // Crear plazas (Place)
        Map<Integer, Place> places = new HashMap<>();
        // Ejemplo: P0 con 1 token, el resto con 0 (ajusta según la red real)
        places.put(0, new Place(0, 3)); // P0
        places.put(1, new Place(1, 0)); // P1
        places.put(2, new Place(2, 0)); // P2
        places.put(3, new Place(3, 0)); // P3
        places.put(4, new Place(4, 0)); // P4
        places.put(5, new Place(5, 0)); // P5
        places.put(6, new Place(6, 0)); // P6
        places.put(7, new Place(7, 0)); // P7
        places.put(8, new Place(8, 0)); // P8
        places.put(9, new Place(9, 0)); // P9
        places.put(10, new Place(10, 0)); // P10
        places.put(11, new Place(11, 0)); // P11

        // Crear transiciones (Transition)
        Map<Integer, Transition> transitions = new HashMap<>();
        // Ejemplo: T0 conecta P0 -> P1
        transitions.put(0, new Transition(0, Arrays.asList(0), Arrays.asList(1)));
        transitions.put(1, new Transition(1, Arrays.asList(1), Arrays.asList(2, 3)));
        transitions.put(2, new Transition(2, Arrays.asList(3), Arrays.asList(4)));
        transitions.put(3, new Transition(3, Arrays.asList(4), Arrays.asList(5)));
        transitions.put(4, new Transition(4, Arrays.asList(5), Arrays.asList(11)));
        transitions.put(5, new Transition(5, Arrays.asList(3), Arrays.asList(6)));
        transitions.put(6, new Transition(6, Arrays.asList(6), Arrays.asList(11)));
        transitions.put(7, new Transition(7, Arrays.asList(3), Arrays.asList(8)));
        transitions.put(8, new Transition(8, Arrays.asList(8), Arrays.asList(9)));
        transitions.put(9, new Transition(9, Arrays.asList(9), Arrays.asList(10)));
        transitions.put(10, new Transition(10, Arrays.asList(10), Arrays.asList(11)));
        transitions.put(11, new Transition(11, Arrays.asList(11), Arrays.asList(0)));

        return new PetriNet(places, transitions);
    }
}
