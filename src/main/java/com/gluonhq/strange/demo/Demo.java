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
package com.gluonhq.strange.demo;

import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.*;
import com.gluonhq.strange.local.Computations;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Demo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Hello, demo");
        Program p = new Program(4);
        Gate yGate = new Y(0);
        Gate xGate = new X(1);
        Gate zGate = new Z(3);
        Step step = new Step();
        step.addGates(yGate, xGate, zGate);
        p.addStep(step);
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result res = sqee.runProgram(p);
        Qubit[] qubits = res.getQubits();
        Arrays.asList(qubits).forEach(q -> System.out.println(q.measure()));
        Arrays.asList(res.getProbability()).forEach(c -> System.out.println("prob = "+c));
        Complex[][] perm = sqee.createPermutationMatrix(1,2,3);
        for (int i = 0; i < perm.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < perm[i].length; j++) {
                 sb.append(perm[i][j]).append("   ");
            }
            System.out.println("sb = "+sb);
        }
        PermutationGate pg = new PermutationGate(0,2,3);
        Complex[][] m = pg.getMatrix();
        printMatrix(m);
        
        multiplyMod5x3andswapandclean();
    }

    private static void printMatrix(Complex[][] a) {
        for (int i = 0; i < a.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < a[i].length; j++) {
                sb.append(a[i][j]).append("    ");
            }
            System.out.println("m["+i+"]: "+sb);
        }
    }
    
    public static void multiplyMod5x3andswapandclean() { // 5 x 3 mod 6 = 3
        Program p = new Program(9);
        Step prep = new Step();
        int mul = 5;
        int N = 6;
        prep.addGates(new X(4), new X(5)); // 3 in high register
      //  p.addStep(prep);
        for (int i = 0; i < mul; i++) {
            AddModulus add = new AddModulus(0, 3, 4, 7, N);
            p.addStep(new Step(add));
        }
        p.addStep(new Step( new Swap(0,4)));
        p.addStep(new Step( new Swap(1,5)));
        p.addStep(new Step( new Swap(2,6)));
        p.addStep(new Step( new Swap(3,7)));

        int invsteps = Computations.getInverseModulus(mul,N);
        for (int i = 0; i < invsteps; i++) {
            AddModulus add = new AddModulus(0, 3, 4, 7, N).inverse();
            p.addStep(new Step(add));
        }
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result result = sqee.runProgram(p);
        Qubit[] q = result.getQubits();

    }
}
