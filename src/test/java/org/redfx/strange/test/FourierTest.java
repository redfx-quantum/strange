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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Tag;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Cr;
import org.redfx.strange.gate.Fourier;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.InvFourier;
import org.redfx.strange.gate.X;


/**
 *
 * @author johan
 */
public class FourierTest extends BaseGateTests {

    static final double D = 0.000001d;

    @Test
    public void createFourierOne() {
        Fourier f = new Fourier(1,0);
        float den = (float) Math.sqrt(2);
        Complex[][] a = f.getMatrix();
        assertEquals(2, a.length);
        Complex val = a[0][0];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1/den,D);
        val = a[1][0];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1/den,D);
        val = a[0][1];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1/den,D);
        val = a[1][1];
        assertEquals(val.i,0, D);
        assertEquals(val.r,-1/den, D);
    }

    @Test
    public void createFourierTwo() {
        Fourier f = new Fourier(2,0);
        f.getMatrix();
        Complex[][] a = f.getMatrix();
        assertEquals(a.length, 4);
        Complex val = a[0][0];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1d/2);
        val = a[0][1];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1d/2);
        val = a[0][2];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1d/2);
        val = a[0][3];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1d/2);
        val = a[1][0];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1d/2);
        val = a[1][1];
        assertEquals(val.i, 1d/2, D);
        assertEquals(val.r, 0, D);
        val = a[1][2];
        assertEquals(val.i, 0, D);
        assertEquals(val.r, -1d/2, D);
        val = a[1][3];
        assertEquals(val.i, -1d/2, D);
        assertEquals(val.r, 0, D);
        val = a[2][0];
        assertEquals(val.i, 0, D);
        assertEquals(val.r, 1d/2, D);
        val = a[2][1];
        assertEquals(val.i, 0, D);
        assertEquals(val.r, -1d/2, D);
        val = a[2][2];
        assertEquals(val.i, 0, D);
        assertEquals(val.r, 1d/2, D);
        val = a[2][3];
        assertEquals(val.i, 0, D);
        assertEquals(val.r, -1d/2, D);
        val = a[3][0];
        assertEquals(val.i, 0, D);
        assertEquals(val.r, 1d/2, D);
        val = a[3][1];
        assertEquals(val.i, -1d/2, D);
        assertEquals(val.r, 0, D);
        val = a[3][2];
        assertEquals(val.i, 0, D);
        assertEquals(val.r, -1d/2, D);
        val = a[3][3];
        assertEquals(val.i, 1d/2, D);
        assertEquals(val.r, 0, D);
    }
    
    
    @Test
    public void fourierProgram() {
        Program p = new Program(1, new Step(new Fourier(1, 0)));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        assertEquals (2, probability.length);
        assertEquals(.5, probability[0].abssqr(),D);
        assertEquals(.5, probability[1].abssqr(),D  );
    }
          
    @Test
    public void fourierPartProgram() {
        Program p = new Program(2, new Step(new Fourier(1, 0)));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        assertEquals(4, probability.length);
        assertEquals(.5, probability[0].abssqr(),D);
        assertEquals(.5, probability[1].abssqr(),D  );
    }

    @Test
    public void fourierPartTwoProgram() {
        Program p = new Program(2, new Step(new Fourier(1, 0)));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        assertEquals(4, probability.length);
        assertEquals(.5, probability[0].abssqr(),D);
        assertEquals(.5, probability[1].abssqr(),D  );
    }

    @Test
    public void invFourierProgram() {
        Step prep = new Step(new X(1));
        Program p = new Program(2,
                prep,
                new Step(new Fourier(2, 0)),
                new Step(new InvFourier(2,0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void invFourierPartProgram() {
        Step prep = new Step(new X(1));
        Program p = new Program(3,
                prep,
                new Step(new Fourier(2, 0)),
                new Step(new InvFourier(2,0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    // additions with 1 + 1 qubit
    // result in q0
    @Test
    public void fourierAdditionProgram00() {
        Program p = new Program(2,
                new Step(new Fourier(1, 0)),
                new Step (new Cr(0, 1, 2, 1)),
                new Step(new InvFourier(1,0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void fourierAdditionProgram01() {
        Step prep = new Step(new X(1));
        Program p = new Program(2,
                prep,
                new Step(new Fourier(1, 0)),
                new Step (new Cr(0, 1, 2, 1)),
                new Step(new InvFourier(1,0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void fourierAdditionProgram10() {
        Step prep = new Step(new X(0));
        Program p = new Program(2,
                prep,
                new Step(new Fourier(1, 0)),
                new Step (new Cr(0, 1, 2, 1)),
                new Step(new InvFourier(1,0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }

    @Test
    public void fourierAdditionProgram11() {
        Step prep = new Step(new X(0), new X(1));
        Program p = new Program(2,
                prep,
                new Step(new Fourier(1, 0)),
                new Step (new Cr(0, 1, 2, 1)),
                new Step(new InvFourier(1,0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }

    @Test
    public void fourierAdditionProgram0000() {
        Program p = new Program(4,
                new Step(new Fourier(2, 0)),
                new Step (new Cr(1, 3, 2, 1)),
                new Step (new Cr(1, 2, 2, 2)),
                new Step (new Cr(0, 2, 2, 1)),
                new Step(new InvFourier(2,0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(0, qubits[2].measure());
        assertEquals(0, qubits[3].measure());
    }

//     0101 -> 0110
    @Test
    public void fourierAdditionProgram0101() {
        Step prep = new Step(new X(0), new X(2));
        Program p = new Program(4,
                prep,
                new Step(new Fourier(2, 0)),
                new Step (new Cr(0,3, 2, 1)),
                new Step (new Cr(0,2, 2, 2)),
                new Step (new Cr(1,2, 2, 1)),
                new Step(new InvFourier(2,0)));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        Complex.printArray(probability);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
        assertEquals(0, qubits[3].measure());
    }
//     1010 -> 0010
    @Test
    public void fourierAdditionProgram1010() {
        Step prep = new Step(new X(1), new X(3));
        Program p = new Program(4,
                prep,
                new Step(new Fourier(2, 0)),
                new Step (new Cr(0,3, 2, 1)),
                new Step (new Cr(0,2, 2, 2)),
                new Step (new Cr(1,2, 2, 1)),
                new Step(new InvFourier(2,0)));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        Complex.printArray(probability);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(0, qubits[2].measure());
        assertEquals(1, qubits[3].measure());
    }
    
    // 0011 -> 1111
    @Test
    public void fourierAdditionProgram0011() {
        Step prep = new Step(new X(2), new X(3));
        Program p = new Program(4,
                prep,
                new Step(new Fourier(2, 0)),
                new Step (new Cr(2,1, 2, 1)),
                new Step (new Cr(2,0, 2, 2)),
                new Step (new Cr(3,0, 2, 1)),
                new Step(new InvFourier(2,0)));
//                new Step(new Fourier(2,0).inverse()));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        Complex.printArray(probability);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
        assertEquals(1, qubits[3].measure());
    }

    @Test
    public void fourier3() {
        Program p = new Program(3);
        Step prep = new Step(new X(0), new X(1), new X(2));
        Step fourier = new Step(new Fourier(3,0));
        p.addStep(prep);
        p.addStep(new Step(new Hadamard(1)));
        p.addStep(fourier);
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        Complex.printArray(probability);
    }
 
    @Test
    public void fourier4() {
        Program p = new Program(4);
        Step prep = new Step(new X(3));
        Step fourier = new Step(new Fourier(4,0));
        p.addStep(prep);
        p.addStep(fourier);
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        Complex.printArray(probability);
    }

    @Tag("performance")
    @Test
    public void perfFourierProgram() { // 11 x 13 mod 97 = 46
        Complex.resetCounters();
        Program p = new Program(1, new Step(new Fourier(1, 0)));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        assertEquals(2, probability.length);
        assertEquals(.5, probability[0].abssqr(), D);
        assertEquals(.5, probability[1].abssqr(), D);
        printComplexStats(System.err);
    }

    @Tag("performance")
    @Test
    public void perfFourierAdditionProgram0011() {
        Complex.resetCounters();
        Step prep = new Step(new X(2), new X(3));
        Program p = new Program(4,
                prep,
                new Step(new Fourier(2, 0)),
                new Step (new Cr(1,2, 2, 1)),
                new Step (new Cr(0,2, 2, 2)),
                new Step (new Cr(0,3, 2, 1)),
                new Step(new InvFourier(2,0)));
        Result res = runProgram(p);
        Complex[] probability = res.getProbability();
        Complex.printArray(probability);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
        assertEquals(1, qubits[3].measure());
        printComplexStats(System.err);
    }
}
