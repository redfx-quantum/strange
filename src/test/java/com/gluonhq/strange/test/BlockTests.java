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
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author johan
 */
public class BlockTests extends BaseGateTests {

    @Test
    public void empty() {
    }

    @Test
    public void createBlock() {
        Block block = new Block(1);
        block.addGate(Gate.identity(0));
        BlockGate gate = new BlockGate(block, 0);
    }

    @Test
    public void addWrongIndex() {
        Block block = new Block(2);
        block.addGate(Gate.x(0));
        assertThrows(
                IllegalArgumentException.class,
                () -> block.addGate(Gate.x(2)));
        assertThrows(
                IllegalArgumentException.class,
                () -> block.addGate(Gate.cnot(2, 1)));
        block.addGate(Gate.x(1));
    }

    @Test
    public void createBlockInProgram() {
        Block block = new Block(1);
        block.addGate(Gate.identity(0));
        BlockGate gate = new BlockGate(block, 0);
        Program p = new Program(1);
        p.addStep(new Step(gate));
        Result result = runProgram(p);
        Qubit[] qubits = result.getQubits();
        assertEquals(0, qubits[0].measure());
    }
    
    @Test
    public void createXBlockInProgram() {
        Block block = new Block(1);
        block.addGate(Gate.x(0));
        BlockGate gate = new BlockGate(block, 0);
        Program p = new Program(1);
        p.addStep(new Step(gate));
        Result result = runProgram(p);
        Qubit[] qubits = result.getQubits();
        assertEquals(1, qubits[0].measure());
    }
        
    @Test
    public void createXBlockInProgramAddPos2() {
        Block block = new Block(1);
        block.addGate(Gate.x(0));
        BlockGate gate = new BlockGate(block, 1);
        Program p = new Program(2);
        p.addStep(new Step(gate));
        Result result = runProgram(p);
        Qubit[] qubits = result.getQubits();
        assertEquals(1, qubits[1].measure());
        assertEquals(0, qubits[0].measure());
    }

    @Test
    public void createXXBlockInProgramAddPos2() {
        Block block = new Block(1);
        block.addGate(Gate.x(0));
        BlockGate gate = new BlockGate(block, 1);
        BlockGate gate2 = new BlockGate(block, 0);
        BlockGate gate3 = new BlockGate(block, 1);
        Program p = new Program(2);
        p.addSteps(new Step(gate), new Step(gate2), new Step(gate3));
        Result result = runProgram(p);
        Qubit[] qubits = result.getQubits();
        assertEquals(0, qubits[1].measure());
        assertEquals(1, qubits[0].measure());
    }

}
