package org.redfx.strange.test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.AddModulus;
import org.redfx.strange.gate.Swap;
import org.redfx.strange.gate.X;
import org.redfx.strange.local.Computations;

/**
 *
 * @author johan
 */
public class AddModulusTests extends BaseGateTests {

    @Test
    public void addmodgate2q() {
        int n = 1;
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(1), new X(3));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,1,2,3, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(5, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(1, q[3].measure());
        assertEquals(0, q[4].measure());
    }

    @Test
    public void addmodgate2q2() {
        int n = 1;
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(1), new X(2));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,1,2,3, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(5, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(1, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(0, q[4].measure());
    }

    @Test
    public void addmodgate2q3() {
        int n = 1;
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(0), new X(2));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,1,2,3, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(5, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(0, q[4].measure());
    }

    @Test
    public void addmodgate2q4() {
        int n = 1;
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(2));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,1,2,3, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        Complex[] probs = result.getProbability();
        System.err.println("2Q4 PROBS = "+Arrays.toString(probs));
        assertEquals(5, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(1, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(0, q[4].measure());
    }

    // @Test invalid as this is 3 + 1 mod 3 with x >= 3
    public void addmodgate2q5() {
        int n = 1; 
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(0), new X(1), new X(2));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,1,2,3, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        System.err.println("2Q5 PROBS = "+Arrays.toString(result.getProbability()));
        assertEquals(5, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(0, q[4].measure());
    }

    @Test
    public void addmodgate2q6step0() {
        int n = 1;
        int N = 2;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,1,2,3, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(5, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(0, q[4].measure());
    }

    @Test
    public void addmodgate() {
        int n = 2;
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(1), new X(4));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,2,3,5, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(7, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(1, q[4].measure());
        assertEquals(0, q[5].measure());
        assertEquals(0, q[6].measure());
    }

    //@Test //invalid as this is (2 + 4) mod 3 with 4 >3
    public void addmodgate2() {
        int n = 2;
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
        prep.addGates(new X(1), new X(5));
        p.addStep(prep);
        
        Step mod = new Step(new AddModulus(0,2,3,5, N));
        p.addStep(mod);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(7, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(1, q[4].measure());
        assertEquals(1, q[5].measure());
        assertEquals(0, q[6].measure());
    }

    @Test
    public void multiplyMod5x3andswapandcleans1() { // 5 x 3 mod 6 = 3
        Program p = new Program(9);
        Step prep = new Step();
        int mul = 5;
        int N = 6;
        prep.addGates(new X(4), new X(5)); // 3 in high register
        p.addStep(prep);
        for (int i = 0; i < mul; i++) {
            AddModulus add = new AddModulus(0, 3, 4, 7, N);
            p.addStep(new Step(add));
        }
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(9, q.length);
        assertEquals(1, q[0].measure()); // q2,q1,q0,q3 should be clean
        assertEquals(1, q[1].measure());  
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(1, q[4].measure()); // result in q4,q5,q6,q7
        assertEquals(1, q[5].measure());
        assertEquals(0, q[6].measure());  
        assertEquals(0, q[7].measure());  
        assertEquals(0, q[8].measure());  
    }
    
    @Test
    public void multiplyMod5x3andswapandcleans2() { // 5 x 3 mod 6 = 3
        // |A00110000> -> 
        Program p = new Program(9);
        Step prep = new Step();
        int mul = 5;
        int N = 6;
        prep.addGates(new X(4), new X(5)); // 3 in high register
        p.addStep(prep);
        for (int i = 0; i < 1; i++) {
            AddModulus add = new AddModulus(0, 3, 4, 7, N);
            p.addStep(new Step(add));
        }
        p.addStep(new Step( new Swap(0,4)));
        p.addStep(new Step( new Swap(1,5)));
        p.addStep(new Step( new Swap(2,6)));
        p.addStep(new Step( new Swap(3,7)));

        int invsteps = Computations.getInverseModulus(mul,N);
        for (int i = 0; i < invsteps; i++) {
            AddModulus addModulus = new AddModulus(0, 3, 4, 7, N).inverse();
            p.addStep(new Step(addModulus));
        }
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(9, q.length);
        assertEquals(0, q[0].measure()); // q2,q1,q0,q3 should be clean
        assertEquals(0, q[1].measure());  
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(1, q[4].measure()); // result in q4,q5,q6,q7
        assertEquals(1, q[5].measure());
        assertEquals(0, q[6].measure());  
        assertEquals(0, q[7].measure());  
        assertEquals(0, q[8].measure());  
    }
    
}
