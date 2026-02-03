/**
 * Main class to run the Petri net simulation.
 */
public class Main {
    public static void main(String[] args) {
        PetriNet petriNet = PetriNetInitializer.createPetriNet();
        Monitor monitor = new Monitor(petriNet);

        // Select policy
         Policy policy = new RandomPolicy(); // Random policy
        //Policy policy = new PrioritizedPolicy(7); // Prioritized policy (example: transition 7 is simple mode)
        
        System.out.println("Initial state of places:");
        for (Place place : petriNet.getPlaces().values()) {
            System.out.println("P" + place.getId() + ": " + place.getTokens() + " tokens");
        }

        
        int numHilos = 3; // You can adjust according to your analysis
        int disparosPorHilo = 200;
        int[] todasLasTransiciones = petriNet.getTransitions().keySet().stream().mapToInt(Integer::intValue).toArray();
        Thread[] hilos = new Thread[numHilos];
        for (int i = 0; i < numHilos; i++) {
            hilos[i] = new Thread(new Worker(monitor, policy, todasLasTransiciones, disparosPorHilo), "Worker-" + i);
            hilos[i].start();
        }
        
        for (Thread hilo : hilos) {
            try { hilo.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        System.out.println("\nConcurrent simulation completed.");
        System.out.println("Final state of places:");
        for (Place place : petriNet.getPlaces().values()) {
            System.out.println("P" + place.getId() + ": " + place.getTokens() + " tokens");
        }
    }
}
