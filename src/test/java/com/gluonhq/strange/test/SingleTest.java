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

import com.gluonhq.strange.Block;
import com.gluonhq.strange.BlockGate;
import com.gluonhq.strange.Complex;
import com.gluonhq.strange.ControlledBlockGate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Add;
import com.gluonhq.strange.gate.AddInteger;
import com.gluonhq.strange.gate.Cnot;
import com.gluonhq.strange.gate.Fourier;
import com.gluonhq.strange.gate.Mul;
import com.gluonhq.strange.gate.MulModulus;
import com.gluonhq.strange.gate.Swap;
import com.gluonhq.strange.gate.X;
import com.gluonhq.strange.local.Computations;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author johan
 */
public class SingleTest extends BaseGateTests {

  //  @Test // 
    public void expmul3p3() { // 3^3 = 27 -> mod 8 = 3
        int length = 3;
        int N = 8;
        // q0 -> q2: x (3)
        // q3 -> q5: ancilla (0 before, 0 after)
        // q6 -> q8: result
        int a = 3;
        Program p = new Program(3 * length);
        Step prep = new Step(new X(0), new X(1));
        Step prepAnc = new Step(new X(2 * length));
        p.addStep(prep);
        p.addStep(prepAnc);
        for (int i = length - 1; i > -1; i--) {
//            int m = (int) Math.pow(a, 1 << i);
            int m = 1;
            for (int j = 0; j < 1 << i; j++) {
                m = m*a %N;
            }
            System.err.println("M = " + m);
            Mul mul = new Mul(length, 2 * length - 1, m);
            ControlledBlockGate cbg = new ControlledBlockGate(mul, length, i);
            p.addStep(new Step(cbg));
        }
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(9, q.length);
        System.err.println("results: ");
        for (int j = 0; j < 9; j++) {
            System.err.println("m[" + j + "]: " + q[j].measure());
        }
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(0, q[4].measure());
        assertEquals(0, q[5].measure());
        assertEquals(1, q[6].measure());
        assertEquals(1, q[7].measure());
        assertEquals(0, q[8].measure());
    }
    
    @Test
  public void expmul3p4mod7() { // 3^4 = 81 -> mod 7 = 4
        int length = 3; 
        // q0 -> q2: a (3)
        // q3 -> q5: ancilla (0 before, 0 after)
        // q6 -> q8: result
        int a = 3;
        int mod = 7;
        Program p = new Program(3 * length);
        Step prep = new Step(new X(0), new X(1));
        Step prepAnc = new Step(new X(2 * length));
        p.addStep(prep);
        p.addStep(prepAnc);

        for (int i = 0; i < length; i++) {
            int m = (int) Math.pow(a, 1 <<  i);
            System.err.println("M = "+m);
            MulModulus mul = new MulModulus(length, 2 * length-1, m, mod);
            ControlledBlockGate cbg = new ControlledBlockGate(mul, length, i);
            p.addStep(new Step(cbg));
        }
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(9, q.length);
        System.err.println("results: ");
        for (int i = 0; i < 9; i++) {
            System.err.println("m["+i+"]: "+q[i].measure());
        }
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(0, q[3].measure());
        assertEquals(0, q[4].measure());
        assertEquals(0, q[5].measure());
        assertEquals(0, q[6].measure());
        assertEquals(1, q[7].measure());
        assertEquals(0, q[8].measure());
    }
}
