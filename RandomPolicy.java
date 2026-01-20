import java.util.Random;
/**
 * A policy that selects a transition to fire at random from the 
 * set of enabled transitions.
 */
public class RandomPolicy extends Policy {
    private final Random random = new Random();

    @Override
    public int selectTransition(int[] enabledTransitions) {
        if (enabledTransitions.length == 0) return -1;
        return enabledTransitions[random.nextInt(enabledTransitions.length)];
    }
}
