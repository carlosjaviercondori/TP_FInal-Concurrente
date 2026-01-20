import java.util.List;

/**
 * Represents a transition in a Petri net.
 * Each transition has a unique ID,
 *  a list of input places, and a list of output places.
 */
public class Transition {
    private final int id;// Unique identifier for the transition
    private final List<Integer> inputPlaces;// IDs of input places
    private final List<Integer> outputPlaces;// IDs of output places

    /**
     * Constructor for Transition.
     * @param id
     * @param inputPlaces
     * @param outputPlaces
     */
    public Transition(int id, List<Integer> inputPlaces, List<Integer> outputPlaces) {
        this.id = id;
        this.inputPlaces = inputPlaces;
        this.outputPlaces = outputPlaces;
    }
    /**
     * Get the unique identifier of the transition.
     * @return transition ID
     */
    public int getId() {
        return id;
    }
    /**
     * Get the list of input place IDs.
     * @return
     */
    public List<Integer> getInputPlaces() {
        return inputPlaces;
    }
    /**
     * Get the list of output place IDs.
     * @return
     */
    public List<Integer> getOutputPlaces() {
        return outputPlaces;
    }
}
