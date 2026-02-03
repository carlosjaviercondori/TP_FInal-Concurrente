/**
 * Main class to run the Petri net simulation.
 */
public class Main {
    public static void main(String[] args) {
        PetriNet petriNet = PetriNetInitializer.createPetriNet();
        Monitor monitor = new Monitor(petriNet);

        // Elige la política a usar (puedes cambiar entre RandomPolicy y PrioritizedPolicy)
         Policy policy = new RandomPolicy(); // Política aleatoria
        //Policy policy = new PrioritizedPolicy(7); // Política priorizada (ejemplo: transición 7 es modo simple)
        
        System.out.println("Estado inicial de las plazas:");
        for (Place place : petriNet.getPlaces().values()) {
            System.out.println("P" + place.getId() + ": " + place.getTokens() + " tokens");
        }

        // Lanzar varios hilos Worker
        int numHilos = 3; // Puedes ajustar según tu análisis
        int disparosPorHilo = 200;
        int[] todasLasTransiciones = petriNet.getTransitions().keySet().stream().mapToInt(Integer::intValue).toArray();
        Thread[] hilos = new Thread[numHilos];
        for (int i = 0; i < numHilos; i++) {
            hilos[i] = new Thread(new Worker(monitor, policy, todasLasTransiciones, disparosPorHilo), "Worker-" + i);
            hilos[i].start();
        }
        // Esperar a que terminen
        for (Thread hilo : hilos) {
            try { hilo.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        System.out.println("\nSimulación concurrente finalizada.");
        System.out.println("Estado final de las plazas:");
        for (Place place : petriNet.getPlaces().values()) {
            System.out.println("P" + place.getId() + ": " + place.getTokens() + " tokens");
        }
    }
}
