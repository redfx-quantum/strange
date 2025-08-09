package org.redfx.strange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.gate.Hadamard;

public class QubitsTest {
  
  @Test
  public void testProgramInitializedWithQubits() {
    Qubits<String> qs = new Qubits<>("q1", "q2");
    Program program = qs.programOf(
      qs.hadamardStep("q1"),
      qs.cnotStep("q2", "q1")
    );
    List<Step> steps = program.getSteps();
    assertEquals(2,  steps.size());

    Gate hadamardGate = steps.get(0).getGates().get(0);
    assertInstanceOf(Hadamard.class, hadamardGate);
    assertEquals(0, ((Hadamard) hadamardGate).getMainQubitIndex());

    Gate cnotGate = steps.get(1).getGates().get(0);
    assertInstanceOf(Cnot.class, cnotGate);
    assertEquals(1, ((Cnot) cnotGate).getControlQubitIndex());
    assertEquals(0, ((Cnot) cnotGate).getSecondQubitIndex());
  };
}
