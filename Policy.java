/**
 * Abstract class representing a policy for selecting
 * transitions in a Petri net.
 * 
 */
public abstract class Policy {
    public abstract int selectTransition(int[] enabledTransitions);

    
}
