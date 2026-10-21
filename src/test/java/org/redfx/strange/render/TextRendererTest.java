package org.redfx.strange.render;

import org.junit.jupiter.api.Test;
import org.redfx.strange.*;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.X;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class TextRendererTest {

    private static String capture(Program p, Result r) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        new TextRenderer(ps).render(p, r);
        return baos.toString(StandardCharsets.UTF_8);
    }

    @Test
    public void singleQubitCircuit() {
        Program p = new Program(1, new Step(new Hadamard(0)));
        String out = capture(p, null);
        assertTrue(out.contains("H"), "Expected 'H' in output:\n" + out);
        assertTrue(out.contains("q[0]"), "Expected qubit label in output");
    }

    @Test
    public void bellStateCircuit() {
        Program p = new Program(2,
            new Step(new Hadamard(0)),
            new Step(new Cnot(0, 1))
        );
        String out = capture(p, null);
        // Control qubit 0 should show ● in step 1
        String[] lines = out.lines().toArray(String[]::new);
        String q0line = lines[1]; // "q[0]: ..."
        String q1line = lines[2]; // "q[1]: ..."
        assertTrue(q0line.contains("●"), "q[0] row should contain control dot:\n" + out);
        assertTrue(q1line.contains("X"), "q[1] row should contain X (CNOT target):\n" + out);
    }

    @Test
    public void withResult() {
        Program p = new Program(2,
            new Step(new Hadamard(0)),
            new Step(new Cnot(0, 1))
        );
        Result r = new SimpleQuantumExecutionEnvironment().runProgram(p);
        String out = capture(p, r);
        assertTrue(out.contains("Result:"), "Expected result section:\n" + out);
        assertTrue(out.contains("prob"), "Expected probability values:\n" + out);
        assertTrue(out.contains("State vector"), "Expected state vector section:\n" + out);
    }

    @Test
    public void nullResultNoException() {
        Program p = new Program(2, new Step(new X(0)));
        assertDoesNotThrow(() -> capture(p, null));
    }

    @Test
    public void nonAdjacentCnotShowsConnector() {
        // Cnot(0, 3) spans qubits 0-3 — qubits 1 and 2 should show │ connectors
        Program p = new Program(4, new Step(new Cnot(0, 3)));
        String out = capture(p, null);
        assertTrue(out.contains("│"), "Expected vertical connector on intermediate qubits:\n" + out);
    }
}
