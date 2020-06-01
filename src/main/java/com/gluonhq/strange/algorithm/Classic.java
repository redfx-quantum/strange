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
package com.gluonhq.strange.algorithm;

import com.gluonhq.strange.Complex;
import com.gluonhq.strange.Gate;
import com.gluonhq.strange.Program;
import com.gluonhq.strange.QuantumExecutionEnvironment;
import com.gluonhq.strange.Qubit;
import com.gluonhq.strange.Result;
import com.gluonhq.strange.Step;
import com.gluonhq.strange.gate.Cr;
import com.gluonhq.strange.gate.Fourier;
import com.gluonhq.strange.gate.Hadamard;
import com.gluonhq.strange.gate.InvFourier;
import com.gluonhq.strange.gate.Oracle;
import com.gluonhq.strange.gate.ProbabilitiesGate;
import com.gluonhq.strange.gate.X;
import com.gluonhq.strange.local.SimpleQuantumExecutionEnvironment;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author johan
 */
public class Classic {
    
    public static int randomBit() {
        Program program = new Program(1, new Step(new Hadamard(0)));
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
        Result result = qee.runProgram(program);
        Qubit[] qubits = result.getQubits();
        int answer = qubits[0].measure();
        return answer;
    }
    
    /**
     * Use a quantum addition algorithm to compute the sum of a and b
     * @param a first term
     * @param b second term
     * @return the sum of a and b
     */
    public static int qsum(int a, int b) {
        int y = a > b ? a : b;
        int x = a > b ? b : a;
        int m = y < 2? 1 : 1 + (int) Math.ceil(Math.log(y)/Math.log(2));
        int n = x < 2? 1 : 1 + (int) Math.ceil(Math.log(x)/Math.log(2));
        Program program = new Program(m + n);
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
        Step prep = new Step();
        int y0 = y;
        for (int i = 0; i < m; i++) {
            int p = 1 << (m-i-1);
            if (y0 >= p) {
                prep.addGate(new X(m-i-1));
                y0 = y0-p;
            }
        }
        int x0 = x;
        for (int i = 0; i < n; i++) {
            int p = 1 << (n-i-1);
            if (x0 >= p) {
                prep.addGate(new X(m+n-i-1));
                x0 = x0-p;
            }   
        }
        program.addStep(prep);
        program.addStep(new Step(new Fourier(m, 0)));
        for (int i = 0; i < m; i++) {
           for (int j = 0; j < m-i ; j++) {
                int cr0 = 2 *m-j-i-1;
                if (cr0 < m + n) {
                    Step s = new Step(new Cr(i, cr0,2,1+j));
                    program.addStep(s);
                }
            }
        }
        program.addStep(new Step(new InvFourier(m, 0)));
        Result res = qee.runProgram(program);
        Qubit[] qubits = res.getQubits();
        int answer = 0;
        for (int i = 0; i < m; i++) {
            if (qubits[i].measure() == 1) {
                answer = answer + (1<<i);
            }
        }
        return answer;
    }
    
    /**
     * Apply Grover's search algorithm to find the element from the supplied
     * list that would evaluate the provided function to 1
     * @param <T> the type of the element 
     * @param list the list of all elements that need to be searched into
     * @param function the function that, when evaluated, returns 0 for all
     * elements except for the element that this method returns, which evaluation
     * leads to 1.
     * @return the single element from the provided list that, when evaluated
     * by the function, returns 1.
     */
    public static<T> T search(List<T> list, Function<T, Integer> function) {
        int size = list.size();
        int n = (int) Math.ceil((Math.log(size)/Math.log(2)));
        int N = 1 << n;
        double cnt = Math.PI*Math.sqrt(N)/4;

        Oracle oracle = createGroverOracle(n, list, function);
                QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();

        Program p = new Program(n);
        Step s0 = new Step();
        for (int i = 0; i < n; i++) {
            s0.addGate(new Hadamard(i));
        }
        p.addStep(s0);
        oracle.setCaption("O");
        Complex[][] dif = createDiffMatrix(n);
        Oracle difOracle = new Oracle(dif);
        difOracle.setCaption("D");
        for (int i = 1; i < cnt; i++) {
            Step s1 = new Step("Oracle "+i);
            s1.addGate(oracle);
            Step s2 = new Step("Diffusion "+i);
            s2.addGate(difOracle);
            Step s3 = new Step("Prob "+i);
            s3.addGate(new ProbabilitiesGate(0));
            p.addStep(s1);
            p.addStep(s2);
            p.addStep(s3);
        }
        System.out.println(" n = "+n+", steps = "+cnt);

        Result res = qee.runProgram(p);
        Complex[] probability = res.getProbability();
        int winner = 0;
        double wv = 0;
        for (int i = 0 ; i < probability.length; i++) {
            double a = probability[i].abssqr();
            if (a > wv) {
                wv = a;
                winner = i;
            }
        }
        System.err.println("winner = "+winner+" with prob "+wv);
        return list.get(winner);
    }

    private static<T> Oracle createGroverOracle(int n, List<T> list, Function<T, Integer> function) {
        int N = 1 << n;
        Complex[][] matrix = new Complex[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = (i!=j) ? Complex.ZERO : 
                        function.apply(list.get(i)) == 0 ? 
                        Complex.ONE : Complex.ONE.mul(-1);
            }
        }
        return new Oracle(matrix);
    }

    private static Complex[][] createDiffMatrix(int n) {
        int N = 1<<n;
        Gate g = new Hadamard(0);
        Complex[][] matrix = g.getMatrix();
        Complex[][] h2 = matrix;
        for (int i = 1; i < n; i++) {
            h2 = Complex.tensor(h2, matrix);
        }
        Complex[][] I2 = new Complex[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <N;j++) {
                if (i!=j) {
                    I2[i][j] = Complex.ZERO;
                } else {
                    I2[i][j] = Complex.ONE;
                }
            }
        }
        I2[0][0] = Complex.ONE.mul(-1);
        int nd = n<<1;

        Complex[][] inter1 = Complex.mmul(h2,I2);
        Complex[][] dif = Complex.mmul(inter1, h2);
        return dif;
    }

}
