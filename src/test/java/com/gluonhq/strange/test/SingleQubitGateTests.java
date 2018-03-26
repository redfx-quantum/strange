package com.gluonhq.strange.test;

import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Hadamard;
import com.gluonhq.strange.gate.Identity;
import com.gluonhq.strange.gate.X;
import com.gluonhq.strange.gate.Y;
import com.gluonhq.strange.gate.Z;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SingleQubitGateTests {

    @Test
    public void empty() {
    }
        
    @Test
    public void simpleIGate() {
        Program p = new Program(1);
        Step s = new Step();
        s.addGate(new Identity(0));
        p.addStep(s);
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result res = sqee.runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertTrue(qubits[0].measure()==0);
    }    
    
    @Test
    public void simpleXGate() {
        Program p = new Program(1);
        Step s = new Step();
        s.addGate(new X(0));
        p.addStep(s);
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result res = sqee.runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertTrue(qubits[0].measure()==1);
    }    
    
    @Test
    public void simpleYGate() {
        Program p = new Program(1);
        Step s = new Step();
        s.addGate(new Y(0));
        p.addStep(s);
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result res = sqee.runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertTrue(qubits[0].measure()==1);
    }
    
    @Test
    public void simpleZGate() {
        Program p = new Program(1);
        Step s = new Step();
        s.addGate(new Z(0));
        p.addStep(s);
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result res = sqee.runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertTrue(qubits[0].measure()==0);
    }   
        
    @Test
    public void simpleHGate() {
        int[] results = new int[2];
        for (int i = 0; i < 100; i++) {
            Program p = new Program(1);
            Step s = new Step();
            s.addGate(new Hadamard(0));
            p.addStep(s);
            SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
            Result res = sqee.runProgram(p);
            Qubit[] qubits = res.getQubits();
            results[qubits[0].measure()]++;
        }
        System.out.println("res0 = "+results[0]+" and res1 = "+results[1]);
        assertTrue(results[0] > 10);
        assertTrue(results[1] > 10);
    }   
    
    @Test
    public void simpleTogetherGate() {
        Program p = new Program(4);
        Step s = new Step();
        s.addGate(new X(0));
        s.addGate(new Y(1));
        s.addGate(new Z(2));
        s.addGate(new Identity(3));
        p.addStep(s);
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result res = sqee.runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertTrue(qubits[0].measure()==1);
        assertTrue(qubits[1].measure()==1);
        assertTrue(qubits[2].measure()==0);
        assertTrue(qubits[3].measure()==0);
    }

}
