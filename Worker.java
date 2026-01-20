/**
 * Worker class that executes transition firings in a Petri net
 * according to a specified policy.
 */

public class Worker implements Runnable {
    private final Monitor monitor;
    private final Policy policy;
    private final int[] posiblesTransiciones;
    private final int disparosObjetivo;
    private int disparosRealizados = 0;

    public Worker(Monitor monitor, Policy policy, int[] posiblesTransiciones, int disparosObjetivo) {
        this.monitor = monitor;
        this.policy = policy;
        this.posiblesTransiciones = posiblesTransiciones;
        this.disparosObjetivo = disparosObjetivo;
    }

    @Override
    public void run() {
        while (disparosRealizados < disparosObjetivo) {
            // Buscar transiciones habilitadas
            java.util.List<Integer> habilitadas = new java.util.ArrayList<>();
            for (int t : posiblesTransiciones) {
                if (monitor.isEnabled(t)) {
                    habilitadas.add(t);
                }
            }
            int[] habilitadasArr = habilitadas.stream().mapToInt(Integer::intValue).toArray();
            int seleccionada = policy.selectTransition(habilitadasArr);
            if (seleccionada != -1) {
                boolean fired = monitor.fireTransition(seleccionada);
                if (fired) {
                    disparosRealizados++;
                    // Si la transición es temporal, aplicar sleep
                    if (isTemporalTransition(seleccionada)) {
                        try { Thread.sleep(getTransitionTime(seleccionada)); } catch (InterruptedException e) { break; }
                    }
                }
            } else {
                // Si no hay transiciones habilitadas, el hilo puede dormir un poco para evitar busy-wait
                try { Thread.sleep(1); } catch (InterruptedException e) { break; }
            }
        }
    }

    // IDs de transiciones temporales
    private boolean isTemporalTransition(int transitionId) {
        return transitionId == 1 || transitionId == 3 || transitionId == 4 || transitionId == 6 || transitionId == 8 || transitionId == 9 || transitionId == 10;
    }

    // Asigna tiempo a cada transición temporal (puedes ajustar los valores)
    private int getTransitionTime(int transitionId) {
        switch (transitionId) {
            case 1: return 100; // T1
            case 3: return 120; // T3
            case 4: return 150; // T4
            case 6: return 110; // T6
            case 8: return 130; // T8
            case 9: return 140; // T9
            case 10: return 160; // T10
            default: return 0;
        }
    }
}
