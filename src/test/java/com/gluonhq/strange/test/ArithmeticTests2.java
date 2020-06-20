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
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Add;
import com.gluonhq.strange.gate.AddInteger;
import com.gluonhq.strange.gate.Cnot;
import com.gluonhq.strange.gate.X;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author johan
 */
public class ArithmeticTests2 extends BaseGateTests {

    static final double D = 0.000000001d;
    
   // @Test
    public void addmod() {
        int n = 2;
        int N = 3;
        int dim = 2 * (n+1)+1;
        Program p = new Program(dim);
        Step prep = new Step();
       // prep.addGates(new X(0), new X(2));
        p.addStep(prep);
        Add add = new Add(0,2,3,5);
        p.addStep(new Step(add));
        AddInteger min = new AddInteger(0,2,N).inverse();
        p.addStep(new Step(min));
        p.addStep(new Step(new Cnot(0,dim-1)));
        
        AddInteger addN = new AddInteger(0,2,N);

        ControlledBlockGate cbg = new ControlledBlockGate(addN, 0,dim-1);

        
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(7, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
        assertEquals(1, q[6].measure());
    }
     
  //  @Test
    public void controlledAdd001() {
        Program p = new Program(3);
        Step s = new Step();
        Add add = new Add(1,1,2,2);
        ControlledBlockGate cbg = new ControlledBlockGate(add, 1,0);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);
        Step prep = new Step();
        prep.addGates(new X(0));
        p.addStep(prep);
        Step cg = new Step(cbg);
        p.addStep(cg);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(4, q.length);
        assertEquals(1, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(0, q[2].measure());
    }
    
            
    @Test // q_0 = q_1(0) + q_0 (0) = 0
    public void controlledAdd100() {
        Program p = new Program(3);
        Step s = new Step();
        Add add = new Add(0,0,1,1);
        ControlledBlockGate cbg = new ControlledBlockGate(add, 0,2);
        Complex[][] m = cbg.getMatrix();
        Complex.printMatrix(m, System.err);
        Step prep = new Step();
        prep.addGates(new X(2));
        p.addStep(prep);
        Step cg = new Step(cbg);
        p.addStep(cg);
        Result result = runProgram(p);
        Qubit[] q = result.getQubits();
        assertEquals(3, q.length);
        assertEquals(0, q[0].measure());
        assertEquals(0, q[1].measure());
        assertEquals(1, q[2].measure());
    }
         
}
