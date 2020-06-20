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
import com.gluonhq.strange.ControlledBlockGate;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Cnot;
import com.gluonhq.strange.gate.Toffoli;
import com.gluonhq.strange.gate.X;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author johan
 */
public class ControlledBlockTests extends BaseGateTests {
    
    @Test
    public void cnotblock00() {
        Program p = new Program(2);
        Block block = new Block(1);
        block.addStep(new Step(new X(0)));
        BlockGate gate = new BlockGate(block, 0);
        ControlledBlockGate cbg = new ControlledBlockGate(gate, 0,1 );
        p.addStep(new Step(cbg));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
    }
             
    @Test
    public void cnotblock10() { //10 -> 11
        // q0 -----X-- 1
        //         |
        // q1 --X--o-- 1
        Program p = new Program(2);
        Step prep = new Step(new X(1));
        Block block = new Block(1);
        block.addStep(new Step(new X(0)));
        BlockGate gate = new BlockGate(block, 0);
        ControlledBlockGate cbg = new ControlledBlockGate(gate, 0, 1);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);

        p.addStep(prep);
        p.addStep(new Step(cbg));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
    }
    
             
    @Test
    public void cnotblock01() { //01 -> 11
        // q0 --X--o-- 1
        //         |
        // q1 -----X-- 1
        Program p = new Program(2);
        Step prep = new Step(new X(0));
        Block block = new Block(1);
        block.addStep(new Step(new X(0)));
        BlockGate gate = new BlockGate(block, 0);
        ControlledBlockGate cbg = new ControlledBlockGate(gate, 1, 0);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);

        p.addStep(prep);
        p.addStep(new Step(cbg));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(2, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(1, q[1].measure());
    }
                     
    @Test
    public void cnotblock100a() { //100 -> 110
        Program p = new Program(3);
        Step prep = new Step(new X(2));
        Block block = new Block(1);
        block.addStep(new Step(new X(0)));
        BlockGate gate = new BlockGate(block, 0);
        ControlledBlockGate cbg = new ControlledBlockGate(gate, 1, 2);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);

        p.addStep(prep);
        p.addStep(new Step(cbg));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(1, q[1].measure());
        assertEquals(1, q[2].measure());
    }
        
   @Test
    public void cnotblock100() { //100 -> 101
        Program p = new Program(3);
        Step prep = new Step(new X(2));
        Block block = new Block(1);
        block.addStep(new Step(new X(0)));
        BlockGate gate = new BlockGate(block, 0);
        ControlledBlockGate cbg = new ControlledBlockGate(gate, 0, 2);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);

        p.addStep(prep);
        p.addStep(new Step(cbg));
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(1, q[2].measure());
    }
    
}
