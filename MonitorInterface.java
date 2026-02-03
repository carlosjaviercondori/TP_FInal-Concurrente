/**
 * MonitorInterface.java
 * Defines the interface for the Monitor class that manages
 * the firing of transitions in a Petri net.
 */
public interface MonitorInterface {
    boolean fireTransition(int transition);
}
