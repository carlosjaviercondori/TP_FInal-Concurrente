import java.util.Map;
import java.util.List;

/**
 * Represents a Petri net consisting of places and transitions.
 * Each place and transition is identified by a unique ID.
 * Places are stored in a map with their IDs as keys,
 * and transitions are stored similarly.
 */
public class PetriNet {
    private final Map<Integer, Place> places;
    private final Map<Integer, Transition> transitions;
    /**
     * Constructor for PetriNet.
     * @param places Map of place IDs to Place objects
     * @param transitions Map of transition IDs to Transition objects
     */
    public PetriNet(Map<Integer, Place> places, Map<Integer, Transition> transitions) {
        this.places = places;
        this.transitions = transitions;
    }
    /**
     * Get a place by its unique identifier.
     * @param id Place ID
     * @return Place object
     */
    public Place getPlace(int id) {
        return places.get(id);
    }

    /**
     * Get a transition by its unique identifier.
     * @param id Transition ID
     * @return Transition object
     */
    public Transition getTransition(int id) {
        return transitions.get(id);
    }
    /**
     * Get all places in the Petri net.
     * @return Map of place IDs to Place objects
     */
    public Map<Integer, Place> getPlaces() {
        return places;
    }
    /**
     * Get all transitions in the Petri net.
     * @return Map of transition IDs to Transition objects
     */
    public Map<Integer, Transition> getTransitions() {
        return transitions;
    }
}
