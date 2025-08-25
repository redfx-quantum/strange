package org.redfx.strange.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.AddInteger;
import org.redfx.strange.gate.AddIntegerModulus;
import org.redfx.strange.gate.X;
import static org.redfx.strange.test.BaseGateTests.DELTA;

/**
 *
 * @author johan
 */
public class AddIntegerTests extends BaseGateTests {
    
    @Test
    public void addmodp0() {
        int N = 1;
        int dim = 2;
        Program p = new Program(dim);
        AddInteger min = new AddInteger(0,1,N).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
    }

    @Test
    public void add0num0() {
        Program p = new Program(1);
        Step prep = new Step();
        p.addStep(prep);
        AddInteger add = new AddInteger(0,0,0);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(1, q.length);
        assertEquals(0, q[0].measure());
    }
           
    @Test
    public void add0num1() {
        Program p = new Program(1);
        Step prep = new Step();
        p.addStep(prep);
        AddInteger add = new AddInteger(0,0,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(1, q.length);
        assertEquals(1, q[0].measure());  
    }
    
    @Test
    public void add1num0() {
        Program p = new Program(1);
        Step prep = new Step();
        prep.addGate(new X(0));
        p.addStep(prep);
        AddInteger add = new AddInteger(0,0,0);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(1, q.length);
        assertEquals(1, q[0].measure());  
    }

    @Test
    public void add1num1() {
        Program p = new Program(1);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        AddInteger add = new AddInteger(0,0,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(1, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[0].measure());
    }

    @Test
    public void add01num1() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        AddInteger add = new AddInteger(0,1,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
    }
 
    @Test
    public void add00num1() {
        Program p = new Program(2);
        Step prep = new Step();
        p.addStep(prep);
        AddInteger add = new AddInteger(0,1,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
    }
 
    @Test
    public void min00num1() { // 2 -1 = 1
        Program p = new Program(2);
        Step prep = new Step();
        p.addStep(prep);
        AddInteger min = new AddInteger(0,1,1).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        Complex[] probs = result.getProbability();
        assertEquals(0, probs[0].abssqr(), DELTA);
        assertEquals(0, probs[1].abssqr(), DELTA);
        assertEquals(0, probs[2].abssqr(), DELTA);
        assertEquals(1, probs[3].abssqr(), DELTA);
    }

    @Test
    public void min01num1() { // 1 -1 = 0
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        AddInteger min = new AddInteger(0,1,1).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        Complex[] probs = result.getProbability();
        assertEquals(1, probs[0].abssqr(), DELTA);
        assertEquals(0, probs[1].abssqr(), DELTA);
        assertEquals(0, probs[2].abssqr(), DELTA);
        assertEquals(0, probs[3].abssqr(), DELTA);
    }
    
    
    @Test
    public void min10num1() { // 2 -1 = 1
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(1));
        p.addStep(prep);
        AddInteger min = new AddInteger(0,1,1).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        Complex[] probs = result.getProbability();
        assertEquals(0, probs[0].abssqr(), DELTA);
        assertEquals(1, probs[1].abssqr(), DELTA);
        assertEquals(0, probs[2].abssqr(), DELTA);
        assertEquals(0, probs[3].abssqr(), DELTA);
    }       

    @Test
    public void add5num2() {
        Program p = new Program(3);
        Step prep = new Step();
        prep.addGates(new X(0), new X(2));
        p.addStep(prep);
        AddInteger add = new AddInteger(0,2,2);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Complex[] probs = result.getProbability();
        assertEquals(1, probs[7].abssqr(), DELTA);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
    }
                
           
    @Test
    public void add5num2p() {
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(1), new X(3));
        p.addStep(prep);
        AddInteger add = new AddInteger(1,3,2);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
        assertEquals(1, q[3].measure());
        Complex[] probs = result.getProbability();
        assertEquals(1, probs[14].abssqr(), DELTA);
    }
    
    @Test
    public void add5num4() {
        Program p = new Program(3);
        Step prep = new Step();
        prep.addGates(new X(0), new X(2));
        p.addStep(prep);
        AddInteger add = new AddInteger(0,2,4);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
    }
        
    @Test
    public void add0num3() {
        Program p = new Program(3);
        AddInteger add = new AddInteger(0,2,3);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(0, q[2].measure());
    }

    @Test
    public void add0num3_4() {
        Program p = new Program(4);
        AddInteger add = new AddInteger(0,3,3);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
    }
          
    @Test
    public void addmod11num4() {
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        AddIntegerModulus add = new AddIntegerModulus(0,1,1,3);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
    }  
}
