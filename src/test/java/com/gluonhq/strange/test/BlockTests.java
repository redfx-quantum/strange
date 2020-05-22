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
import com.gluonhq.strange.gate.Cnot;
import com.gluonhq.strange.gate.Toffoli;
import com.gluonhq.strange.gate.X;

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
        block.addStep(new Step(Gate.identity(0)));
        BlockGate gate = new BlockGate(block, 0);
    }

//    @Test
//    public void addWrongIndex() {
//        Block block = new Block(2);
//        block.addStep(new Step(Gate.x(0)));
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> block.addGate(Gate.x(2)));
//        assertThrows(
//                IllegalArgumentException.class,
//                () -> block.addGate(Gate.cnot(2, 1)));
//        block.addGate(Gate.x(1));
//    }

    @Test
    public void createBlockInProgram() {
        Block block = new Block(1);
        block.addStep (new Step(Gate.identity(0)));
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
        Step bs = new Step(Gate.x(0));
        block.addStep(bs);
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
        block.addStep( new Step(Gate.x(0)));
        BlockGate gate = new BlockGate(block, 1);
        Program p = new Program(2);
        p.addStep(new Step(gate));
        Result result = runProgram(p);
        Qubit[] qubits = result.getQubits();
        assertEquals(1, qubits[1].measure());
        assertEquals(0, qubits[0].measure());
    }

    @Test
    public void createManyXBlockInProgramAddPos2() {
        Block block = new Block(1);
        block.addStep(new Step(Gate.x(0)));
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

    @Test
    public void createXXBlockInProgram() {
        Block block = new Block(1);
        block.addStep(new Step(Gate.x(0)));
        block.addStep(new Step(Gate.x(0)));
        BlockGate gate = new BlockGate(block, 0);
        Program p = new Program(1);
        p.addSteps(new Step(gate));
        Result result = runProgram(p);
        Qubit[] qubits = result.getQubits();
        assertEquals(0, qubits[0].measure());
    }
    
    @Test
    public void createXXBlockInProgramAddPos2() {
        Block block = new Block(1);
        block.addStep(new Step(Gate.x(0)));
        block.addStep(new Step(Gate.x(0)));
        BlockGate gate = new BlockGate(block, 1);
        Program p = new Program(2);
        p.addSteps(new Step(gate));
        Result result = runProgram(p);
        Qubit[] qubits = result.getQubits();
        assertEquals(0, qubits[1].measure());
        assertEquals(0, qubits[0].measure());
    }
    
    @Test
    public void compareBlock() {
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                Step prep = new Step();
                if (a % 2 == 1) {
                    prep.addGate(new X(1));
                }
                if (b % 2 == 1) {
                    prep.addGate(new X(2));
                }
                if (a / 2 == 1) {
                    prep.addGate(new X(4));
                }
                if (b / 2 == 1) {
                    prep.addGate(new X(5));
                }
                Program p = new Program(7);
                Step s0 = new Step(new Toffoli(1, 2, 3));
                Step s1 = new Step(new Cnot(1, 2));
                Step s2 = new Step(new Toffoli(0, 2, 3));
                p.addSteps(prep, s0, s1, s2);
                Result normal = runProgram(p);
                Qubit[] normalQ = normal.getQubits();
                System.err.println("CREATE BLOCK NOW");
                Block carry = new Block("carry", 4);
                carry.addStep(new Step(new Toffoli(1, 2, 3)));
                carry.addStep(new Step(new Cnot(1, 2)));
                carry.addStep(new Step(new Toffoli(0, 2, 3)));

                Program bp = new Program(7);
                Step bs0 = new Step(new BlockGate(carry, 0));
                bp.addSteps(prep, bs0);
                Result blockResult = runProgram(bp);
                Qubit[] blockQ = blockResult.getQubits();

                assertEquals(normalQ.length, blockQ.length);
                for (int i = 0; i < normalQ.length; i++) {
                    assertEquals(normalQ[i].measure(), blockQ[i].measure());
                }
            }
        }
    }

    @Test
    public void compareBlock2() {
        for (int a = 1; a < 2; a++) {
            for (int b = 0; b < 1; b++) {
                Step prep = new Step();
                if (a % 2 == 1) {
                    prep.addGate(new X(1));
                }
                if (b % 2 == 1) {
                    prep.addGate(new X(2));
                }

                Program p = new Program(4);
             
                Step s0 = new Step(new Toffoli(1,2,3));
                Step s1 = new Step(new Cnot(1, 2));
                p.addSteps(prep, s0, s1);
                Result normal = runProgram(p);
                Qubit[] normalQ = normal.getQubits();
                System.err.println("q0 = "+normalQ[0].measure());
                System.err.println("q1 = "+normalQ[1].measure());
                System.err.println("q2 = "+normalQ[2].measure());
                
                System.err.println("\nCREATE BLOCK NOW");
                Block carry = new Block("carry", 4);
                carry.addStep(new Step(new Toffoli(1,2,3)));
                carry.addStep(new Step(new Cnot(1, 2)));


                Program bp = new Program(4);
                Step bs0 = new Step(new BlockGate(carry, 0));
                bp.addSteps(prep, bs0);
                Result blockResult = runProgram(bp);
                Qubit[] blockQ = blockResult.getQubits();
                System.err.println("qq0 = "+blockQ[0].measure());
                System.err.println("qq1 = "+blockQ[1].measure());
                System.err.println("qq2 = "+blockQ[2].measure());

                assertEquals(normalQ.length, blockQ.length);
                for (int i = 0; i < normalQ.length; i++) {
                    assertEquals(normalQ[i].measure(), blockQ[i].measure());
                }
            }
        }
    }
}
