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
import com.gluonhq.strange.Program;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Demo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("Hello, demo");
        Program p = new Program(4);
        Step s = new Step();
        s.addGate(new Y(0));
        s.addGate(new X(1));
        s.addGate(new Z(3));
        p.addStep(s);
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
}
