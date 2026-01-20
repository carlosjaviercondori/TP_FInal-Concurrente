    /**
     * Class representing a place in a Petri net.
     */
public class Place {
    private final int id;
    private int tokens;

    /**
     * Constructor for Place.
     * @param id
     * @param tokens
     */
    public Place(int id, int tokens) {
        this.id = id;
        this.tokens = tokens;
    }
    /**
     * Get the ID of the place.
     * @return
     */
    public int getId() {
        return id;
    }
    /**
     * Get the number of tokens in the place.
     * @return
     */
    public int getTokens() {
        return tokens;
    }
    /**
     * Set the number of tokens in the place.
     * @param tokens
     */
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    /**
     * Add a token to the place.
     */
    public void addToken() {
        tokens++;
    }

    /**
     * Remove a token from the place if available.
     */
    public void removeToken() {
        if (tokens > 0) tokens--;
    }
    
}
