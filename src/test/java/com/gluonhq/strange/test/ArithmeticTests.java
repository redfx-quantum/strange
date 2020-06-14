/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.strange.test;


import com.gluonhq.strange.Complex;
import com.gluonhq.strange.ControlledBlockGate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Add;
import com.gluonhq.strange.gate.Fourier;
import com.gluonhq.strange.gate.Mul;
import com.gluonhq.strange.gate.Swap;
import com.gluonhq.strange.gate.X;
import com.gluonhq.strange.local.Computations;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author johan
 */
public class ArithmeticTests extends BaseGateTests {

    static final double D = 0.000000001d;

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
    public void adjoint() {
           Program p = new Program(6);
        Step prep = new Step();
        prep.addGates(new X(1), new X(2), new X(3));
        p.addStep(prep);
        Add add = new Add(0,2,3,5);
        p.addStep(new Step(add));
        Add min = new Add(0,2,3,5).inverse();
        p.addStep(new Step(min));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());  
        assertEquals(1, q[2].measure());
        assertEquals(1, q[3].measure()); 
        assertEquals(0, q[4].measure());
        assertEquals(0, q[5].measure());  
    }
    
    @Test
    public void multiply5x3() { // 5 x 3 mod 8 = 7
        Program p = new Program(6);
        Step prep = new Step();
        prep.addGates(new X(3), new X(4)); // 5 in second register
        p.addStep(prep);
        for (int i = 0; i < 5; i++) {
            Add add = new Add(0, 2, 3, 5);
            p.addStep(new Step(add));
        }
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        assertEquals(1, q[0].measure()); // result in q2,q1,q0
        assertEquals(1, q[1].measure());  
        assertEquals(1, q[2].measure());
        assertEquals(1, q[3].measure()); 
        assertEquals(1, q[4].measure());
        assertEquals(0, q[5].measure());  
    }
      
   @Test
    public void multiply5x7() { // 5 x 7 mod 8 = 3
        Program p = new Program(6);
        Step prep = new Step();
        prep.addGates(new X(3), new X(4), new X(5));
        p.addStep(prep);
        for (int i = 0; i < 5; i++) {
            Add add = new Add(0, 2, 3, 5);
            p.addStep(new Step(add));
        }
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());  
        assertEquals(0, q[2].measure());
        assertEquals(1, q[3].measure()); 
        assertEquals(1, q[4].measure());
        assertEquals(1, q[5].measure());  
    }
    
    @Test
    public void multiply5x3andswap() { // 5 x 3 mod 8 = 7
        Program p = new Program(6);
        Step prep = new Step();
        prep.addGates(new X(3), new X(4)); // 5 in second register
        p.addStep(prep);
        for (int i = 0; i < 5; i++) {
            Add add = new Add(0, 2, 3, 5);
            p.addStep(new Step(add));
        }
        p.addStep(new Step( new Swap(0,3)));
        p.addStep(new Step( new Swap(1,4)));
        p.addStep(new Step( new Swap(2,5)));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        assertEquals(1, q[0].measure()); // result in q2,q1,q0
        assertEquals(1, q[1].measure());  
        assertEquals(0, q[2].measure());
        assertEquals(1, q[3].measure()); 
        assertEquals(1, q[4].measure());
        assertEquals(1, q[5].measure());  
    }
    
    @Test
    public void getInverseModulus() {
        int answer = Computations.getInverseModulus(56, 69);
        assertEquals(53, answer);
    }
         
    @Test
    public void multiply5x3andswapandclean() { // 5 x 3 mod 8 = 7
        Program p = new Program(6);
        Step prep = new Step();
        int mul = 5;
        prep.addGates(new X(3), new X(4)); // 3 in high register
        p.addStep(prep);
        for (int i = 0; i < mul; i++) {
            Add add = new Add(0, 2, 3, 5);
            p.addStep(new Step(add));
        }
        p.addStep(new Step( new Swap(0,3)));
        p.addStep(new Step( new Swap(1,4)));
        p.addStep(new Step( new Swap(2,5)));

        int invsteps = Computations.getInverseModulus(mul,8);
        for (int i = 0; i < invsteps; i++) {
            Add add = new Add(0, 2, 3, 5).inverse();
            p.addStep(new Step(add));
        }
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        assertEquals(0, q[0].measure()); // q2,q1,q0 should be clean
        assertEquals(0, q[1].measure());  
        assertEquals(0, q[2].measure());
        assertEquals(1, q[3].measure()); // result in q3,q4,q5
        assertEquals(1, q[4].measure());
        assertEquals(1, q[5].measure());  
    }
    
    @Test 
    public void mul01() {
        Program p = new Program(2);
        Step s = new Step(new Mul(0,0,1));
        p.addStep(s);
          Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
    }
        
    @Test 
    public void mul11() {
        Program p = new Program(2);
        Step prep = new Step();
        prep.addGates(new X(1));
        Step s = new Step(new Mul(0,0,1));
        p.addStep(prep);
        p.addStep(s);
          Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
    }
            
   @Test 
    public void mul13() { // 0100 -> 1100
        Program p = new Program(4);
        Step prep = new Step();
        prep.addGates(new X(2));
        Step s = new Step(new Mul(0,1,3));
        p.addStep(prep);
        p.addStep(s);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        System.err.println("results: ");
        for (int i = 0; i < 4; i++) {
            System.err.println("m["+i+"]: "+q[i].measure());
        }
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(1, q[2].measure());
        assertEquals(1, q[3].measure());
    }
    
                
    @Test 
    public void mul55() { // 101000 -> 001000
        Program p = new Program(6);
        Step prep = new Step();
        prep.addGates(new X(3), new X(5));
        Step s = new Step(new Mul(0,2,5));
        p.addStep(prep);
        p.addStep(s);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        System.err.println("results: ");
        for (int i = 0; i < 6; i++) {
            System.err.println("m["+i+"]: "+q[i].measure());
        }
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(1, q[3].measure());
        assertEquals(0, q[4].measure());
        assertEquals(0, q[5].measure());
    }
    
    @Test
    public void controlledAdd001() {
        Program p = new Program(3);
        Step s = new Step();
        Add add = new Add(1,1,2,2);
        ControlledBlockGate cbg = new ControlledBlockGate(add, 1,0);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        Step cg = new Step(cbg);
        p.addStep(cg);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
    }
     
      
    @Test // q_0 = q_1(0) + q_0 (0) = 0
    public void controlledAdd100() {
        Program p = new Program(3);
        Step s = new Step();
        Add add = new Add(1,1,2,2);
        ControlledBlockGate cbg = new ControlledBlockGate(add, 1,0);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);
        Step prep = new Step();
        prep.addGates(new X(2));
        p.addStep(prep);
        Step cg = new Step(cbg);
        p.addStep(cg);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(1, q[2].measure());
    }
    
    @Test // q_0 = q_1(0) + q_0 (1) = 1
    public void controlledAdd101() {
        Program p = new Program(3);
        Step s = new Step();
        Add add = new Add(1,1,2,2);
        ControlledBlockGate cbg = new ControlledBlockGate(add, 1,0);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);
        Step prep = new Step();
        prep.addGates(new X(0), new X(2));
        p.addStep(prep);
        Step cg = new Step(cbg);
        p.addStep(cg);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(1, q[2].measure());
    }
    
    @Test // q_0 = q_1(1) + q_0 (0) = 1
    public void controlledAdd110() {
        Program p = new Program(3);
        Step s = new Step();
        Add add = new Add(1,1,2,2);
        ControlledBlockGate cbg = new ControlledBlockGate(add, 1,0);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);
        Step prep = new Step();
        prep.addGates(new X(1), new X(2));
        p.addStep(prep);
        Step cg = new Step(cbg);
        p.addStep(cg);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
    }
           
    @Test // q_0 = q_1(1) + q_0 (1) = 0
    public void controlledAdd111() {
        Program p = new Program(3);
        Step s = new Step();
        Add add = new Add(1,1,2,2);
        ControlledBlockGate cbg = new ControlledBlockGate(add, 1,0);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);
        Step prep = new Step();
        prep.addGates(new X(0), new X(1), new X(2));
        p.addStep(prep);
        Step cg = new Step(cbg);
        p.addStep(cg);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
    }
    
   // @Test // q_0 = q_1(1) + q_0 (1) = 0
    public void expmul() {
        int length = 2;
        int a = 3;
        Program p = new Program(3 * length);
        Step prepAnc = new Step(new X(3 * length -1));
        p.addStep(prepAnc);
        for (int i = 0; i < length; i++) {
            int m = a << (length -1 - i); // 2^(n-1-i)
            Mul mul = new Mul(length, 2 * length-1, a);
            if (i < length -1) {
                Swap swap = new Swap(i, length-1);
                p.addStep(new Step(swap));
            }
            ControlledBlockGate cbg = new ControlledBlockGate(mul, length, length -1);
            p.addStep(new Step(cbg));
            if (i < length -1) {
                Swap swap = new Swap(i, length-1);
                p.addStep(new Step(swap));
            }
        }
                Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(6, q.length);
        System.err.println("results: ");
        for (int i = 0; i < 6; i++) {
            System.err.println("m["+i+"]: "+q[i].measure());
        }
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(1, q[3].measure());
        assertEquals(0, q[4].measure());
        assertEquals(0, q[5].measure());
    }
    
}
