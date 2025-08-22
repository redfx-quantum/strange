/*-
 * #%L
 * Strange
 * %%
 * Copyright (C) 2020 Johan Vos
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Johan Vos nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package org.redfx.strange.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.gate.Cr;
import org.redfx.strange.gate.Cz;
import org.redfx.strange.gate.Identity;
import org.redfx.strange.gate.Measurement;
import org.redfx.strange.gate.Swap;
import org.redfx.strange.gate.X;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.Chadamard;

/**
 *
 * @author johan
 */
public class TwoQubitGateTests extends BaseGateTests {
    
    @Test
    public void empty() {
    }

    @Test
    public void doubleIGate() {
        Program p = new Program(2, new Step(new Identity(0),new Identity(1)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void oneXGate1() {
        Program p = new Program(2, new Step(new X(0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test   
    public void oneXGate2() {
        Program p = new Program(2, new Step(new X(1)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void oneXGate3() {
        Program p = new Program(2, new Step(new X(0), new X(1)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void swapGate00() {
        Program p = new Program(2,
            new Step(new Identity(0),new Identity(1)),
            new Step(new Swap(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void swapGate01() {
        Program p = new Program(2,
           new Step(new Identity(0),new X(1)),
           new Step(new Swap(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void swapGate10() {
        Program p = new Program(2,
            new Step(new Identity(1),new X(0)),
            new Step(new Swap(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[1].measure());
        assertEquals(0, qubits[0].measure());
    }

    @Test
    public void swapGate11() {
        Program p = new Program(2,
            new Step(new X(0),new X(1)),
            new Step(new Swap(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void swapGate012() {
        Program p = new Program(3,
            new Step(new X(0)),
            new Step(new Swap(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(0, qubits[2].measure());
    }

    @Test
    public void swapGate122() {
        Program p = new Program(3,
            new Step(new X(1)),
            new Step(new Swap(1,2))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void swapGate022() {
        Program p = new Program(3,
            new Step(new X(0)),
            new Step(new Swap(0,2))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void swapGate202() {
        Program p = new Program(3,
            new Step(new X(0)),
            new Step(new Swap(2,0))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void cnot01() {
        Program p = new Program(2, new Step(new Cnot(0,1)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }


    @Test
    public void cnotx01() {
        Program p = new Program(2,
            new Step(new X(0)),
            new Step(new Cnot(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void cnotx10() {
        Program p = new Program(2,
            new Step(new X(1)),
            new Step(new Cnot(1,0))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void cnotxx01() {
        Program p = new Program(2,
            new Step(new X(0), new X(1)),
            new Step(new Cnot(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        Complex[] prob = res.getProbability();
        assertEquals(4, prob.length);
        assertEquals(0, prob[0].r, DELTA);
        assertEquals(0, prob[0].i, DELTA);
        assertEquals(1, prob[1].r, DELTA);
        assertEquals(0, prob[1].i, DELTA);
        assertEquals(0, prob[2].r, DELTA);
        assertEquals(0, prob[2].i, DELTA);
        assertEquals(0, prob[3].r, DELTA);
        assertEquals(0, prob[3].i, DELTA);
    }
    @Test
    public void cnotx02() {
        Program p = new Program(3,
            new Step(new X(0)),
            new Step(new Cnot(0,2))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void cnotx20() {
        Program p = new Program(3,
            new Step(new X(2)),
            new Step(new Cnot(2,0))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void cnotx21() {
        Program p = new Program(3,
            new Step(new X(2)),
            new Step(new Cnot(2,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void cz01() {
        Program p = new Program(2, new Step(new Cz(0,1)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void czx01() {
        Program p = new Program(2,
            new Step(new X(0)),
            new Step(new Cz(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void czxx01() {
        Program p = new Program(2,
            new Step(new X(0), new X(1)),
            new Step(new Cz(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        Complex[] prob = res.getProbability();
        assertEquals(4, prob.length);
        assertEquals(0, prob[0].r, DELTA);
        assertEquals(0, prob[0].i, DELTA);
        assertEquals(0, prob[1].r, DELTA);
        assertEquals(0, prob[1].i, DELTA);
        assertEquals(0, prob[2].r, DELTA);
        assertEquals(0, prob[2].i, DELTA);
        assertEquals(-1, prob[3].r, DELTA);
        assertEquals(0, prob[3].i, DELTA);
    }

    @Test
    public void cr01() {
        Program p = new Program(2, new Step(new Cr(0,1,2,1)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void crx01() {
        Program p = new Program(2,
            new Step(new X(0)),
            new Step(new Cr(0,1,2,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void crxx01() {
        Program p = new Program(2,
            new Step(new X(0), new X(1)),
            new Step(new Cr(0,1,2,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        Complex[] prob = res.getProbability();
        assertEquals(4, prob.length);
        assertEquals(0, prob[0].r, DELTA);
        assertEquals(0, prob[0].i, DELTA);
        assertEquals(0, prob[1].r, DELTA);
        assertEquals(0, prob[1].i, DELTA);
        assertEquals(0, prob[2].r, DELTA);
        assertEquals(0, prob[2].i, DELTA);
        assertEquals(-1, prob[3].r, DELTA);
        assertEquals(0, prob[3].i, DELTA);
    }

    @Test
    public void cr01S() {
        Program p = new Program(2, new Step(new Cr(0,1,2,2)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void crx01S() {
        Program p = new Program(2,
            new Step(new X(0)),
            new Step(new Cr(0,1,2,2))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void crxx01S() {
        Program p = new Program(2,
            new Step(new X(0), new X(1)),
            new Step(new Cr(0,1,2,2))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        Complex[] prob = res.getProbability();
        assertEquals(4, prob.length);
        assertEquals(0, prob[0].r, DELTA);
        assertEquals(0, prob[0].i, DELTA);
        assertEquals(0, prob[1].r, DELTA);
        assertEquals(0, prob[1].i, DELTA);
        assertEquals(0, prob[2].r, DELTA);
        assertEquals(0, prob[2].i, DELTA);
        assertEquals(0, prob[3].r, DELTA);
        assertEquals(1, prob[3].i, DELTA);
    }

    @Test
    public void cr01T() {
        Program p = new Program(2, new Step(new Cr(0,1,2,3)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }
    @Test
    public void crx01T() {
        Program p = new Program(2,
            new Step(new X(0)),
            new Step(new Cr(0,1,2,3))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void crxx01T() {
        Program p = new Program(2,
            new Step(new X(0), new X(1)),
            new Step(new Cr(0,1,2,3))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        Complex[] prob = res.getProbability();
        assertEquals(4, prob.length);
        assertEquals(0, prob[0].r, DELTA);
        assertEquals(0, prob[0].i, DELTA);
        assertEquals(0, prob[1].r, DELTA);
        assertEquals(0, prob[1].i, DELTA);
        assertEquals(0, prob[2].r, DELTA);
        assertEquals(0, prob[2].i, DELTA);
        assertEquals(Math.sqrt(2)/2, prob[3].r, DELTA);
        assertEquals(Math.sqrt(2)/2, prob[3].i, DELTA);
    }

    @Test
    public void crxx000T() {
        Program p = new Program(3,
            new Step(new X(0), new X(2)),
            new Step(new Cr(0,2,2,3))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
        Complex[] prob = res.getProbability();
        assertEquals(8, prob.length);
        assertEquals(0, prob[0].r, DELTA);
        assertEquals(0, prob[0].i, DELTA);
        assertEquals(0, prob[1].r, DELTA);
        assertEquals(0, prob[1].i, DELTA);
        assertEquals(0, prob[2].r, DELTA);
        assertEquals(0, prob[2].i, DELTA);
        assertEquals(0, prob[3].r, DELTA);
        assertEquals(0, prob[3].i, DELTA);
        assertEquals(0, prob[4].r, DELTA);
        assertEquals(0, prob[4].i, DELTA);
        assertEquals(Math.sqrt(2)/2, prob[5].r, DELTA);
        assertEquals(Math.sqrt(2)/2, prob[5].i, DELTA);
        assertEquals(0, prob[6].r, DELTA);
        assertEquals(0, prob[6].i, DELTA);
        assertEquals(0, prob[7].r, DELTA);
        assertEquals(0, prob[7].i, DELTA);
    }

    @Test
    public void crxxinv01T() {
        Cr cr1 = new Cr(0,1,2,3);
        Cr cr2 = new Cr(0,1,2,3);
        cr2.setInverse(true);
        Program p = new Program(2,
            new Step(new X(0), new X(1)),
            new Step(cr1),
            new Step(cr2)
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        Complex[] prob = res.getProbability();
        assertEquals(4, prob.length);
        assertEquals(0, prob[0].r, DELTA);
        assertEquals(0, prob[0].i, DELTA);
        assertEquals(0, prob[1].r, DELTA);
        assertEquals(0, prob[1].i, DELTA);
        assertEquals(0, prob[2].r, DELTA);
        assertEquals(0, prob[2].i, DELTA);
        assertEquals(1, prob[3].r, DELTA);
        assertEquals(0, prob[3].i, DELTA);
    }

    @Test
    public void IMcnot() {
        Program p = new Program(2,
            new Step(new Identity(0)),
            new Step(new Measurement(0)),
            new Step(new Cnot(0,1))
        );
    }

    @Test
    public void IMcnot10() {
        Program p = new Program(2,
            new Step(new Identity(0)),
            new Step(new Measurement(0))
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> p.addStep(new Step(new Cnot(1,0))));
    }

    @Test
    public void chadamardGate() {
        int[] results = new int[2];
        for (int i = 0; i < 100; i++) {
            Program p = new Program(2,
                    new Step(new Hadamard(0)),
                    new Step(new Chadamard(0,1)));
            Result res = runProgram(p);
            Qubit[] qubits = res.getQubits();
            results[qubits[1].measure()]++;
        }
        assertTrue(results[0] > 10);
        assertTrue(results[1] > 10);
    }

    @Test
    public void chadamardXGate() {
        int[] results = new int[2];
        for (int i = 0; i < 100; i++) {
            Program p = new Program(2,
                    new Step(new X(0)),
                    new Step(new Chadamard(0,1)));
            Result res = runProgram(p);
            Qubit[] qubits = res.getQubits();
            results[qubits[1].measure()]++;
        }
        assertTrue(results[0] > 10);
        assertTrue(results[1] > 10);
    }

    @Test
    public void chadamardNotApplied() {
        int[] results = new int[2];
        for (int i = 0; i < 100; i++) {
            Program p = new Program(2,
                    new Step(new Chadamard(0,1)));
            Result res = runProgram(p);
            Qubit[] qubits = res.getQubits();
            results[qubits[1].measure()]++;
        }
        assertEquals(100, results[0]);
        assertEquals(0, results[1]);
    }

}
