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

import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Cnot;
import com.gluonhq.strange.gate.Hadamard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author johan
 */
public class BellStateTest extends BaseGateTests {
    
    @Test
    public void empty() {
    }
        

    @Test
    public void hcnot01() {
        Program p = new Program(2,
          new Step(new Hadamard(0)),
          new Step(new Cnot(0,1))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        int q0 = qubits[0].measure();
        int q1 = qubits[1].measure();
        assertEquals(q0,q1);
    }

    /**
     * When making multiple measurements on the same Result object, we
     * should be able to see different outcomes (0-0 or 1-1)
     */
    @Test
    public void multimeasurement() {
        Program p = new Program(2,
            new Step(new Hadamard(0)),
            new Step(new Cnot(0,1))
        );
        Result res = runProgram(p);
        int zeroCount = 0;
        final int RUNS = 100;
        for (int i = 0; i < 100; i++) {
            res.measureSystem();
            Qubit[] qubits = res.getQubits();
            int q0 = qubits[0].measure();
            int q1 = qubits[1].measure();
            assertEquals(q0,q1);
            if (q0 == 0) zeroCount++;
        }
        assertTrue(zeroCount > 0);
        assertTrue(zeroCount < RUNS);
    }

    /**
     * BellState with a third qubit that is sent through a H gate
     */
    @Test
    public void cnotH() {
        Program p = new Program(3,
            new Step(new Hadamard(0)),
            new Step(new Cnot(0,1)),
            new Step(new Hadamard(2))
        );
        Result res = runProgram(p);
        int zeroCount = 0;
        int q2count0 = 0;
        final int RUNS = 100;
        for (int i = 0; i < RUNS; i++) {
            res.measureSystem();
            Qubit[] qubits = res.getQubits();
            int q0 = qubits[0].measure();
            int q1 = qubits[1].measure();
            int q2 = qubits[2].measure();
            assertEquals(q0,q1);
            if (q0 == 0) zeroCount++;
            if (q2 == 0) q2count0++;
        }
        assertTrue(zeroCount > 0);
        assertTrue(zeroCount < RUNS);
        assertTrue(q2count0 > 0);
        assertTrue(q2count0 < RUNS);
    }
}
