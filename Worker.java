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
                    // Puedes agregar aquí un pequeño sleep si quieres simular tiempo de procesamiento
                }
            } else {
                // Si no hay transiciones habilitadas, el hilo puede dormir un poco para evitar busy-wait
                try { Thread.sleep(1); } catch (InterruptedException e) { break; }
            }
        }
    }
}
