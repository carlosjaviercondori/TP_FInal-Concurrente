# Trabajo Práctico Final - Programación Concurrente

## Simulador de Redes de Petri Concurrentes

Este proyecto implementa un **simulador de Redes de Petri** utilizando Java con programación concurrente. El objetivo es simular el comportamiento de sistemas concurrentes mediante disparos de transiciones en una red de Petri, utilizando múltiples hilos (Workers) y un monitor sincronizado.

---

## Índice

1. [Descripción General](#descripción-general)
2. [Conceptos de Redes de Petri](#conceptos-de-redes-de-petri)
3. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
4. [Componentes Principales](#componentes-principales)
5. [Diagramas](#diagramas)
6. [Ejecución](#ejecución)

---

## Descripción General

### Propósito

Simular la ejecución de una **Red de Petri** de forma concurrente, donde múltiples threads (Workers) disparan transiciones simultáneamente bajo control sincronizado de un Monitor. La ejecución respeta los invariantes de la red y utiliza diferentes **políticas de selección** para elegir qué transición disparar.

### Características

- ✅ Ejecución concurrente de múltiples workers
- ✅ Sincronización mediante Monitor (patrón java concurrente)
- ✅ Políticas configurables: Random y Prioritized
- ✅ Logging de todos los eventos
- ✅ Verificación de invariantes en Petri

---

## Conceptos de Redes de Petri

Una **Red de Petri** es un modelo matemático para representar sistemas concurrentes y distribuidos.

### Elementos

- **Lugares (Places)**: Nodos que almacenan tokens (P1, P2, etc.)
- **Transiciones (Transitions)**: Acciones que transfieren tokens entre lugares
- **Tokens**: Recursos que se mueven por la red
- **Arcos**: Conexiones entre places y transitions

### Regla de Disparo

Una transición está **habilitada** si:
- Todos los lugares de entrada tienen al menos 1 token

Cuando se dispara:
- Se **quita 1 token** de cada lugar de entrada
- Se **agrega 1 token** a cada lugar de salida

---

## Arquitectura del Proyecto

### Estructura de Carpetas

```
Petri/
├── Place.java              # Nodos de almacenamiento (lugares)
├── Transition.java         # Acciones (transiciones)
├── PetriNet.java           # Estructura principal de la red
├── Monitor.java            # Control sincronizado de disparos
├── MonitorInterface.java   # Interfaz del monitor
├── Policy.java             # Clase abstracta para políticas
├── RandomPolicy.java       # Selección aleatoria
├── PrioritizedPolicy.java  # Selección prioritaria
├── Worker.java             # Thread que ejecuta disparos
├── PetriNetInitializer.java    # Inicializador principal
├── VerificarInvariante.java    # Verificación de invariantes
└── README.md               # Este archivo
```

---

## Componentes Principales

### 1. **Place (Lugar)**

Representa un nodo de almacenamiento en la red.

```java
public class Place {
    private final int id;        // Identificador único
    private int tokens;          // Cantidad de tokens
    
    // Métodos principales
    public int getTokens()       // Obtener tokens
    public void addToken()       // Agregar token
    public void removeToken()    // Quitar token
}
```

**Responsabilidades:**
- Almacenar y gestionar tokens
- Proporcionar métodos thread-safe para acceso a tokens

---

### 2. **Transition (Transición)**

Representa una acción en la red.

```java
public class Transition {
    private final int id;                     // Identificador único
    private final List<Integer> inputPlaces;  // Lugares de entrada
    private final List<Integer> outputPlaces; // Lugares de salida
    
    // Métodos principales
    public List<Integer> getInputPlaces()
    public List<Integer> getOutputPlaces()
}
```

**Responsabilidades:**
- Definir relaciones entre lugares
- Almacenar información de entrada y salida

---

### 3. **PetriNet (Red de Petri)**

Contenedor principal que almacena todas las places y transitions.

```java
public class PetriNet {
    private final Map<Integer, Place> places;           // Todos los lugares
    private final Map<Integer, Transition> transitions; // Todas las transiciones
    
    // Métodos principales
    public Place getPlace(int id)
    public Transition getTransition(int id)
    public Map<Integer, Place> getPlaces()
    public Map<Integer, Transition> getTransitions()
}
```

**Responsabilidades:**
- Mantener la estructura completa de la red
- Proporcionar acceso a componentes específicos

---

### 4. **MonitorInterface (Interfaz del Monitor)**

Interfaz que define el contrato de sincronización.

```java
public interface MonitorInterface {
    boolean fireTransition(int transitionId);  // Disparar una transición
    boolean isEnabled(int transitionId);       // Verificar si está habilitada
}
```

---

### 5. **Monitor (Monitor Sincronizado)**

Implementa la lógica sincronizada de disparo de transiciones. Es el **punto de sincronización** entre workers.

```java
public class Monitor implements MonitorInterface {
    private final PetriNet petriNet;
    private FileWriter logWriter;
    
    // Métodos sincronizados (synchronized)
    public synchronized boolean fireTransition(int transitionId) {
        // 1. Verificar si la transición está habilitada
        for (int placeId : transition.getInputPlaces()) {
            Place place = petriNet.getPlace(placeId);
            if (place.getTokens() == 0) 
                return false; // No habilitada
        }
        
        // 2. Quitar tokens de entrada
        for (int placeId : transition.getInputPlaces()) {
            petriNet.getPlace(placeId).removeToken();
        }
        
        // 3. Agregar tokens de salida
        for (int placeId : transition.getOutputPlaces()) {
            petriNet.getPlace(placeId).addToken();
        }
        
        // 4. Registrar en log
        logTransition(transitionId);
        return true;
    }
    
    public synchronized boolean isEnabled(int transitionId) {
        // Verificar si todos los lugares de entrada tienen tokens
    }
}
```

**Responsabilidades:**
- Sincronizar acceso al disparo de transiciones
- Verificar habilitación de transiciones
- Registrar eventos en log
- Garantizar consistencia de la red

---

### 6. **Policy (Estrategia de Selección)**

Clase abstracta que implementa el patrón **Strategy** para seleccionar transiciones.

```java
public abstract class Policy {
    public abstract int selectTransition(int[] enabledTransitions);
}
```

**Patrón Design:** Strategy
- Permite cambiar el algoritmo de selección en tiempo de ejecución
- Facilita agregar nuevas políticas sin modificar Worker

---

### 7. **RandomPolicy**

Selecciona una transición **aleatoriamente** de las disponibles.

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

### 8. **PrioritizedPolicy**

Prioriza una transición específica si está habilitada, sino selecciona la primera disponible.

```java
public class PrioritizedPolicy extends Policy {
    private final int priorityTransitionId;
    
    @Override
    public int selectTransition(int[] enabledTransitions) {
        // Buscar la transición prioritaria
        for (int t : enabledTransitions) {
            if (t == priorityTransitionId) 
                return t; // Prioridad encontrada
        }
        // Si no está, retornar la primera disponible
        return enabledTransitions.length > 0 ? enabledTransitions[0] : -1;
    }
}
```

---

### 9. **Worker (Thread Ejecutor)**

Clase que implementa `Runnable`. Cada Worker ejecuta disparos de transiciones en su propio hilo.

```java
public class Worker implements Runnable {
    private final Monitor monitor;           // Acceso sincronizado
    private final Policy policy;             // Estrategia de selección
    private final int[] posiblesTransiciones; // Transiciones permitidas
    private final int disparosObjetivo;      // Meta de disparos
    private int disparosRealizados = 0;      // Contador actual
    
    @Override
    public void run() {
        while (disparosRealizados < disparosObjetivo) {
            // 1. Buscar transiciones habilitadas
            List<Integer> habilitadas = new ArrayList<>();
            for (int t : posiblesTransiciones) {
                if (monitor.isEnabled(t)) {
                    habilitadas.add(t);
                }
            }
            
            // 2. Si hay transiciones disponibles
            if (!habilitadas.isEmpty()) {
                int[] arr = habilitadas.stream().mapToInt(Integer::intValue).toArray();
                
                // 3. Usar política para seleccionar
                int transitionSeleccionada = policy.selectTransition(arr);
                
                // 4. Intentar disparar
                if (monitor.fireTransition(transitionSeleccionada)) {
                    disparosRealizados++;
                }
            }
        }
    }
}
```

**Responsabilidades:**
- Ejecutar en paralelo (cada Worker en su propio hilo)
- Buscar transiciones habilitadas
- Usar Policy para seleccionar
- Actualizar contador de disparos
- Sincronizar con Monitor

---

### 10. **PetriNetInitializer**

Clase principal que inicializa y ejecuta la simulación.

```java
public class PetriNetInitializer {
    public static void main(String[] args) {
        // 1. Crear lugares (Places)
        Place p1 = new Place(1, 1);  // Lugar 1 con 1 token
        Place p2 = new Place(2, 0);
        Place p3 = new Place(3, 0);
        
        // 2. Crear transiciones (Transitions)
        Transition t1 = new Transition(1, 
            Arrays.asList(1),        // Entrada: P1
            Arrays.asList(2)         // Salida: P2
        );
        
        // 3. Crear la red
        Map<Integer, Place> places = new HashMap<>();
        places.put(1, p1); places.put(2, p2); places.put(3, p3);
        
        Map<Integer, Transition> transitions = new HashMap<>();
        transitions.put(1, t1);
        
        PetriNet petriNet = new PetriNet(places, transitions);
        
        // 4. Crear monitor
        Monitor monitor = new Monitor(petriNet);
        
        // 5. Crear workers con políticas
        Policy randomPolicy = new RandomPolicy();
        Worker worker1 = new Worker(monitor, randomPolicy, 
            new int[]{1, 2}, 5);  // Disparos objetivo: 5
        
        // 6. Ejecutar en hilos
        Thread t = new Thread(worker1);
        t.start();
        t.join();  // Esperar a que termine
        
        // 7. Verificar resultado
        System.out.println("Estado final: P1=" + p1.getTokens() + 
                         ", P2=" + p2.getTokens());
    }
}
```

---

## Diagramas

### Diagrama de Clases

![Diagrama de Clases](../../pruebas/class.png)

**Relaciones principales:**
- `Worker` implementa `Runnable` (patrón concurrente)
- `Monitor` implementa `MonitorInterface` (contrato de sincronización)
- `Worker` usa `Monitor` y `Policy` (inyección de dependencias)
- `RandomPolicy` y `PrioritizedPolicy` extienden `Policy` (patrón Strategy)
- `PetriNet` contiene `Place` y `Transition` (composición)

---

### Diagrama de Secuencia

![Diagrama de Secuencia](../../pruebas/sec.png)

**Flujo de ejecución:**

1. **Inicialización:** PetriNetInitializer crea PetriNet, Monitor y Workers
2. **Búsqueda:** Worker busca transiciones habilitadas (isEnabled)
3. **Selección:** Policy selecciona una transición del conjunto habilitado
4. **Disparo Sincronizado:** Monitor sincroniza el fireTransition
5. **Actualización:** Se quitan tokens de entrada, se agregan de salida
6. **Logging:** Se registra el evento
7. **Repetición:** Ciclo continúa hasta alcanzar disparosObjetivo

---

## Patrones de Diseño

### 1. **Strategy Pattern**
- **Ubicación:** Policy, RandomPolicy, PrioritizedPolicy
- **Beneficio:** Permite cambiar el algoritmo de selección sin modificar Worker

### 2. **Monitor Pattern**
- **Ubicación:** Clase Monitor con métodos synchronized
- **Beneficio:** Sincroniza acceso a recursos compartidos (PetriNet)

### 3. **Dependency Injection**
- **Ubicación:** Worker recibe Monitor y Policy en constructor
- **Beneficio:** Facilita testing y flexibilidad

### 4. **Template Method (implícito)**
- **Ubicación:** Método run() en Worker sigue patrón repetitivo
- **Beneficio:** Estructura clara y predecible

---

## Ejecución

### Compilación

```bash
cd Petri
javac *.java
```

### Ejecución

```bash
java PetriNetInitializer
```

### Salida

```
Simulando 10 disparos con 3 workers...
Petri_log.txt: Registro de todos los eventos
Estado final: P1=2, P2=3, P3=5
```

---

## Verificación de Invariantes

La clase `VerificarInvariante.java` verifica que se mantienen los invariantes de la red:

```java
public class VerificarInvariante {
    public static void main(String[] args) {
        // Leer petri_log.txt
        // Reconstruir estado para cada evento
        // Verificar que la suma de tokens es invariante
    }
}
```

---

## Conclusión
