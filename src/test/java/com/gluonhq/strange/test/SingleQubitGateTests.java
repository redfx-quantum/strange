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
import com.gluonhq.strange.gate.Hadamard;
import com.gluonhq.strange.gate.Identity;
import com.gluonhq.strange.gate.Measurement;
import com.gluonhq.strange.gate.X;
import com.gluonhq.strange.gate.Y;
import com.gluonhq.strange.gate.Z;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingleQubitGateTests extends BaseGateTests {

    @Test
    public void empty() {
    }
        
    @Test
    public void simpleIGate() {
        Program p = new Program(1, new Step(new Identity(0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
    }

    @Test
    public void simpleXGate() {
        Program p = new Program(1, new Step(new X(0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
    }    
        
    @Test
    public void simpleIXGate() {
        Program p = new Program(2, new Step(new X(0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
    }    
            
    @Test
    public void simpleXIGate() {
        Program p = new Program(2, new Step(new X(1)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[1].measure());
    }    
               
    @Test
    public void simpleXIIGate() {
        Program p = new Program(3, new Step(new X(2)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[2].measure());
    }    
    @Test
    public void simpleYGate() {
        Program p = new Program(1, new Step(new Y(0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
    }
    
    @Test
    public void simpleZGate() {
        Program p = new Program(1, new Step(new Z(0)));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
    }   
        
    @Test
    public void simpleHGate() {
        int[] results = new int[2];
        for (int i = 0; i < 100; i++) {
            Program p = new Program(1, new Step(new Hadamard(0)));
            Result res = runProgram(p);
            Qubit[] qubits = res.getQubits();
            results[qubits[0].measure()]++;
        }
        assertTrue(results[0] > 10);
        assertTrue(results[1] > 10);
    }   
    
    @Test
    public void simpleTogetherGate() {
        Program p = new Program(4,
            new Step(new X(0),
            new Y(1),
            new Z(2),
            new Identity(3))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(0, qubits[2].measure());
        assertEquals(0, qubits[3].measure());
    }

    @Test
    public void simpleIM() {
        Program p = new Program(1,
            new Step(new Identity(0)),
            new Step(new Measurement(0))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
    }

    @Test
    public void simpleXM() {
        Program p = new Program(1,
            new Step(new X(0)),
            new Step(new Measurement(0))
        );
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
    }

    @Test
    public void simpleXMH() {
        Program p = new Program(1,
            new Step(new X(0)),
            new Step(new Measurement(0))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> p.addStep(new Step(new Hadamard(0))));
    }


}
