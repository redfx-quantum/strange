/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Gluon Software
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
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Cnot;
import com.gluonhq.strange.gate.Fourier;
import com.gluonhq.strange.gate.Identity;
import com.gluonhq.strange.gate.Toffoli;
import com.gluonhq.strange.gate.X;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author johan
 */
public class FourierTest extends BaseGateTests {

    static final double D = 0.000000001d;
    @Test
    public void empty() {
    }

    @Test
    public void createFourierOne() {
        Fourier f = new Fourier(1,0);
        double den = Math.sqrt(2);
        Complex[][] a = f.getMatrix();
        assertEquals(2, a.length);
        Complex val = a[0][0];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1/den);
        val = a[1][0];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1/den);
        val = a[0][1];
        assertEquals(val.i, 0);
        assertEquals(val.r, 1/den);
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
    public void FourierProgram() {
        Program p = new Program(1, new Step(new Fourier(1, 0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure()); 
    }
    
    @Test
    public void InvFourierProgram() {
        Program p = new Program(1, new Step(new Fourier(1, 0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure()); 
    }
}
