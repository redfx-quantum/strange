package org.redfx.strange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to manage names of qubits and the mapping to the qubuit index.
 */
public class Qubits<T> {
  
  private Map<T, Integer> qubits = new HashMap<>();

  /**
   * Initializes with the provided Qubit keys.
   *
   * @param keys the initial qubit keys
   * @throws IllegalArgumentException if there are duplicate keys
   */
  public Qubits(T... keys) {
    for (T key : keys) {
      newBit(key);
    }
  }

  /**
   * Gets the number of qubit keys.
   *
   * @return the number of qubits
   */
  public int numberOfQubits() {
    return qubits.size();
  }

  /**
   * Returns a list of the qubit keys.
   *
   * @return the list of qubit keys
   */
  public List<T> qubitKeys() {
    return List.copyOf(qubits.keySet());
  }

  /**
   * Registers a new qubit key and returns its index.
   *
   * @param key the key for the new qubit
   * @return the index of the new qubit
   * @throws IllegalArgumentException if the key already exists
   */
  public int newBit(T key) {
    if (qubits.containsKey(key)) {
      throw new IllegalArgumentException("Qubit " + key + " already exists, use bit(key) to retrieve it.");
    }
    int index = qubits.size();
    qubits.put(key, index);
    return index;
  }

  /**
   * Returns the index of the qubit for the given key.
   *
   * @param key the key for the qubit
   * @return the index of the qubit
   * @throws IllegalArgumentException if the key does not exist
   */
  public int bit(T key) {
    if (! qubits.containsKey(key)) {
      throw new IllegalArgumentException("Qubit " + key + " isn't known, use newBit(key) to create it first.");
    }
    return qubits.get(key);
  }

  /**
   * Creates a new Program with these qubits and the given steps.
   *
   * @param steps the steps to be included in the program
   * @return a new Program instance containing the specified steps
   */
  public Program programOf(Step... steps) {
    return new Program(this.numberOfQubits(), steps);
  }

  /**
   * <p>Getter for the field <code>qubits</code>.</p>
   *
   * @return an array of {@link org.redfx.strange.Qubit} objects
   */
  public Map<T, Qubit> getQubits(Result result) {
    Qubit[] qubits = result.getQubits();
    Map<T, Qubit> qubitMap = new HashMap<>();
    for (T key : qubitKeys()) {
        qubitMap.put(key, qubits[bit(key)]);
    }
    return qubitMap;
  }

  // Factory methods for gates

  public Gate cnot(T a, T b) { return Gate.cnot(bit(a), bit(b)); }
  public Step cnotStep(T a, T b) { return new Step(cnot(a, b)); }

  public Gate cz(T a, T b) { return Gate.cz(bit(a), bit(b)); }
  public Step czStep(T a, T b) { return new Step(cz(a, b)); }

  public Gate hadamard(T q) { return Gate.hadamard(bit(q)); }
  public Step hadamardStep(T q) { return new Step(hadamard(q)); }

  // ...
}
