# Final Practical Project - Concurrent Programming

## Concurrent Petri Net Simulator

This project implements a **Petri Net simulator** using Java with concurrent programming. The objective is to simulate the behavior of concurrent systems by triggering transitions in a Petri net, using multiple threads (workers) and a synchronized monitor.

---

## Table of Contents

1. [General Description](#general-description)
2. [Petri Net Concepts](#petri-net-concepts)
3. [Project Architecture](#project-architecture)
4. [Main Components](#main-components)
5. [Diagrams](#diagrams)
6. [Design Patterns](#design-patterns)
7. [Execution](#execution)

---

## General Description

### Purpose

To simulate the concurrent execution of a **Petri Net**, where multiple threads (Workers) trigger transitions simultaneously under the synchronized control of a Monitor. Execution respects the net's invariants and uses different **selection policies** to choose which transition to trigger.


### Features

- ✅ Concurrent execution of multiple workers
- ✅ Synchronization using Monitor (concurrent Java pattern)
- ✅ Configurable policies: Random and Prioritized
- ✅ Logging of all events
- ✅ Petri net invariant verification

---

## Petri Net Concepts

A **Petri Net** is a mathematical model for representing concurrent and distributed systems.

### Elements

- **Places**: Nodes that store tokens (P1, P2, etc.)
- **Transitions**: Actions that transfer tokens between places
- **Tokens**: Resources that move through the network
- **Arcs**: Connections between places and transitions

### Triggering Rule

A transition is **enabled** if:
- All input places have at least 1 token

When it triggers:
- **1 token** is removed from each input place
- **1 token** is added to each output place

---

## Project Architecture

### Folder Structure

```
Petri/
├── Place.java                   # Storage nodes (places)
├── Transition.java              # Actions (transitions)
├── PetriNet.java                # Main network structure
├── Monitor.java                 # Synchronized trigger control
├── MonitorInterface.java        # Monitor interface
├── Policy.java                  # Abstract class for policies
├── RandomPolicy.java            # Random selection
├── PrioritizedPolicy.java       # Prioritized selection
├── Worker.java                  # Thread that executes triggers
├── PetriNetInitializer.java     # Main initializer
├── VerificarInvariante.java     # Invariant verification
└── README.md                    # This file
```

---

## Main Components

### 1. **Place** - Storage Node

Represents a storage node on the network that holds tokens.

```java
public class Place {
    private final int id;      // Unique identifier
    private int tokens;        // Number of tokens

    public Place(int id, int tokens)
    public int getId()
    public int getTokens()
    public void setTokens(int tokens)
    public void addToken()     // Increment tokens
    public void removeToken()  // Decrement tokens
}
```

**Responsibilities:**
- Store and manage tokens
- Provide thread-safe methods for accessing tokens

---

### 2. **Transition** - Action Node

Represents an action that transfers tokens between places.

```java
public class Transition {
    private final int id;                      // Unique identifier
    private final List<Integer> inputPlaces;   // Input places
    private final List<Integer> outputPlaces;  // Output places

    public Transition(int id, List<Integer> inputPlaces, 
                      List<Integer> outputPlaces)
    public int getId()
    public List<Integer> getInputPlaces()
    public List<Integer> getOutputPlaces()
}
```

**Responsibilities:**
- Define relationships between places
- Store input and output information

---

### 3. **PetriNet** - Network Container

Main container that stores all places and transitions.

```java
public class PetriNet {
    private final Map<Integer, Place> places;           // All places
    private final Map<Integer, Transition> transitions; // All transitions

    public PetriNet(Map<Integer, Place> places, 
                    Map<Integer, Transition> transitions)
    public Place getPlace(int id)
    public Transition getTransition(int id)
    public Map<Integer, Place> getPlaces()
    public Map<Integer, Transition> getTransitions()
}
```

**Responsibilities:**
- Maintain the complete network structure
- Provide access to specific components

---

### 4. **MonitorInterface** - Synchronization Contract

Interface that defines the synchronization contract for the monitor.

```java
public interface MonitorInterface {
    boolean fireTransition(int transitionId);  // Trigger a transition
    boolean isEnabled(int transitionId);       // Check if enabled
}
```

---
### 5. **Monitor** - Synchronized Control

Implements the synchronized logic for triggering transitions. This is the **synchronization point** between workers.

```java
public class Monitor implements MonitorInterface {
    private final PetriNet petriNet;
    private FileWriter logWriter;

    public Monitor(PetriNet petriNet)
    
    public synchronized boolean fireTransition(int transitionId) {
        // 1. Check if the transition is enabled
        for (int placeId : transition.getInputPlaces()) {
            Place place = petriNet.getPlace(placeId);
            if (place.getTokens() == 0) return false;
        }
        
        // 2. Remove input tokens
        for (int placeId : transition.getInputPlaces()) {
            petriNet.getPlace(placeId).removeToken();
        }
        
        // 3. Add output tokens
        for (int placeId : transition.getOutputPlaces()) {
            petriNet.getPlace(placeId).addToken();
        }
        
        // 4. Record in log
        logTransition(transitionId);
        return true;
    }
    
    public synchronized boolean isEnabled(int transitionId)
    private void logTransition(int transitionId)
}
```

**Responsibilities:**
- Synchronize access to trigger transitions
- Verify transition enablement
- Log events
- Ensure network consistency

---

### 6. **Policy** - Selection Strategy

Abstract class that implements the **Strategy** pattern for selecting transitions.

```java
public abstract class Policy {
    public abstract int selectTransition(int[] enabledTransitions);
}
```

**Design Pattern:** Strategy
- Allows changing the selection algorithm at runtime
- Facilitates adding new policies without modifying the Worker

---

### 7. **RandomPolicy** - Random Selection

Selects a transition **randomly** from those available.

```java
public class RandomPolicy extends Policy {
    private final Random random = new Random();

    @Override
    public int selectTransition(int[] enabledTransitions) {
        if (enabledTransitions.length == 0) return -1;
        return enabledTransitions[random.nextInt(enabledTransitions.length)];
    }
}
```

---

### 8. **PrioritizedPolicy** - Priority-Based Selection

Prioritizes a specific transition if it is enabled; otherwise selects the first available.

```java
public class PrioritizedPolicy extends Policy {
    private final int priorityTransitionId;

    public PrioritizedPolicy(int priorityTransitionId)

    @Override
    public int selectTransition(int[] enabledTransitions) {
        // Find the priority transition
        for (int t : enabledTransitions) {
            if (t == priorityTransitionId) return t;
        }
        // If not found, return the first available
        return enabledTransitions.length > 0 ? enabledTransitions[0] : -1;
    }
}
```

---

### 9. **Worker** - Thread Executor

Class that implements `Runnable`. Each Worker executes transition triggers in its own thread.

```java
public class Worker implements Runnable {
    private final Monitor monitor;             // Synchronized access
    private final Policy policy;               // Selection strategy
    private final int[] posiblesTransiciones;  // Allowed transitions
    private final int disparosObjetivo;        // Target firings
    private int disparosRealizados = 0;        // Current counter

    public Worker(Monitor monitor, Policy policy, 
                  int[] transiciones, int disparos)

    @Override
    public void run() {
        while (disparosRealizados < disparosObjetivo) {
            // 1. Find enabled transitions
            List<Integer> habilitadas = new ArrayList<>();
            for (int t : posiblesTransiciones) {
                if (monitor.isEnabled(t)) {
                    habilitadas.add(t);
                }
            }

            // 2. If transitions are available
            if (!habilitadas.isEmpty()) {
                int[] arr = habilitadas.stream()
                    .mapToInt(Integer::intValue).toArray();

                // 3. Use policy to select
                int transitionSeleccionada = policy.selectTransition(arr);

                // 4. Attempt to fire
                if (monitor.fireTransition(transitionSeleccionada)) {
                    disparosRealizados++;
                }
            }
        }
    }
}
```

**Responsibilities:**
- Run in parallel (each Worker on its own thread)
- Look for enabled transitions
- Use Policy to select
- Update the firing counter
- Synchronize with Monitor

### 10. **PetriNetInitializer** - Network Factory

Factory class that creates and initializes the Petri Net structure.

```java
public class PetriNetInitializer {
    public static PetriNet createPetriNet() {
        // 1. Create places
        Place p1 = new Place(1, 5);   // Initial tokens
        Place p2 = new Place(2, 0);
        Place p3 = new Place(3, 0);

        // 2. Create transitions
        Transition t1 = new Transition(1,
            Arrays.asList(1),   // Input: P1
            Arrays.asList(2)    // Output: P2
        );

        // 3. Build the network
        Map<Integer, Place> places = new HashMap<>();
        places.put(1, p1);
        places.put(2, p2);
        places.put(3, p3);

        Map<Integer, Transition> transitions = new HashMap<>();
        transitions.put(1, t1);

        return new PetriNet(places, transitions);
    }
}
```

**Responsibilities:**
- Create and configure the Petri Net
- Initialize places with tokens
- Define transitions and their connections
- Centralize network configuration

---

## Diagrams

### Class Diagram

![Class Diagram](doc/class.png)

**Main Relationships:**
- `Worker` implements `Runnable` (concurrent pattern)
- `Monitor` implements `MonitorInterface` (synchronization contract)
- `Worker` uses `Monitor` and `Policy` (dependency injection)
- `RandomPolicy` and `PrioritizedPolicy` extend `Policy` (Strategy pattern)
- `PetriNet` contains `Place` and `Transition` (composition)

---

### Sequence Diagram

![Sequence Diagram](doc/sec.png)

**Execution Flow:**

1. **Initialization**: PetriNetInitializer creates PetriNet, Monitor, and Workers
2. **Search**: Worker searches for enabled transitions via `isEnabled()`
3. **Selection**: Policy selects a transition from the enabled set
4. **Synchronized Firing**: Monitor synchronizes the `fireTransition()` call
5. **Update**: Input tokens are removed, output tokens are added
6. **Logging**: The event is logged to `petri_log.txt`
7. **Repetition**: Cycle continues until target firings reached

---

## Design Patterns

### 1. **Strategy Pattern**
- **Location**: Policy, RandomPolicy, PrioritizedPolicy
- **Benefit**: Allows changing the selection algorithm without modifying Worker
- **Use Case**: Easy to implement different firing strategies

### 2. **Monitor Pattern**
- **Location**: Monitor class with `synchronized` methods
- **Benefit**: Synchronizes access to shared resources (PetriNet)
- **Use Case**: Prevents race conditions in concurrent access

### 3. **Dependency Injection**
- **Location**: Worker receives Monitor and Policy in constructor
- **Benefit**: Facilitates testing and flexibility
- **Use Case**: Decouples components and improves maintainability

### 4. **Template Method**
- **Location**: `run()` method in Worker follows a fixed pattern
- **Benefit**: Clear and predictable structure
- **Use Case**: Standardizes the execution flow across workers

---

## Execution

### Compilation

```bash
cd Petri
javac *.java
```

### Execution

The simulation runs from the **Main** class:

```bash
java Main
```

### Configuration in Main.java

You can adjust the simulation parameters:

```java
// Choose policy (RandomPolicy or PrioritizedPolicy)
Policy policy = new RandomPolicy();              // Random selection
// Policy policy = new PrioritizedPolicy(7);    // Prioritized selection

int numHilos = 3;              // Number of worker threads
int disparosPorHilo = 200;     // Firings per thread
```

### Sample Output

```
Initial state of places:
P1: 5 tokens
P2: 0 tokens
P3: 0 tokens

Concurrent simulation completed.
Final state of places:
P1: 0 tokens
P2: 3 tokens
P3: 2 tokens
```

The complete execution log is saved to **petri_log.txt** with timestamps and all firing events.

---

## Verification of Invariants

The `VerificarInvariante.java` class verifies that network invariants are maintained throughout execution:

```java
public class VerificarInvariante {
    public static void main(String[] args) {
        // Read petri_log.txt
        // Reconstruct state for each event
        // Verify that the sum of tokens remains constant
    }
}
```

---

## Conclusion


---
