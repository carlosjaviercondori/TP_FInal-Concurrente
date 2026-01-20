public class PrioritizedPolicy extends Policy {
    private final int simpleTransitionId;

    public PrioritizedPolicy(int simpleTransitionId) {
        this.simpleTransitionId = simpleTransitionId;
    }

    @Override
    public int selectTransition(int[] enabledTransitions) {
        for (int t : enabledTransitions) {
            if (t == simpleTransitionId) {
                return t; // Prioriza el modo simple
            }
        }
        // Si no está habilitado el modo simple, elige el primero disponible
        return enabledTransitions.length > 0 ? enabledTransitions[0] : -1;
    }
}
