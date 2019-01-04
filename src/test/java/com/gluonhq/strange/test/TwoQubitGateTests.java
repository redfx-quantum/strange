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
import com.gluonhq.strange.gate.Identity;
import com.gluonhq.strange.gate.Swap;
import com.gluonhq.strange.gate.X;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

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
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new Identity(0));
        s0.addGate(new Identity(1));
        p.addStep(s0);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }
    
    @Test
    public void swapGate00() {
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new Identity(0));
        s0.addGate(new Identity(1));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(0,1));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }    
         
    @Test
    public void swapGate01() {
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new Identity(0));
        s0.addGate(new X(1));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(0,1));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }
    
    @Test
    public void swapGate10() {
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new Identity(1));
        s0.addGate(new X(0));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(0,1));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[1].measure());
        assertEquals(0, qubits[0].measure());
    }
    
    @Test
    public void swapGate11() {
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new X(0));
        s0.addGate(new X(1));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(0,1));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }    
    
    @Test
    public void swapGate012() {
        Program p = new Program(3);
        Step s0 = new Step();
        s0.addGate(new X(0));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(0,1));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(0, qubits[2].measure());
    }  
        
    @Test
    public void swapGate122() {
        Program p = new Program(3);
        Step s0 = new Step();
        s0.addGate(new X(1));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(1,2));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }
    
    @Test
    public void swapGate022() {
        Program p = new Program(3);
        Step s0 = new Step();
        s0.addGate(new X(0));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(0,2));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }
    
    @Test
    public void swapGate202() {
        Program p = new Program(3);
        Step s0 = new Step();
        s0.addGate(new X(0));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Swap(2,0));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }
    
    @Test
    public void cnot01() {
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new Cnot(0,1));
        p.addStep(s0);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
    }    
      
          
    @Test
    public void cnotx01() {
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new X(0));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Cnot(0,1));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }
    
    @Test
    public void cnotx10() {
        Program p = new Program(2);
        Step s0 = new Step();
        s0.addGate(new X(1));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Cnot(1,0));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(2, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
    }
    
    @Test
    public void cnotx02() {
        Program p = new Program(3);
        Step s0 = new Step();
        s0.addGate(new X(0));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Cnot(0,2));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void cnotx20() {
        Program p = new Program(3);
        Step s0 = new Step();
        s0.addGate(new X(2));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Cnot(2,0));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(1, qubits[0].measure());
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }

    @Test
    public void cnotx21() {
        Program p = new Program(3);
        Step s0 = new Step();
        s0.addGate(new X(2));
        p.addStep(s0);
        Step s1 = new Step();
        s1.addGate(new Cnot(2,1));
        p.addStep(s1);
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(3, qubits.length);
        assertEquals(0, qubits[0].measure());
        assertEquals(1, qubits[1].measure());
        assertEquals(1, qubits[2].measure());
    }
}
