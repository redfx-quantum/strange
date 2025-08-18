package org.redfx.strange.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Add;
import org.redfx.strange.gate.X;

/**
 *
 * @author johan
 */
public class AddTests extends BaseGateTests {
        
    @Test
    public void add00() {
        Program p = new Program(2);
        Step prep = new Step();
        p.addStep(prep);
        Add add = new Add(0,0,1,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());   
    }
    
     @Test
    public void add10() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGate(new X(0));
        p.addStep(prep);
        Add add = new Add(0,0,1,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());   
    }
         
        @Test
    public void add010() {
        Program p = new Program(3);
        Step prep = new Step();
        prep.addGate(new X(1));
        p.addStep(prep);
        Add add = new Add(1,1,2,2);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(0, q[2].measure());   
    }
    @Test
    public void add01() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGate(new X(1));
        p.addStep(prep);
        Add add = new Add(0,0,1,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());  
        Complex[] probs = result.getProbability();
        assertEquals(1, probs[3].abssqr(), DELTA);
    }
            
    @Test
    public void add11() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(0), new X(1));
        p.addStep(prep);
        Add add = new Add(0,0,1,1);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());   
    }
               
    @Test
    public void add61() {
        Program p = new Program(6);
        Step prep = new Step();
        prep.addGates(new X(1), new X(2), new X(3));
        p.addStep(prep);
        Add add = new Add(0,2,3,5);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());  
        assertEquals(1, q[2].measure());
        assertEquals(1, q[3].measure()); 
        assertEquals(0, q[4].measure());
        assertEquals(0, q[5].measure());  
    }
    
                   
    @Test
    public void add21() {
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(1), new X(2));
        p.addStep(prep);
        Add add = new Add(0,1,2,3);
        p.addStep(new Step(add));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());  
        assertEquals(1, q[2].measure());
        assertEquals(0, q[3].measure()); 
    }
                       
    @Test
    public void admin2part1() {
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(1), new X(2));
        p.addStep(prep);
        Add add = new Add(0,1,2,3);
        p.addStep(new Step(add));
//        Add min = new Add(0,1,2,3).inverse();
//        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());  
        assertEquals(1, q[2].measure());
        assertEquals(0, q[3].measure());  
    }

    @Test
    public void m0min0() {
        Program p = new Program(2);
        Add min = new Add(0,0,1,1).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
    }
    @Test
    public void m0min1() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(1));
        p.addStep(prep);
        Add min = new Add(0,0,1,1).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
    }

    @Test
    public void m1min0() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        Add min = new Add(0,0,1,1).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
    }
  
    @Test
    public void m1min1() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(0), new X(1));
        p.addStep(prep);
        Add min = new Add(0,0,1,1).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
    }

    @Test
    public void m3min0() {
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(0), new X(1));
        p.addStep(prep);
        Add min = new Add(0,1,2,3).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
    }

    @Test
    public void m3min1() {
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(0), new X(1), new X(2));
        p.addStep(prep);
        Add min = new Add(0,1,2,3).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
        assertEquals(0, q[3].measure());
    }

    @Test
    public void m3min2() {
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(0), new X(1), new X(3));
        p.addStep(prep);
        Add min = new Add(0,1,2,3).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(1, q[3].measure());
    }

}
